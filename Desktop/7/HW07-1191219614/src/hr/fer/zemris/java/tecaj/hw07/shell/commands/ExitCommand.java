package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.support.CommandsSupport;
import hr.fer.zemris.java.tecaj.hw07.shell.environments.Environment;

/**
 * Razred koji brine o funkcionalnosti komande exit.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class ExitCommand extends CommandsSupport implements ShellCommand {

	/**
	 * Defaultni konstruktor koji služi za punjenje opisa komande.
	 */
	public ExitCommand() {
		createDescriptions();
	}

	/**
	 * Metoda koja služi za kreiranje opisa komande.
	 */
	private void createDescriptions() {
		List<String> tmp = new ArrayList<>();
		tmp.add("UPUTE ZA KORIŠTENJE: ");
		tmp.add("1. Očekuje se jedino riječ exit.");
		tmp.add("2. Komanda terminira korisnikov rad s ljuskom.");
		tmp.add("PRIMJER POZIVA:");
		tmp.add("exit");
		tmp.add("PRIMJER ISPISA:");
		tmp.add("Nikakva poruka se ne ispisuje i prekida se izvođenje programa (ljuska se gasi!).");
		setDescriptions(tmp);

	}

	/**
	 * {@inheritDoc} Označava kraj rada s ljuskom.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) throws IOException {

		String[] field = arguments.trim().split("\\s+");
		if (field.length != 0 && !arguments.trim().equals("")) {
			env.writeln("Nedozvoljen broj argumenata!");
			throw new IllegalArgumentException();
		}
		return ShellStatus.TERMINATE;
	}

	@SuppressWarnings("javadoc")
	@Override
	public String getCommandName() {
		return "exit";
	}

	@SuppressWarnings("javadoc")
	@Override
	public List<String> getCommandDescription() {
		return getDescriptions();
	}

}
