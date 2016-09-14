package hr.fer.zemris.java.webserver;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Pametni HTTP server i njegova implementacija. Koristi se thread pool za
 * komunikaciju s klijentima. Sve konfiguracije se povuku iz konfiguracijskih
 * datoteka.
 * 
 * @author Jure Šiljeg
 *
 */
public class SmartHttpServer {
	/**
	 * Staza do prve konfiguracijske datoteke koja se poziva prilikom pokretanja
	 * servera.
	 */
	private static final String MAIN_SERVER_CONFIG_FILE = "./config/server.properties";
	/** Sdresa servera. */
	private String address;
	/** Port pomoću kojeg sever vrši komunikaciju. */
	private int port;
	/** Broj radničkih dretvi. */
	private int workerThreads;
	/** Vrijeme odziva na koje je server spreman čekati. */
	private int sessionTimeout;
	/** Mapa mime tipova. */
	private Map<String, String> mimeTypes = new HashMap<String, String>();
	/** Mapa radnika. */
	private Map<String, IWebWorker> workersMap = new HashMap<>();
	/** Mapa sesija. */
	private volatile Map<String, SessionMapEntry> sessions = new HashMap<String, SmartHttpServer.SessionMapEntry>();
	/** Random seeder koji će služiti za stvaranje novih stringova za SID. */
	private Random sessionRandom = new Random();
	/** Serverska dretva. */
	private ServerThread serverThread;
	/** Bazen dretvi. */
	private ExecutorService threadPool;
	/**
	 * Korijen glavnog dokumenta nad kojim ćemo eventualno raditi parsiranje.
	 */
	private Path documentRoot;
	/** Varijabla označava radimo li još nešto ili ne. */
	private boolean isRunning;

	/**
	 * Konstructor za SmartHttpServer. Prima ime datoteke iz koje povučemo
	 * svojstva servera.
	 * 
	 * @param configFileName
	 *            Ime datoteke za konfiguraciju.
	 */
	public SmartHttpServer(String configFileName) {
		Properties prop = loadProperties(configFileName);
		address = prop.getProperty("server.address");
		port = Integer.valueOf(prop.getProperty("server.port"));
		workerThreads = Integer.valueOf(prop.getProperty("server.workerThreads"));
		documentRoot = Paths.get(prop.getProperty("server.documentRoot"));
		readMimeConfig("./config/" + prop.getProperty("server.mimeConfig"));
		sessionTimeout = Integer.valueOf(prop.getProperty("session.timeout"));
		readWorkers("./config/" + prop.getProperty("server.workers"));

	}

	/**
	 * Metoda za učitavanje svojstava i pripadnih ključeva iz mime datoteke.
	 * 
	 * @param readMimeConfig
	 *            ime konfiguracijske datoteke za mime.
	 */
	private void readMimeConfig(String readMimeConfig) {
		Properties prop = loadProperties(readMimeConfig);
		for (String key : prop.stringPropertyNames()) {
			String value = prop.getProperty(key);
			mimeTypes.put(key, value);
		}
	}

	/**
	 * Metoda za učitavanje svojstava i pripadnih ključeva iz workers datoteke.
	 * 
	 * @param readWorkers
	 *            ime konfiguracijske datoteke za radnike.
	 */
	private void readWorkers(String readWorkers) {
		Properties prop = loadProperties(readWorkers);
		for (String key : prop.stringPropertyNames()) {
			// pazit na parsiranje i komentare i sl...
			String path = key;
			String fqcn = prop.getProperty(key);

			Class<?> referenceToClass = null;
			try {
				referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
			} catch (ClassNotFoundException e) {
				System.out.println("Razred nije pronađen!");
				System.exit(-1);
			}
			Object newObject = null;
			try {
				newObject = referenceToClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				System.out.println("Neuspješno stvaranje nove instance!");
				System.exit(-1);
			}
			IWebWorker iww = (IWebWorker) newObject;
			workersMap.put(path, iww);
		}

	}

	/**
	 * Povuče svojstva za zadno ime konfiguracijske datoteke.
	 * 
	 * @param configFileName
	 *            ime zadane datoteke.
	 * @return tražena svojstva.
	 */
	private Properties loadProperties(String configFileName) {
		Properties prop = new Properties();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new BufferedInputStream(new FileInputStream(configFileName)), StandardCharsets.UTF_8));
			prop.load(br);
		} catch (IOException e) {
			System.err.println("Datoteka " + configFileName + " nije pronađena!");
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prop;
	}

	/**
	 * Metoda za pokretanje servera. Uključuje server u rad i njegove dretve za
	 * prihvaćanje radnika.
	 */
	protected synchronized void start() {
		// … start server thread if not already running …
		// … init threadpool by Executors.newFixedThreadPool(...); …
		if (!isRunning) {
			serverThread = new ServerThread();
			threadPool = Executors.newFixedThreadPool(workerThreads);
			serverThread.run();
			isRunning = true;
		}
	}

	/**
	 * Metoda za zustavljanej rada servera. Zaustavlja serversku dretvu i "gasi"
	 * bazen dretvi.
	 * 
	 */
	protected synchronized void stop() {
		// … signal server thread to stop running …
		// … shutdown threadpool …
		isRunning = false;
		threadPool.shutdown();
	}

	/**
	 * Ovaj razred služi za implementaciju serverske dretve i pokretanje.
	 * 
	 * @author Jure Šiljeg
	 *
	 */
	protected class ServerThread extends Thread {
		/**
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			try {
				@SuppressWarnings("resource")
				ServerSocket serverSocket = new ServerSocket();
				serverSocket.bind(new InetSocketAddress(address, port));
				while (true) {
					Socket client = serverSocket.accept();
					ClientWorker cw = new ClientWorker(client);
					threadPool.submit(cw);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Mapa za praćenje ulazaka u sesiju i osvježavanje istih.
	 * 
	 * @author Jure Šiljeg
	 *
	 */
	private static class SessionMapEntry {
		/** Serijski broj sesije. */
		@SuppressWarnings("unused")
		String sid;
		/** Vremensko ograničenje validnosti sesije. */
		long validUntil;
		@SuppressWarnings("javadoc")
		Map<String, String> map = new HashMap<String, String>();

		/**
		 * Konstructor za sesiju.
		 * 
		 * @param sid
		 *            ulazna sesija.
		 * @param validUntil
		 *            ulas o validnom vremenus.
		 * @param map
		 *            mapa parametara.
		 */
		public SessionMapEntry(String sid, long validUntil, Map<String, String> map) {
			super();
			if (sid == null) {
				throw new IllegalArgumentException("SID ne smije biti null!");
			}

			this.sid = sid;
			this.validUntil = validUntil;
			this.map = map;
		}
	}

	/**
	 * Client worker implementacija za naš server.. Client workeri se dodaju u
	 * bazen s dretvama.
	 * 
	 * @author Jure Šiljeg
	 *
	 */
	private class ClientWorker implements Runnable {
		/** Klijentska utičnica. */
		private Socket csocket;
		/** Input stream. */
		private PushbackInputStream istream;
		/** Output stream. */
		private OutputStream ostream;
		/** Verzija klijenta kao radnika. */
		private String version;
		/** Metoda za klijenta radnika. */
		private String method;
		/** parametri */
		private Map<String, String> params = new HashMap<String, String>();
		/** Trajni parametri. */
		private Map<String, String> permPrams = new HashMap<>();
		/** Lista kolačića. */
		private List<RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
		/** Verzija sesije. */
		@SuppressWarnings("unused")
		private String SID;
		/** Ulazna mapa za sesiju. */
		private SessionMapEntry sessionMapEntry;

		/**
		 * Konstruktor za klijenta-radnika. Prima zadanu utičnicu i
		 * inijalizacijuvrši.
		 * 
		 * @param csocket
		 *            zadana utičnica za komunikaciju.
		 */
		public ClientWorker(Socket csocket) {
			super();
			this.csocket = csocket;
		}

		/**
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			try {
				// obtain input stream from socket and wrap it to pushback input
				// stream
				istream = new PushbackInputStream(csocket.getInputStream());
				// obtain output stream from socket
				ostream = csocket.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Then read complete request header from your client in separate
			// method...
			byte[] request = null;

			request = readRequest();

			if (request == null) {
				sendError(ostream, 400, "Bad request");
				return;
			}
			String requestStr = new String(request, StandardCharsets.US_ASCII);

			// If header is invalid (less then a line at least) return response
			// status 400
			List<String> headers = extractHeaders(requestStr);
			// npr. firstLine = "GET /abc/def?name=joe&country=usa HTTP/1.1"
			String[] firstLine = headers.isEmpty() ? null : headers.get(0).split(" ");
			if (firstLine == null || firstLine.length != 3) {
				sendError(ostream, 400, "Bad request");
				return;
			}

			// Extract (method, requestedPath, version) from firstLine
			method = firstLine[0].toUpperCase();
			// if method not GET or version not HTTP/1.0 or HTTP/1.1 return
			// response status 400
			if (!method.equals("GET")) {
				sendError(ostream, 400, "Method Not Allowed");
				return;
			}
			version = firstLine[2].toUpperCase();
			if (!version.equals("HTTP/1.1") && !version.equals("HTTP/1.0")) {
				sendError(ostream, 400, "HTTP Version Not Supported");
				return;
			}

			String path;
			String paramString;
			// (path, paramString) = split requestedPath to path and
			// parameterString
			String requestedPath = firstLine[1];
			String[] pathSplit = requestedPath.split("\\?"); // !!!
			path = pathSplit[0];
			if (pathSplit.length == 2) {
				paramString = pathSplit[1];
			} else {
				paramString = "";
				// možda error bacit
			}

			// Now, when you process clients request, before doing anything else
			// (before calling parseParameters) call
			// the method checkSession with a list of header lines.
			checkSession(headers);

			// parseParameters(paramString); ==> your method to fill map
			// parameters
			if (!paramString.equals("")) {
				parseParameters(paramString);
			}

			// requestedPath = resolve path with respect to documentRoot
			// if requestedPath is not below documentRoot, return response
			// status 403 forbidden
			path = resolvePath(path);

			// check if requestedPath exists, is file and is readable;
			// if not, return status 404 else extract file extension
			requestedPath = path + "?" + paramString;
			Path reqPath = Paths.get(path);
			RequestContext rc = new RequestContext(ostream, params, permPrams, outputCookies);

			// modify the way clients requests are processed so that you first
			// check if the request is of form
			// /ext/ XXX if it is, as JVM to load that class, create an instance
			// of it, cast it to IWebWorker and use it
			// to process the request. Otherwise process as before.
			if (pathSplit[0].startsWith("/ext")) {
				IWebWorker iww = getInstanceOfWorker((pathSplit[0].replace("/ext/", "")));
				iww.processRequest(rc);
				closeConnection();
				return;
			}

			// After the code that parses parameters and just before you check
			// the extension in
			// requested URL insert a code that checks if the requested path is
			// mapped to some IWebWorker (consult
			// workersMap). If it is, call that worker's processRequest and you
			// are done; if it is not, proceed as usual.
			if (workersMap.get(pathSplit[0]) != null) {
				workersMap.get(pathSplit[0]).processRequest(rc);
				closeConnection();
				return;
			}

			String fileExtension = "";
			if (!(Files.exists(reqPath) && reqPath.toFile().isFile() && reqPath.toFile().canRead())) {
				sendError(ostream, 404, "Invalid file path.");
			} else {
				int i = path.lastIndexOf('.');
				if (i > 0) {// barem malo desno
					fileExtension = path.substring(i + 1);
				}
				// možda else error
			}

			// find in mimeTypes map appropriate mimeType for current file
			// extension
			// (you filled that map during the construction of SmartHttpServer
			// from mime.properties)
			// if no mime type found, assume application/octet-stream
			String mimeType = mimeTypes.get(fileExtension);
			// create a rc = new RequestContext(...); set mime-type; set status
			// to 200
			rc.setMimeType(mimeType);
			rc.setStatusCode(200);
			rc.setStatusText("OK");
			// If you want, you can modify RequestContext to allow you to add
			// additional headers
			// so that you can add “Content-Length: 12345” if you know that file
			// has 12345 bytes
			// open file, read its content and write it to rc
			// (that will generate header and send file bytes to client)
			byte[] data;
			try {
				data = Files.readAllBytes(Paths.get(path));
				if (fileExtension.equals("smscr")) {
					String documentBody = new String(data, StandardCharsets.UTF_8);
					new SmartScriptEngine(new SmartScriptParser(documentBody).getDocumentNode(), rc).execute();
				} else {
					rc.write(data);
				}
			} catch (IOException e) {
				System.out.println("Nije moguće učitati datoteku u buffer!");
				System.exit(-1);
			} finally {
				closeConnection();
			}
		}

		/**
		 * Metoda za provjeru sesije. Metoda radi to da ide kroz sve linije
		 * zaglavlja i ignorira one koje ne počinju s Cookie: , potom promatra
		 * pronađene kolačiće i radi određene akcije. got
		 * 
		 * @param headers
		 *            lista linija zaglavlja.
		 */
		private synchronized void checkSession(List<String> headers) {
			String sidCandidate = null;
			SessionMapEntry sessionMapEntry;

			for (String headerLine : headers) {
				if (headerLine.startsWith("Cookie:")) {
					if (headerLine.contains("sid=")) {
						String cookieString = headerLine.replace("Cookie: ", "");
						String[] cookies = cookieString.split(";");
						for (String cookie : cookies) {
							if (cookie.startsWith("sid")) {
								sidCandidate = cookie.split("sid")[1].replace("\"", "");
								sidCandidate = sidCandidate.replace("=", "");
							}
						}
					}
				}
			}

			if (sidCandidate == null) {
				// stvara se new SessionMapEntry
				sidCandidate = generateSid();
				long validTime = System.currentTimeMillis() / 1000 + sessionTimeout;
				sessionMapEntry = new SessionMapEntry(sidCandidate, validTime, permPrams);
				sessions.put(sidCandidate, sessionMapEntry);
				outputCookies.add(new RCCookie("sid", sidCandidate, null, address, "/"));
			} else {
				sessionMapEntry = sessions.get(sidCandidate);
				if (sessionMapEntry == null) {
					// Stvara se new SessionMapEntry
					sidCandidate = generateSid();
					long validTime = System.currentTimeMillis() / 1000 + sessionTimeout;
					sessionMapEntry = new SessionMapEntry(sidCandidate, validTime, permPrams);
					sessions.put(sidCandidate, sessionMapEntry);
					outputCookies.add(new RCCookie("sid", sidCandidate, null, address, "/"));
				} else if ((System.currentTimeMillis() / 10000) > sessionMapEntry.validUntil) {

					sessions.remove(sidCandidate);
					sidCandidate = generateSid();
					long validTime = System.currentTimeMillis() / 1000 + sessionTimeout;
					sessionMapEntry = new SessionMapEntry(sidCandidate, validTime, permPrams);
					sessions.put(sidCandidate, sessionMapEntry);
					outputCookies.add(new RCCookie("sid", sidCandidate, null, address, "/"));
				} else {

					sessionMapEntry.validUntil = System.currentTimeMillis() / 1000;
					sessions.put(sidCandidate, sessionMapEntry);
				}
				SID = sidCandidate;
				permPrams = sessionMapEntry.map;
			}
		}

		/**
		 * Generira se string velikih slova na slučajan način s odgovarajućom
		 * duljinom 20.
		 * 
		 * @return taj string.
		 */
		private String generateSid() {
			// ascii vrijednosti su od 65 do 90 za velika slova
			String rand = "";
			for (int i = 0; i < 20; i += 1) {
				rand += String.valueOf(sessionRandom.nextInt(25) + 65);
			}
			return rand;
		}

		/**
		 * Uzima iz argumenta ime razreda i vraća ga kao instancu sučelja
		 * {@link IWebWorker}.
		 * 
		 * @param worker
		 *            string iz kojeg treba ekstrahirati ime i stvoriti
		 *            odgovarajuću instancu.
		 * @return instancu {@link IWebWorker}.
		 */
		private IWebWorker getInstanceOfWorker(String worker) {
			try {
				Class<?> referenceToClass = this.getClass().getClassLoader()
						.loadClass("hr.fer.zemris.java.webserver.workers." + worker);
				Object newObject = referenceToClass.newInstance();
				IWebWorker iww = (IWebWorker) newObject;
				return iww;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * Metoda koja služi za prekid konekcije i zatvaranje izlaznog strema i
		 * utičnice koju smo koristili.
		 */
		private void closeConnection() {
			try {
				ostream.flush();
				ostream.close();
				csocket.close();
			} catch (IOException e) {
				System.out.println("Neuspjelo zatvaranje veze!");
				System.exit(-1);
			}
		}

		/**
		 * Razrješuje ulaznu stazu za korijenski dokument. Sve se radi po
		 * algoritmu: Ako staza sadrži korijen dokumenta, mora se nalaziti na
		 * početku staze. Inače, samo trebamo dodati korijenski dokument na
		 * zadanu stazu.
		 * 
		 * @param path
		 *            Ulazna staza.
		 * @return Razriješena i upotrebljiva staza.
		 */
		private String resolvePath(String path) {
			String docRoot = documentRoot.toString();
			if (path.contains(docRoot)) {
				if (!path.startsWith(docRoot)) {
					sendError(ostream, 403, "Forbidden");
				}
				return path;
			} else {
				return docRoot + path;
			}
		}

		/**
		 * Metoda koja puni mapu parametara iz zadanog stringa koji ih sadrži.
		 * Npr. firstLine = "GET /abc/def?name=joe&country=usa HTTP/1.1" =>
		 * params.put("name", "joe"); params.put("country", "usa");
		 * 
		 * @param paramString
		 *            linija koju treba parsirati i napuniti mapu parametara.
		 */
		private void parseParameters(String paramString) {

			String[] paramSplit = paramString.split("\\&");
			for (String param : paramSplit) {
				String[] p = param.split("\\=");
				params.put(p[0], p[1]);
			}

		}

		/**
		 * Ekstrahira se zaglavlje stringa koji reprezentira zahtjev zaglavlja.
		 * 
		 * @param requestStr
		 *            ulazni zahtjev zaglavlja.
		 * @return listu parametara iz zaglavlja.
		 */
		private List<String> extractHeaders(String requestStr) {
			List<String> headers = new ArrayList<>();

			String currentLine = null;
			for (String s : requestStr.split("\n")) {
				if (s.isEmpty())
					break;

				char c = s.charAt(0);
				if (c == 9 || c == 32) { // tab or space
					currentLine += s;
				} else {
					if (currentLine != null) {
						headers.add(currentLine);
					}
					currentLine = s;
				}
			}

			if (!currentLine.isEmpty()) {
				headers.add(currentLine);
			}

			return headers;
		}

		/**
		 * Pomoćna metoda za slanje poruka o greškama sa zadanim statusom. Sve
		 * se šalje klijentu.
		 * 
		 * @param cos
		 *            Output stream za klijenta.
		 * @param statusCode
		 *            Status kod pogreške.
		 * @param statusText
		 *            Statusni tekst pogreške.
		 */
		private void sendError(OutputStream cos, int statusCode, String statusText) {
			try {
				cos.write(("HTTP/1.1 " + statusCode + " " + statusText + "\r\n" + "Server: simple java server\r\n"
						+ "Content-Type: text/plain;charset=UTF-8\r\n" + "Content-Length: 0\r\n"
						+ "Connection: close\r\n" + "\r\n").getBytes(StandardCharsets.US_ASCII));
				cos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Metoda koja čita klijentov zahtjev. Pretvara input strema podatke u
		 * listu stringova. Implementirano je automatom.
		 * 
		 * @return Request header listu stringova razdvojenih prelaskom u novi
		 *         red.
		 */
		private byte[] readRequest() {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int state = 0;
			l: while (true) {
				int b = 0;
				try {
					b = istream.read();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (b == -1)
					return null;
				if (b != 13) {
					bos.write(b);
				}
				switch (state) {
				case 0:
					if (b == 13) {
						state = 1;
					} else if (b == 10)
						state = 4;
					break;
				case 1:
					if (b == 10) {
						state = 2;
					} else
						state = 0;
					break;
				case 2:
					if (b == 13) {
						state = 3;
					} else
						state = 0;
					break;
				case 3:
					if (b == 10) {
						break l;
					} else
						state = 0;
					break;
				case 4:
					if (b == 10) {
						break l;
					} else
						state = 0;
					break;
				}
			}
			return bos.toByteArray();
		}
	}

	/**
	 * Glavna metoda za puštanje servera u pogon.
	 * 
	 * @param args
	 *            argumenti komandne linije koje ne koristimo. Implementirali
	 *            smo fiksnu konfiguracijsku datoteku koja se pokreće.
	 * @throws IOException
	 *             u slučaju pogreške u radu.
	 */
	public static void main(String[] args) throws IOException {
		final SmartHttpServer server = new SmartHttpServer(MAIN_SERVER_CONFIG_FILE);
		server.start();
	}

}
