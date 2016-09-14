package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.support.CommandsSupport;
import hr.fer.zemris.java.tecaj.hw07.shell.environments.Environment;

/**
 * Razred koji brine o funkcionalnosti komande tree.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class TreeCommand extends CommandsSupport implements ShellCommand, FileVisitor<Path> {

	/**
	 * Defaultni konstruktor koji služi za punjenje opisa komande.
	 */
	public TreeCommand() {
		createDescriptions();
	}

	/**
	 * Metoda koja služi za kreiranje opisa komande.
	 */
	private void createDescriptions() {
		List<String> tmp = new ArrayList<>();
		tmp.add("UPUTE ZA KORIŠTENJE: ");
		tmp.add("1. Očekuje se jedan argument - ime direktorija.");
		tmp.add("2. Komanda ispisuje stablastu strukturu direktorija u konzolu tako da svaki direktorij-level pomiče ispis za 2 prazna znaka udesno.");
		tmp.add("PRIMJER POZIVA:");
		tmp.add("tree C:\\Users\\Jure\\Desktop\\Predavanje3\\src");
		tmp.add("PRIMJER ISPISA:");
		tmp.add("  hr");
		tmp.add("    fer");
		tmp.add("      zemris");
		tmp.add("        java");
		tmp.add("          tecaj");
		tmp.add("            p3");
		tmp.add("              Demonstracija.java");
		tmp.add("              GeometrijskiLik.java");
		tmp.add("              Pravokutnik.java");
		tmp.add("            p3b");
		tmp.add("              Demonstracija.java");
		tmp.add("              GeometrijskiLik.java");
		tmp.add("              Pravokutnik.java");
		tmp.add("              SadrziocTocaka.java");
		setDescriptions(tmp);
	}

	/** Dubina rekurzie prilikom obilaska stabla direktorija. */
	private int level;

	/**
	 * {@inheritDoc} Ispisuje se rekurzivni, stablasti ispis direktorija.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) throws IOException {

		String[] field = verifyAndParseArguments(env, arguments);
		if (field[1] != null) {
			env.writeln("Nedozvoljen broj argumenata!");
			throw new IllegalArgumentException();
		}

		directoryCheck(env, field[0].trim());

		Files.walkFileTree(Paths.get(field[0].trim()), new TreeCommand());

		return ShellStatus.CONTINUE;
	}

	@SuppressWarnings("javadoc")
	@Override
	public String getCommandName() {
		return "tree";
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
	 * Metoda koja brine o stablastom ispisu strukture direktorija.
	 * 
	 * @param file
	 *            zadana staza do direktorija.
	 */
	private void ispisi(Path file) {
		if (level == 0) {
			System.out.println(file.normalize().toAbsolutePath());
		} else {
			System.out.printf("%" + (2 * level) + "s%s%n", "", file.getFileName());
		}
	}

}
