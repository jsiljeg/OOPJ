package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.support.CommandsSupport;
import hr.fer.zemris.java.tecaj.hw07.shell.environments.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.environments.MyEnvironment;

/**
 * Razred koji brine o funkcionalnosti komande help.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class HelpCommand extends CommandsSupport implements ShellCommand {

	/**
	 * Defaultni konstruktor koji služi za punjenje opisa komande.
	 */
	public HelpCommand() {
		createDescriptions();
	}

	/**
	 * Metoda koja služi za kreiranje opisa komande.
	 */
	private void createDescriptions() {
		List<String> tmp = new ArrayList<>();
		tmp.add("UPUTE ZA KORIŠTENJE: ");
		tmp.add("1. Očekuje se jedan ili nijedan argument.");
		tmp.add("2. Ako je pokrenuto s jednim argumentom, on mora biti ime komande.");
		tmp.add("3. Komanda u tom slučaju ispisuje ime komande i upute korištenja iste.");
		tmp.add("4. Ako je pokrenuto bez argumenata, treba se ispisati popis svih dostupnih komandi u ljusci.");
		tmp.add("PRIMJER POZIVA:");
		tmp.add("a) help");
		tmp.add("b) help exit");
		tmp.add("PRIMJER ISPISA:");
		tmp.add("a) ");
		tmp.add("Popis komandi:");
		tmp.add("cat");
		tmp.add("charsets");
		tmp.add("copy");
		tmp.add("exit");
		tmp.add("***");
		tmp.add("b) ");
		tmp.add("Ime komande: exit");
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
	 * {@inheritDoc} Komanda zadužena za ispisivanje svih podržanih komandi ako
	 * se ne proslijedi nikakav parametar, a ako dobije parametar koi označava
	 * ime komande, treba ispisati ime i opis zadane komande.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) throws IOException {
		if (arguments.trim().equals("")) {
			env.writeln("Popis komandi:");
			env.commands();
		}

		String field[] = arguments.trim().split("\\s+");

		if (field.length != 1) {
			env.writeln("Nedozvoljen broj argumenata!");
			throw new IllegalArgumentException();
		} else {
			if (((MyEnvironment) env).getCommands().get(field[0].trim()) == null) {
				env.writeln("Ne postoji komanda " + field[0]);
				throw new IllegalArgumentException();
			}
			env.writeln("Ime komande: " + ((MyEnvironment) env).getCommands().get(field[0].trim()).getCommandName());
			List<String> printList = ((MyEnvironment) env).getCommands().get(field[0].trim()).getCommandDescription();
			for (String item : printList) {
				env.writeln(item);
			}
		}

		return ShellStatus.CONTINUE;
	}

	@SuppressWarnings("javadoc")
	@Override
	public String getCommandName() {
		return "help";
	}

	@SuppressWarnings("javadoc")
	@Override
	public List<String> getCommandDescription() {
		return getDescriptions();
	}

}
