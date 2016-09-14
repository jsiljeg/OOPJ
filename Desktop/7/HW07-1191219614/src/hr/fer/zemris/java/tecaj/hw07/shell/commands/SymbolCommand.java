package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.support.CommandsSupport;
import hr.fer.zemris.java.tecaj.hw07.shell.environments.Environment;

/**
 * Razred koji brine o funkcionalnosti komande symbol.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class SymbolCommand extends CommandsSupport implements ShellCommand {

	/**
	 * Defaultni konstruktor koji služi za punjenje opisa komande.
	 */
	public SymbolCommand() {
		createDescriptions();
	}

	/**
	 * Metoda koja služi za kreiranje opisa komande.
	 */
	private void createDescriptions() {
		List<String> tmp = new ArrayList<>();
		tmp.add("UPUTE ZA KORIŠTENJE: ");
		tmp.add("1. Očekuje se jedan ili dva argumenta.");
		tmp.add("2. Prvi argument mora biti validna ključna riječ: ");
		tmp.add("   2.1. PROMPT - odnosi se na defaultni ispis u konzolu prije unosa komande. Defaultni znak je '>'");
		tmp.add("   2.2. MORELINES - odnosi se na znak za prijelom teksta u novi redak. Defaultni znak je '\'.");
		tmp.add("   2.3. MULTILINES - odnosi se na znak za višelinijski upis i prikazuje se na početku svake linije u navedenom tipu upisa. Defaultni znak je '|'.");
		tmp.add("3. Drugi argument predstavlja novi znak za ključnu riječ.");
		tmp.add("5. Komanda će u oba slučaja obavijestiti korisnika o eventualnoj promjeni ili trenutnom znaku za ključnu riječ, te, naravno, izvršiti interno samu promjenu.");
		tmp.add("PRIMJER POZIVA:");
		tmp.add("a) symbol PROMPT");
		tmp.add("b) symbol \\");
		tmp.add("   PROMPT $");
		tmp.add("PRIMJER ISPISA:");
		tmp.add("a) Symbol for PROMPT is '>'");
		tmp.add("b) Symbol for PROMPT changed from '>' to '$'");
		setDescriptions(tmp);

	}

	/**
	 * {@inheritDoc} Komanda podešena za upravljanje operacijama promjene i
	 * ispisa ugrađenih karakterističnih simbola za višelinijski upis, prijeloma
	 * u novi red i defaultnog ispisa prilikom unosa nove komande.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) throws IOException {
		String[] field = arguments.split("\\s+");
		if (field.length != 1 && field.length != 2) {
			env.writeln("Nedozvoljen broj argumenata!");
			throw new IllegalArgumentException();
		} else if (field.length == 1) {
			if (!checkWord(field[0].trim())) {
				env.writeln("Koristite neku od riječi: PROMPT, MORELINES, MULTILINE!!");
				throw new IllegalArgumentException();
			} else {
				env.write("Symbol for " + arguments + " is '");
				switch (arguments) {
				case "PROMPT":
					env.writeln(env.getPromptSymbol() + "'");
					break;
				case "MORELINES":
					env.writeln(env.getMorelinesSymbol() + "'");
					break;
				case "MULTILINE":
					env.writeln(env.getMultilineSymbol() + "'");
					break;
				}
			}
		} else {
			if (!checkWord(field[0].trim())) {
				env.writeln("Koristite neku od riječi: PROMPT, MORELINES, MULTILINE!!");
				throw new IllegalArgumentException();
			} else {
				env.write("Symbol for " + field[0].trim() + " changed from '");
				Character sign = field[1].trim().charAt(0);
				switch (field[0].trim()) {
				case "PROMPT":
					env.writeln(env.getPromptSymbol() + "' to '" + sign + "'");

					env.setPromptSymbol(sign);
					break;
				case "MORELINES":
					env.writeln(env.getMorelinesSymbol() + "' to '" + sign + "'");
					env.setMorelinesSymbol(sign);
					break;
				case "MULTILINE":
					env.writeln(env.getMultilineSymbol() + "' to '" + sign + "'");
					env.setMultilineSymbol(sign);
					break;
				}
			}
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Metoda provjerava je li proslijeđena riječ dopustiva.
	 * 
	 * @param word
	 *            riječ za koju se provjera i vrši.
	 * @return istinu ako je iz skupa dopustivih, inače laž.
	 */
	private boolean checkWord(String word) {
		return word.equals("PROMPT") || word.equals("MULTILINE") || word.equals("MORELINES");
	}

	@SuppressWarnings("javadoc")
	@Override
	public String getCommandName() {
		return "symbol";
	}

	@SuppressWarnings("javadoc")
	@Override
	public List<String> getCommandDescription() {
		return getDescriptions();
	}

}
