package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.support.CommandsSupport;
import hr.fer.zemris.java.tecaj.hw07.shell.environments.Environment;

/**
 * Razred koji brine o funkcionalnosti komande mkdir.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class MkdirCommand extends CommandsSupport implements ShellCommand {

	/**
	 * Defaultni konstruktor koji služi za punjenje opisa komande.
	 */
	public MkdirCommand() {
		createDescriptions();
	}

	/**
	 * Metoda koja služi za kreiranje opisa komande.
	 */
	private void createDescriptions() {
		List<String> tmp = new ArrayList<>();
		tmp.add("UPUTE ZA KORIŠTENJE: ");
		tmp.add("1. Očekuje se jedan argument - staza direktorija.");
		tmp.add("2. Komanda stvara stablo zadane strukture direktorija.");
		tmp.add("PRIMJER POZIVA:");
		tmp.add("mkdir C:\\Users\\Jure\\Desktop\\Stablasta\\Struktura");
		tmp.add("PRIMJER ISPISA:");
		tmp.add("Korisniku se ne ispisuje ništa, ali se stvori zadana hijerarhija direktorija!");
		setDescriptions(tmp);

	}

	/**
	 * {@inheritDoc} Komanda stvara odgovarajuću datotečnu strukturu.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) throws IOException {
		String[] field = verifyAndParseArguments(env, arguments);
		if (field[1] != null) {
			env.writeln("Nedozvoljen broj argumenata!");
			throw new IllegalArgumentException();
		}

		Path path = Paths.get(field[0].trim());
		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
				env.writeln("Stvorili ste direktorij: " + path.toString());
			} catch (IOException e) {
				env.writeln("Nije moguće stvoriti datoteku!");
				throw new AccessDeniedException(field[0]);
			}
		}

		return ShellStatus.CONTINUE;
	}

	@SuppressWarnings("javadoc")
	@Override
	public String getCommandName() {
		return "mkdir";
	}

	@SuppressWarnings("javadoc")
	@Override
	public List<String> getCommandDescription() {
		return getDescriptions();
	}

}
