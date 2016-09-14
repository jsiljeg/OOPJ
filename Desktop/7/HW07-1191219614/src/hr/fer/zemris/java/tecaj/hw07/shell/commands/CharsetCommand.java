package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.support.CommandsSupport;
import hr.fer.zemris.java.tecaj.hw07.shell.environments.Environment;

/**
 * Razred koji brine o funkcionalnosti komande charsets.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class CharsetCommand extends CommandsSupport implements ShellCommand {

	/**
	 * Defaultni konstruktor koji služi za punjenje opisa komande.
	 */
	public CharsetCommand() {
		createDescriptions();
	}

	/**
	 * Metoda koja služi za kreiranje opisa komande.
	 */
	private void createDescriptions() {
		List<String> tmp = new ArrayList<>();
		tmp.add("UPUTE ZA KORIŠTENJE: ");
		tmp.add("1. Ne očekuje se niti jedan argument.");
		tmp.add("2. Komanda ispisuje listu imena svih podržanih charsetova u konzolu.");
		tmp.add("PRIMJER POZIVA:");
		tmp.add("charsets");
		tmp.add("PRIMJER ISPISA:");
		tmp.add("Big5");
		tmp.add("Big5-HKSCS");
		tmp.add("CESU-8");
		tmp.add("EUC-JP");
		tmp.add("***");
		setDescriptions(tmp);

	}

	/**
	 * {@inheritDoc} Dodatno, ispisuje se popis svih dostupnih charsetova
	 * podržanih na ovoj Java platformi u komandnu liniju.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) throws IOException {
		if (!arguments.trim().equals("")) {
			env.writeln("Nedozvoljen broj argumenata!");
			throw new IllegalArgumentException();
		}
		Map<String, Charset> chs = Charset.availableCharsets();
		for (Map.Entry<String, Charset> entry : chs.entrySet()) {
			env.writeln(entry.getKey());
		}
		return ShellStatus.CONTINUE;
	}

	@SuppressWarnings("javadoc")
	@Override
	public String getCommandName() {
		return "charsets";
	}

	@SuppressWarnings("javadoc")
	@Override
	public List<String> getCommandDescription() {
		return getDescriptions();
	}

}
