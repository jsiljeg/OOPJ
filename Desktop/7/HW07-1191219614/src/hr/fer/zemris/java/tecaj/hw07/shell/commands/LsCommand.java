package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.support.CommandsSupport;
import hr.fer.zemris.java.tecaj.hw07.shell.environments.Environment;

/**
 * Razred koji brine o funkcionalnosti komande ls.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class LsCommand extends CommandsSupport implements ShellCommand, FileVisitor<Path> {
	/** Označava format i duljinu dijela ispisa */
	private static final String CONST_SPACE = "          ";// 10 praznih mjesta
	/** Dubina rekurzie prilikom obilaska stabla direktorija. */
	private int level;
	/** Statičko okruženje potrebno za obilazak. */
	private static Environment env;

	/**
	 * Defaultni konstruktor koji služi za punjenje opisa komande.
	 */
	public LsCommand() {
		createDescriptions();
	}

	/**
	 * Metoda koja služi za kreiranje opisa komande.
	 */
	private void createDescriptions() {
		List<String> tmp = new ArrayList<>();
		tmp.add("UPUTE ZA KORIŠTENJE: ");
		tmp.add("1. Očekuje se jedan argument - direktorij.");
		tmp.add("2. Komanda pruža izlistak direktorija (nerekurzivan).");
		tmp.add("3. U prvom stupcu se nalazi d- ako je datoteka direktorij; r - ako je datoteka dostupna za čitanje; w - ako je datoteka dostupna za pisanje; x - ako je datoteka izvršiva.");
		tmp.add("   Za slučaj da nešto od navedenog nije - ispiše se crtica (-).");
		tmp.add("4. U drugom stupcu možete vidjeti veličinu datoteke. ");
		tmp.add("5. U narednom stupcu možete vidjeti datum kreiranja.");
		tmp.add("6. U posljednjem stupcu se vidi ime datoteke.");
		tmp.add("PRIMJER POZIVA:");
		tmp.add("ls C:\\Users\\Jure\\Desktop\\provjera");
		tmp.add("PRIMJER ISPISA:");
		tmp.add("-rwx        64 2016-04-27 04:30:13 tekst.txt");
		tmp.add("-rwx      4750 2016-04-27 03:50:09 testingcat.txt");
		tmp.add("-rwx      4750 2016-04-27 03:51:46 testni_dokument.t.txt");
		setDescriptions(tmp);
	}

	/**
	 * {@inheritDoc} Ispisuje se nerekurzivni izlistak direktorija.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) throws IOException {
		String[] field = verifyAndParseArguments(env, arguments);
		if (field[1] != null) {
			env.writeln("Nedozvoljen broj argumenata!");
			throw new IllegalArgumentException();
		}
		directoryCheck(env, field[0].trim());
		LsCommand.env = env;
		Files.walkFileTree(Paths.get(field[0].trim()), new LsCommand());

		return ShellStatus.CONTINUE;
	}

	@SuppressWarnings("javadoc")
	@Override
	public String getCommandName() {
		return "ls";
	}

	@SuppressWarnings("javadoc")
	@Override
	public List<String> getCommandDescription() {
		return getDescriptions();
	}

	@SuppressWarnings("javadoc")
	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		level--;
		return FileVisitResult.CONTINUE;
	}

	@SuppressWarnings("javadoc")
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		ispisi(dir);
		level++;
		return FileVisitResult.CONTINUE;
	}

	@SuppressWarnings("javadoc")
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		ispisi(file);
		return FileVisitResult.CONTINUE;
	}

	@SuppressWarnings("javadoc")
	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		return FileVisitResult.CONTINUE;
	}

	/**
	 * Metoda koja ispisuje sve što pronađe od datoteka, ali samo na prvoj
	 * razini dubine rekurzije.
	 * 
	 * @param path
	 *            zadana staza do direktorija.
	 * @throws IOException
	 *             u slučaju nedozvoljenih situacija u skladu s propozicijama
	 *             zadatka.
	 */
	private void ispisi(Path path) throws IOException {
		if (level == 1) {
			processFile(path.normalize());
		}
	}

	/**
	 * Metoda koja brine o formatu ispisa u skladu sa specifikacijom zadatka.
	 * 
	 * @param path
	 *            zadana staza do direktorija.
	 * @throws IOException
	 *             u slučaju nedozvoljenih situacija u skladu s propozicijama
	 *             zadatka.
	 */
	private void processFile(Path path) throws IOException {
		permissions(path);
		totalSize(path);
		dateOfCreation(path);
		nameWithExtension(path);
	}

	/**
	 * Metoda koja brine o propisanom ispisu imena i ekstenzije datoteke.
	 * 
	 * @param path
	 *            zadana staza do direktorija.
	 * @throws IOException
	 *             u slučaju nedozvoljenih situacija u skladu s propozicijama
	 *             zadatka.
	 */
	private void nameWithExtension(Path path) throws IOException {
		String pathName = path.getFileName().toString();
		env.writeln(pathName);
	}

	/**
	 * Metoda koja brine o ispisu datuma kreiranja u skladu s propisanim
	 * zahtjevima zadatka.
	 * 
	 * @param path
	 *            zadana staza do direktorija.
	 * @throws IOException
	 *             u slučaju nedozvoljenih situacija u skladu s propozicijama
	 *             zadatka.
	 */
	private void dateOfCreation(Path path) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		BasicFileAttributeView faView = Files.getFileAttributeView(path, BasicFileAttributeView.class,
				LinkOption.NOFOLLOW_LINKS);
		BasicFileAttributes attributes = faView.readAttributes();
		FileTime fileTime = attributes.creationTime();
		String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));
		env.write(formattedDateTime + " ");

	}

	/**
	 * Metoda koja brine o ispisu ukupne veličine datoteke u skladu s propisanim
	 * zahtjevima zadatka.
	 * 
	 * @param path
	 *            zadana staza do direktorija.
	 * @throws IOException
	 *             u slučaju nedozvoljenih situacija u skladu s propozicijama
	 *             zadatka.
	 */
	private void totalSize(Path path) throws IOException {
		long len = Files.size(path);
		String out = String.valueOf(len);
		env.write(CONST_SPACE.substring(0, CONST_SPACE.length() - out.length()) + out + " ");
	}

	/**
	 * Metoda koja brine o ispisu (ne)dopustivih svojstava datoteke u skladu s
	 * propisanim zahtjevima zadatka. Pritom se promatra je li datoteka
	 * direktorij, smije li se iz nje čitati, u nju upisivati i je li izvršiva
	 * datoteka, respektivno.
	 * 
	 * @param path
	 *            zadana staza do direktorija.
	 * @throws IOException
	 *             u slučaju nedozvoljenih situacija u skladu s propozicijama
	 *             zadatka.
	 */
	private void permissions(Path path) throws IOException {
		if (Files.isDirectory(path)) {
			env.write("d");
		} else {
			env.write("-");
		}
		if (Files.isReadable(path)) {
			env.write("r");
		} else {
			env.write("-");
		}
		if (Files.isWritable(path)) {
			env.write("w");
		} else {
			env.write("-");
		}
		if (Files.isExecutable(path)) {
			env.write("x");
		} else {
			env.write("-");
		}
	}

}
