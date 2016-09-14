package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.support.CommandsSupport;
import hr.fer.zemris.java.tecaj.hw07.shell.environments.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.environments.MyEnvironment;

/**
 * Razred koji brine o funkcionalnosti komande copy.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class CopyCommand extends CommandsSupport implements ShellCommand {
	/**
	 * Defaultni konstruktor koji služi za punjenje opisa komande.
	 */
	public CopyCommand() {
		createDescriptions();
	}

	/**
	 * Metoda koja služi za kreiranje opisa komande.
	 */
	private void createDescriptions() {
		List<String> tmp = new ArrayList<>();
		tmp.add("UPUTE ZA KORIŠTENJE: ");
		tmp.add("1. Očekuju se dva argumenta.");
		tmp.add("2. Prvi argument mora biti validna staza do neke datoteke koja će biti naš source - datoteka čiji sadržaj kopiramo.");
		tmp.add("3. Drugi argument mora biti validna staza do neke datoteke koja će biti naš destination - datoteka koja nastaje kopiranjem source datoteke.");
		tmp.add("4. Ako destination datoteka postoji, pita se korisnika želi li je prebrisati (opcije su: DA/NE).");
		tmp.add("5. Komanda mora raditi samo sa datotekama (ne direktorijima!).");
		tmp.add("6. Ako je drugi argument direktorij, pretpostavlja se da želite obaviti kopiranje sadržaja i imena datoteke u taj direktorij.");
		tmp.add("PRIMJER POZIVA:");
		tmp.add("a) copy C:\\Users\\Jure\\Desktop\\provjera.txt  C:\\Users\\Jure\\Files");
		tmp.add("b) copy C:\\Users\\Jure\\Desktop\\provjera.txt  C:\\Users\\Jure\\Desktop\\provjera_novi.txt");
		tmp.add("PRIMJER ISPISA:");
		tmp.add("a) U ovom slučaju se kopira datoteka provjera.txt u direktorij Files pod istim imenom i istog sadržaja.");
		tmp.add("b) U ovom slučaju se kopira sadržaj datoteke provjera.txt u isti direktorij, samo u datoteku provjera_novi.txt");
		setDescriptions(tmp);

	}

	/**
	 * {@inheritDoc} Dodatno, Obavlja kopirane datoteka samo (nedirektoria).
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) throws IOException {

		String[] field = verifyAndParseArguments(env, arguments);
		if (field[1] == null) {
			env.writeln("Nedozvoljen broj argumenata!");
			throw new IllegalArgumentException();
		} else {
			File srcFile = new File(field[0].trim());
			File destFile = new File(field[1].trim());
			if (destFile.exists()) {
				env.writeln("Je li dozvoljeno prebrisati ovu datoteku? (DA/NE)");
				((MyEnvironment) env).write(env.getPromptSymbol() + " ");
				String answer = env.readLine();

				if (answer.equals("DA")) {
					if (destFile.isDirectory()) { // destinacija je dir
						Path path = Paths.get(destFile.getAbsolutePath() + "\\" + srcFile.getName());
						try {
							Files.createFile(path);
							destFile = new File(path.toString());
							copyFiles(srcFile, destFile);
							env.writeln("Stvorena je datoteka s kompletnom stazom: " + path.toString());
						} catch (FileAlreadyExistsException e) {
							env.writeln("Već postoji zadana datoteka: " + e.getMessage());
						}
					} else { // destinacija je već file
						copyFiles(srcFile, destFile);
						env.writeln("Uspješno ste kopirali datoteku " + srcFile.getName() + " u istoimenu datoteku!");
					}
				} else if (answer.equals("NE")) {
					env.writeln("Nije dozvoljeno prebrisati ovu datoteku.");
					throw new IllegalArgumentException();
				} else {
					env.writeln("Niste unijeli podržanu riječ. Unesite DA ili NE");
					throw new IllegalArgumentException();
				}

			} else {
				env.writeln("Ne postoji datoteka sa sljedećim imenom: " + destFile.getName());
				throw new IllegalArgumentException();
			}

		}
		return ShellStatus.CONTINUE;
	}

	/**
	 * Metoda koja obavlja "ručno" kopiranje datoteke u datoteku.
	 * 
	 * @param srcFile
	 *            polazna datoteka čiji sadržaj kopiramo.
	 * @param destFile
	 *            odredišna datoteka koja se napuni sadržajem polazne.
	 * @throws IOException
	 *             u slučaju nedozvoljenih situacija u skladu s propozicijama
	 *             zadatka.
	 */
	private void copyFiles(File srcFile, File destFile) throws IOException {
		InputStream input = null;
		OutputStream output = null;
		try {
			input = new FileInputStream(srcFile);
			output = new FileOutputStream(destFile);
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = input.read(buf)) > 0) {
				output.write(buf, 0, bytesRead);
			}
		} finally {
			input.close();
			output.close();
		}
	}

	@SuppressWarnings("javadoc")
	@Override
	public String getCommandName() {
		return "copy";
	}

	@SuppressWarnings("javadoc")
	@Override
	public List<String> getCommandDescription() {
		return getDescriptions();
	}

}
