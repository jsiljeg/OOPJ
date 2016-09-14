package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.support.CommandsSupport;
import hr.fer.zemris.java.tecaj.hw07.shell.environments.Environment;

/**
 * Razred koji brine o funkcionalnosti komande cat.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class CatCommand extends CommandsSupport implements ShellCommand {

	/**
	 * Defaultni konstruktor koji služi za punjenje opisa komande.
	 */
	public CatCommand() {
		createDescriptions();
	}

	/**
	 * Metoda koja služi za kreiranje opisa komande.
	 */
	private void createDescriptions() {
		List<String> tmp = new ArrayList<>();
		tmp.add("UPUTE ZA KORIŠTENJE: ");
		tmp.add("1. Očekuje se jedan ili dva argumenta.");
		tmp.add("2. Prvi argument mora biti validna staza do neke datoteke.");
		tmp.add("3. Drugi argument je ime nekog charseta kojeg trebamo koristiti za interpretiranje bajtova datoteke.");
		tmp.add("4. Ako nije proslijeđen drugi argument, koristi se defaultni platformski charset.");
		tmp.add("5. Komanda otvara file sa zadane staze i ispisuje njegov sadržaj u konzolu.");
		tmp.add("PRIMJER POZIVA:");
		tmp.add("a) cat C:\\Users\\Jure\\Desktop\\provjera.txt");
		tmp.add("b) cat C:\\Users\\Jure\\Desktop\\provjera.txt  GBK");
		tmp.add("PRIMJER ISPISA:");
		tmp.add("a) Ne ba� tako te�ka zada�a iz OOPJJ. No, nemojmo izazivati sre�u!!");
		tmp.add("b) Ne ba� tako te歬a zada鎍 iz OOPJJ. No, nemojmo izazivati sre鎢!!");
		setDescriptions(tmp);
	}

	/**
	 * {@inheritDoc} Dodatno, otvara se zadana datoteka i ispisuje se njen
	 * sadržaj u konzolu.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) throws IOException {

		String[] field = verifyAndParseArguments(env, arguments);
		File f = new File(field[0].trim());
		BufferedReader in = null;

		if (field[1] == null) {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(f), Charset.defaultCharset()));
		} else {
			Map<String, Charset> chs = Charset.availableCharsets();
			if (!chs.containsKey(field[1].trim())) {
				env.writeln("Unijeli ste nepodržani charset!");
				throw new IllegalArgumentException();
			}
			in = new BufferedReader(new InputStreamReader(new FileInputStream(f), field[1].trim()));
		}
		String str;
		while ((str = in.readLine()) != null) {
			env.writeln(str);
		}

		return ShellStatus.CONTINUE;
	}

	@SuppressWarnings("javadoc")
	@Override
	public String getCommandName() {
		return "cat";
	}

	@SuppressWarnings("javadoc")
	@Override
	public List<String> getCommandDescription() {
		return getDescriptions();
	}

}
