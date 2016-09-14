package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.support.CommandsSupport;
import hr.fer.zemris.java.tecaj.hw07.shell.environments.Environment;

/**
 * Razred koji brine o funkcionalnosti komande hexdump.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class HexdumpCommand extends CommandsSupport implements ShellCommand {

	/** Označava širinu prozora, odnosno koliko će hex znamenki sadržavati. */
	private static final int WIDTH = 16;
	/** String koji će nam pomoći u formatiranju prvih 8 stupaca tablice. */
	private static final String PADDING = "00000000";

	/**
	 * Defaultni konstruktor koji služi za punjenje opisa komande.
	 */
	public HexdumpCommand() {
		createDescriptions();
	}

	/**
	 * Metoda koja služi za kreiranje opisa komande.
	 */
	private void createDescriptions() {
		List<String> tmp = new ArrayList<>();
		tmp.add("UPUTE ZA KORIŠTENJE: ");
		tmp.add("1. Očekuje se jedan argument - ime datoteke zadano preko staze.");
		tmp.add("PRIMJER POZIVA:");
		tmp.add("hexdump C:\\Users\\Jure\\Desktop\\provjera.txt");
		tmp.add("PRIMJER ISPISA:");
		tmp.add("1. Očekujemo ispis retka i hex-izlaza. Npr.:");
		tmp.add("00000000: 4e 65 20 62 61 9a 20 74 | 61 6b 6f 20 74 65 9a 6b | Ne ba. tako te.k");
		tmp.add("00000001: 61 20 7a 61 64 61 e6 61 | 20 69 7a 20 4f 4f 50 4a | a zada.a iz OOPJ");
		tmp.add("00000002: 4a 2e 20 4e 6f 2c 20 6e | 65 6d 6f 6a 6d 6f 20 69 | J. No, nemojmo i");
		tmp.add("00000003: 7a 61 7a 69 76 61 74 69 | 20 73 72 65 e6 75 21 21 | zazivati sre.u!!");
		setDescriptions(tmp);
	}

	@SuppressWarnings("javadoc")
	@Override
	public String getCommandName() {
		return "hexdump";
	}

	@SuppressWarnings("javadoc")
	@Override
	public List<String> getCommandDescription() {
		return getDescriptions();
	}

	/**
	 * {@inheritDoc} Komanda pruža hex-izlaz. Što bi bio hex-ispis? Npr. ovo:
	 * 
	 * <table>
	 * <tbody>
	 * <tr>
	 * <td align = "right">4e 65 20 62 61 9a 20 74</td>
	 * <td>|</td>
	 * <td align= "left">61 6b 6f 20 74 65 9a 6b | Ne ba. tako te.k</td>
	 * </tr>
	 * <tr>
	 * <td align = "right">7a 61 7a 69 76 61 74 69</td>
	 * <td>|</td>
	 * <td align= "left">20 73 72 65 e6 75 21 21 | zazivati sre.u!!</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * 
	 * U samom ispisu se još može očekivati i offset (broj linije).
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) throws IOException {
		String[] field = verifyAndParseArguments(env, arguments);
		if (field[1] != null) {
			env.writeln("Nedozvoljen broj argumenata!");
			throw new IllegalArgumentException();
		}
		fileCheck(env, field[0].trim());
		byte[] byteField = getBytes(env, field[0].trim());
		int counter = 0;
		for (int start = 0; start < byteField.length; start += WIDTH) {
			printOffsets(env, counter++);
			printHex(env, byteField, start, WIDTH);
			printAscii(env, byteField, start, WIDTH);
		}
		return ShellStatus.CONTINUE;
	}

	/**
	 * Metoda zadužena za ispis uvodnih stupaca u svakom retku ispisa. Sadrži
	 * samo redne brojeve linija.
	 * 
	 * @param env
	 *            okruženje u kojem ljuska radi.
	 * @param i
	 *            broj linije koji želimo ispisati kroz 8 mjesta.
	 * @throws IOException
	 *             u slučaju nedozvoljenih situacija u skladu s propozicijama
	 *             zadatka.
	 */
	private void printOffsets(Environment env, int i) throws IOException {
		String out = String.valueOf(i);
		env.write(PADDING.substring(0, PADDING.length() - out.length()) + out + ": ");
	}

	/**
	 * Metoda zadužena za ispis ascii vrijednosti bajta.
	 * 
	 * @param env
	 *            okruženje u kojem ljuska radi.
	 * @param byteField
	 *            polje bajtova koje treba prikladno ispisati.
	 * @param start
	 *            početni indeks.
	 * @param width
	 *            broj ispisa koji trebamo zadovoljiti po linii.
	 * @throws IOException
	 *             u slučaju nedozvoljenih situacija u skladu s propozicijama
	 *             zadatka.
	 */
	private void printAscii(Environment env, byte[] byteField, int start, int width) throws IOException {
		if (start < byteField.length) {
			width = Math.min(width, byteField.length - start);
			String out = new String(byteField, start, width, "UTF-8");
			StringBuilder sb = new StringBuilder();
			for (char c : out.toCharArray()) {
				if ((int) c < 32 || (int) c > 127) {
					sb.append('.');
				} else {
					sb.append(c);
				}
			}
			env.writeln("| " + sb);
		}
	}

	/**
	 * Metoda zadužena za ispis hex vrijednosti bajta.
	 * 
	 * @param env
	 *            okruženje u kojem ljuska radi.
	 * @param byteField
	 *            polje bajtova koje treba prikladno ispisati.
	 * @param start
	 *            početni indeks.
	 * @param width
	 *            broj ispisa koji trebamo zadovoljiti po linii.
	 * @throws IOException
	 *             u slučaju nedozvoljenih situacija u skladu s propozicijama
	 *             zadatka.
	 */
	private void printHex(Environment env, byte[] byteField, int start, int width) throws IOException {
		int counter = 0;
		for (int index = 0; index < width; index++) {
			++counter;
			if (index + start < byteField.length) {
				// za dobro formatiranje jednoznamenkastih hexeva
				if ((byteField[index + start] & 0xFF) < 16) {
					env.write(" ");
				}
				env.write(Integer.toHexString(byteField[index + start] & 0xFF));
				env.write(" ");
			} else {
				env.write("   "); // za ispis praznih mjesta kad nema više slova
			}
			if (counter == WIDTH / 2) {
				env.write("| ");
			}
		}

	}

	/**
	 * Metoda zadužena za dohvat bajtovnih reprezentanata znakova iz datoteke.
	 * 
	 * @param env
	 *            okruženje ljuske.
	 * 
	 * @param fileName
	 *            ime datoteke iz koje čupamo podatke.
	 * @return pole jbajtova koje odgovara prikazu znakova iz datoteke.
	 * @throws FileNotFoundException
	 *             u slučaju nemogućnosti psonalaska filea.
	 * @throws IOException
	 *             u slučaju nedozvoljenih situacija u skladu s propozicijama
	 *             zadatka (problemi kod ispisa).
	 */
	public byte[] getBytes(Environment env, String fileName) throws FileNotFoundException, IOException {
		byte[] buffer = new byte[1024];
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(fileName));
		} catch (FileNotFoundException e) {
			env.writeln("Ne postoji zadana datoteka");
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int bytes = 0;
		while ((bytes = bis.read(buffer, 0, buffer.length)) > 0) {
			baos.write(buffer, 0, bytes);
		}
		byte[] ba = baos.toByteArray();
		baos.close();
		bis.close();
		return ba;
	}

}
