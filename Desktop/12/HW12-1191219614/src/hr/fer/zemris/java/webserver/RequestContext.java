package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Razred koji služi za čuvanje podatak o kontekstu zahtjeva.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 *
 */
public class RequestContext {
	/** Stream zadužen za ispisivanje. */
	private OutputStream outputStream;
	/** Zadani charset koji je vezan uz encoding. */
	private Charset charset;

	/** Kodna stranica koja je referentna za ispis. */
	public String encoding;
	/**
	 * Status kod koji označava prirodu outputa. Npr. 400+ za poruke o greškama,
	 * 200+ ako je sve prošlo OK i sl.
	 */
	public int statusCode;
	/** Tekstualna reprezentacija statusa. */
	public String statusText;
	/**
	 * Oblik prikazivanja koji ćemo oponašati (može biti html, običan tekst,
	 * slika i sl.)
	 */
	public String mimeType;

	/** Parametri. */
	private Map<String, String> parameters;
	/** Privremeni parametri. */
	private Map<String, String> temporaryParameters;
	/** Trajni parametri. */
	private Map<String, String> persistentParameters;
	/**
	 * Kolačići. Podaci koje server može vratiti web pregledniku i koje će
	 * preglednik zapamtiti i dodati svakom zahtjevu kojeg napravi prema
	 * serveru.
	 */
	private List<RCCookie> outputCookies;
	/** Varijabla koja govori je li zaglavlje generirano već ili nije. */
	private boolean headerGenerated;
	/** Skup ključeva iz mape parameters. */
	private Set<String> parameterNames;
	/** Skup ključeva iz mape persistantParameters. */
	@SuppressWarnings("unused")
	private Set<String> persistentParameterNames;
	/** Skup ključeva iz mape temporaryParameters. */
	private Set<String> temporaryParameterNames;

	/**
	 * Konstruktor za inicijalizaciju instance ovog razreda.
	 * 
	 * @param outputStream
	 *            Stream zadužen za ispisivanje.
	 * @param parameters
	 *            Parametri.
	 * @param persistentParameters
	 *            trajni parametri.
	 * @param outputCookies
	 *            Kolačići. Podaci koje server može vratiti web pregledniku i
	 *            koje će preglednik zapamtiti i dodati svakom zahtjevu kojeg
	 *            napravi prema serveru.
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistentParameters, List<RCCookie> outputCookies) {

		encoding = "UTF-8";
		charset = Charset.forName(encoding);
		statusCode = 200;
		statusText = "OK";
		mimeType = "text/html";
		headerGenerated = false;

		temporaryParameterNames = new HashSet<>();
		temporaryParameters = new HashMap<>();
		this.outputStream = outputStream;

		Objects.requireNonNull(outputStream, "outputStream ne smije biti null!");
		if (parameters == null) {
			this.parameters = new HashMap<>();
			this.parameterNames = new HashSet<>();
		} else {
			this.parameters = parameters;
			this.parameterNames = parameters.keySet();
		}

		if (persistentParameters == null) {
			this.persistentParameters = new HashMap<>();
			this.persistentParameterNames = new HashSet<>();
		} else {
			this.persistentParameters = persistentParameters;
			this.persistentParameterNames = persistentParameters.keySet();
		}

		if (outputCookies == null) {
			this.outputCookies = new ArrayList<>();
		} else {
			this.outputCookies = outputCookies;
		}
	}

	/**
	 * Dodaje kolačić u internu listu kolačića.
	 * 
	 * @param rcCookie
	 *            kolačić koji se dodaje.
	 */
	public void addRCCookie(RCCookie rcCookie) {
		outputCookies.add(rcCookie);
	}

	/**
	 * Metoda koja streama bajtove obzirom na zadani stream.
	 * 
	 * @param data
	 *            podaci koji se trebaju prikazati negdje.
	 * @return kontekst nad kojim je pozvana metoda (vlastiti).
	 * @throws IOException
	 *             u slučaju nemogućnosti pisanja.
	 */
	public RequestContext write(byte[] data) throws IOException {
		if (!headerGenerated) {
			generateHeader();
		}
		outputStream.write(data);
		return this;

	}

	/**
	 * Generira header u slučaju da nije već generiran. Sve je u skladu s
	 * propozicijama iz prvog zadatka.
	 * 
	 * @throws IOException
	 *             u slučaju nemogućnosti pisanja na stream.
	 */
	private void generateHeader() throws IOException {
		headerGenerated = true;
		String header = "";
		header += "HTTP/1.1" + " " + statusCode + " " + statusText + "\r\n";

		header += "Content-Type:" + " " + mimeType;
		if (mimeType.startsWith("text/")) {
			header += "; charset=" + encoding;
		}
		header += "\r\n";

		for (RCCookie cookie : outputCookies) {
			header += "Set-Cookie:" + " " + cookie.getName() + "=\"" + cookie.getValue() + "\"";
			if (cookie.getDomain() != null) {
				header += "; Domain=" + cookie.getDomain();
			}
			if (cookie.getPath() != null) {
				header += "; Path=" + cookie.getPath();
			}
			if (cookie.getMaxAge() != null) {
				header += "; Max-Age=" + cookie.getMaxAge();
			}
			header += "\r\n";
		}
		// zadnja empty line
		header += "\r\n";

		outputStream.write(header.getBytes(StandardCharsets.ISO_8859_1));

	}

	/**
	 * Radi isto što i write metoda koja prima polje batova. Posao je delegiran
	 * njoj.
	 * 
	 * @param text
	 *            tekst koji se treba zapisati.
	 * @return kontekst nad kojim je pozvana metoda (vlastiti).
	 * @throws IOException
	 *             u slučaju nemogućnosti pisanja.
	 */
	public RequestContext write(String text) throws IOException {
		this.write(text.getBytes(charset));
		return this;
	}

	/**
	 * Getter za vrijednost iz mape parameters.
	 * 
	 * @param name
	 *            zadano ime uz koje vraćamo vrijednost iz mape parameters.
	 * @return tu vrijednost.
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}

	/**
	 * Getter za Skup ključeva iz mape parameters.
	 * 
	 * @return tražene ključeve.
	 */
	public Set<String> getParameterNames() {
		return parameterNames;
	}

	/**
	 * Getter za vrijednost u persistantParameters.
	 * 
	 * @param name
	 *            zadano ime uz koje vraćamo vrijednost iz mape
	 *            persistantParameters.
	 * @return tu vrijednost.
	 */
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}

	/**
	 * Setter za mapu persistantParameters. Postavlja u tu mapu na mjesto
	 * određeno parametrom name, vrijednost određenu parametrom value.
	 * 
	 * @param name
	 *            ključ po kojem osvježavamo zadanu mapu.
	 * @param value
	 *            vrijednost na koju osvježavamo zadanu mapu.
	 */
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(name, value);
	}

	/**
	 * Uklanja se iz mape persistantParameters element. Uklanja se tako da se iz
	 * te mape na mjestu određeno parametrom name uklanja čitav zapis uređenog
	 * para (name, value).
	 * 
	 * @param name
	 *            ključ koji koristimo za brisanje elementa iz zadane mape.
	 *
	 */
	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}

	/**
	 * Getter za vrijednost u temporaryParameters.
	 * 
	 * @param name
	 *            zadano ime uz koje vraćamo vrijednost iz mape
	 *            temporaryParameters.
	 * @return tu vrijednost.
	 */
	public String getTemporaryParameter(String name) {
		return temporaryParameters.get(name);
	}

	/**
	 * Getter za Skup ključeva iz mape temporaryParameters.
	 * 
	 * @return tražene ključeve.
	 */
	public Set<String> getTemporaryParameterNames() {
		return temporaryParameterNames;
	}

	/**
	 * Setter za mapu temporaryParameters. Postavlja u tu mapu na mjesto
	 * određeno parametrom name, vrijednost određenu parametrom value.
	 * 
	 * @param name
	 *            ključ po kojem osvježavamo zadanu mapu.
	 * @param value
	 *            vrijednost na koju osvježavamo zadanu mapu.
	 */
	public void setTemporaryParameter(String name, String value) {
		temporaryParameters.put(name, value);
	}

	/**
	 * Uklanja se iz mape temporaryParameters element. Uklanja se tako da se iz
	 * te mape na mjestu određeno parametrom name uklanja čitav zapis uređenog
	 * para (name, value).
	 * 
	 * @param name
	 *            ključ koji koristimo za brisanje elementa iz zadane mape.
	 *
	 */
	public void removeTemporaryParameter(String name) {
		temporaryParameters.remove(name);
	}

	/**
	 * Getter za mapu privremenih parametara.
	 * 
	 * @return tu mapu.
	 */
	public Map<String, String> getTemporaryParameters() {
		return temporaryParameters;
	}

	/**
	 * Postavljaju se privremeni parametri.
	 * 
	 * @param temporaryParameters
	 *            mapa kojom se inicijalizira odgovarajuća privatna članska
	 *            varijabla.
	 */
	public void setTemporaryParameters(Map<String, String> temporaryParameters) {
		this.temporaryParameters = temporaryParameters;
	}

	/**
	 * Getter za mapu trajnih parametara.
	 * 
	 * @return tu mapu.
	 */
	public Map<String, String> getPersistentParameters() {
		return persistentParameters;
	}

	/**
	 * Postavljaju se trajni parametri.
	 * 
	 * @param persistentParameters
	 *            mapa kojom se inicijalizira odgovarajuća privatna članska
	 *            varijabla.
	 */
	public void setPersistentParameters(Map<String, String> persistentParameters) {
		this.persistentParameters = persistentParameters;
	}

	/**
	 * Getter za mapu parametara.
	 * 
	 * @return tu mapu.
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}

	/**
	 * Postavlja se zadani enkoding, ali samo jednom. Ako je header već
	 * generiran, baca se RuntimeException.
	 * 
	 * @param encoding
	 *            zadani enkoding za stranicu koji trebamo postaviti.
	 */
	public void setEncoding(String encoding) {
		if (headerGenerated) {
			throw new RuntimeException("Ne možete mijenjati kodnu stranicu ako je header prethodno već generiran!");
		}
		this.encoding = encoding;
		charset = Charset.forName(encoding);
	}

	/**
	 * Postavlja se statusni kod. Sam statusni kod označava prirodu outputa.
	 * Npr. 400+ za poruke o greškama, 200+ ako je sve prošlo OK i sl.
	 * 
	 * @param statusCode
	 *            statusni kod koji želimo postaviti u kontekst zahtjeva.
	 */
	public void setStatusCode(int statusCode) {
		if (headerGenerated) {
			throw new RuntimeException("Ne možete mijenjati status koda ako je header prethodno već generiran!");
		}
		this.statusCode = statusCode;
	}

	/**
	 * Setter. Postavlja se tekstualna reprezentacija statusa.
	 * 
	 * @param statusText
	 *            ta tekstualna reprezentacija.
	 */
	public void setStatusText(String statusText) {
		if (headerGenerated) {
			throw new RuntimeException("Ne možete mijenjati status teksta ako je header prethodno već generiran!");
		}
		this.statusText = statusText;
	}

	/**
	 * Postavlja se oblik prikazivanja koji ćemo oponašati (može biti html,
	 * običan tekst, slika i sl.)
	 * 
	 * @param mimeType
	 *            zadani oblik.
	 */
	public void setMimeType(String mimeType) {
		if (headerGenerated) {
			throw new RuntimeException("Ne možete mijenjati tip prikaza ako je header prethodno već generiran!");
		}
		this.mimeType = mimeType;
	}

	/**
	 * Razred koji čuva objekt koji reprezentira kolačić kakav se i inače
	 * koristi u web preglednicima.
	 * 
	 * @author Jure Šiljeg
	 *
	 */
	public static class RCCookie {
		/** Njegovo ime. */
		private String name;
		/** Vrijednost kolačića. */
		private String value;
		/** Domena odgovarajuća iz URL-a. */
		private String domain;
		/** Staza. */
		private String path;
		/** Maksimalna starost. */
		private Integer maxAge;

		/**
		 * Konstruktor za inicijalizaciju priatnih članskih varijabli objekta
		 * koji reprezentira kolačić.
		 * 
		 * @param name
		 *            Njegovo ime.
		 * @param value
		 *            Vrijednost kolačića.
		 * @param maxAge
		 *            Maksimalna starost.
		 * @param domain
		 *            Domena odgovarajuća iz URL-a.
		 * @param path
		 *            Staza.
		 */
		public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
			super();
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
		}

		/**
		 * Getter za ime kolačića.
		 * 
		 * @return to ime.
		 */
		public String getName() {
			return name;
		}

		/**
		 * Getter za vrijednost koju kolaćić ima.
		 * 
		 * @return tu vrijednost.
		 */
		public String getValue() {
			return value;
		}

		/**
		 * Getter za domenu koju kolačić koristi.
		 * 
		 * @return string reprezentaciju te domene.
		 */
		public String getDomain() {
			return domain;
		}

		/**
		 * Getter za odgovarajuću stazu u kolačiću.
		 * 
		 * @return tu stazu.
		 */
		public String getPath() {
			return path;
		}

		/**
		 * Getter za maksimalnu starost.
		 * 
		 * @return tu starost kolačića.
		 */
		public Integer getMaxAge() {
			return maxAge;
		}

	}

}
