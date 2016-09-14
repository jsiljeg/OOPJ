package hr.fer.zemris.java.tecaj.hw07.shell.commands.support;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hr.fer.zemris.java.tecaj.hw07.shell.environments.Environment;

/**
 * Razred koji implementira metode koje koristimo za provjeru valjanosti
 * unesenih staza.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class CommandsSupport {

	/** Privatna lista u kou ćemo spremati informacije o komandi. */
	private List<String> descriptions;

	/**
	 * Setter za internu listu.
	 * 
	 * @param descriptions
	 *            lista kojom inicijaliziramo internu listu informacija.
	 */
	public void setDescriptions(List<String> descriptions) {
		this.descriptions = descriptions;
	}

	/**
	 * Getter za internu listu informacija.
	 * 
	 * @return tu listu.
	 */
	public List<String> getDescriptions() {
		return descriptions;
	}

	/**
	 * Defaultni konstruktor koji kreira praznu listu.
	 */
	public CommandsSupport() {
		descriptions = new ArrayList<>();
	}

	/**
	 * Metoda broji pojavljivanje navodnih znakova u unesenom retku.
	 * 
	 * @param env
	 *            okruženje ljuske.
	 * @param arguments
	 *            unesena korisnikova linija.
	 * @return broj pojavljivanja dotičnog znaka.
	 */
	public int countQuotes(Environment env, String arguments) {
		// Prebrojimo pojavljivanja navodnika u stringu...
		Pattern p = Pattern.compile("\"");
		Matcher m = p.matcher(arguments);
		int count = 0;
		while (m.find()) {
			++count;
		}
		return count;
	}

	/**
	 * Metoda za filtriranje loših unosa po pitanju broja navodnih znakova.
	 * 
	 * @param env
	 *            okruženje ljuske.
	 * @param arguments
	 *            unesena korisnikova linija.
	 * @throws IOException
	 *             u slučaju nedozvoljenih situacija u skladu s propozicijama
	 *             zadatka.
	 */
	public void checkPath(Environment env, String arguments) throws IOException {

		int count = countQuotes(env, arguments);
		// loše nadovnike napisali za slučaj count == 1
		if (count == 1) {
			env.writeln("Pozvali ste navodnike unutar datotečnog imena, ali samo jednom. Unesite korektno path!");
			throw new IllegalArgumentException();
		}
		// Ako je barem 3, imamo nedozvoljeno (iako dozvoljeno za escape) ime
		// datoteke
		if (count > 2 && count != 4) {
			env.writeln("Pozvali ste navodnike unutar datotečnog imena. Takva datoteka ne postoji!");
			throw new IllegalArgumentException();
		} // u ovom trenutku imamo ili točno 2 ili točno 0 ili točno 4 navodnika
	}

	/**
	 * MEtoda vrši dodatno filtriranje u slučaju da je korisnik unio 4 navodna
	 * znaka. To može biti i valjan i nevaljan unos koji onda iziskuje provjeru
	 * (radi li se možda o dvije staze). Metoda specijalno napravljena za copy
	 * komandu koja jedina izričito mora imati dvije staze čiju valjanost onda
	 * treba i provjeriti.
	 * 
	 * @param env
	 *            okruženje ljuske.
	 * @param arguments
	 *            unesena korisnikova linija.
	 * @return polje od 2 Stringa gdje svaki odgovara ekstrahiranom dijelu
	 *         unutar navodnih znakova.
	 * @throws IOException
	 *             u slučaju nedozvoljenih situacija u skladu s propozicijama
	 *             zadatka.
	 */
	private String[] extraCopyCheck(Environment env, String arguments) throws IOException {

		String[] str = null;
		String[] returnField = new String[2];
		str = arguments.trim().split("\"");
		returnField[0] = str[1];
		returnField[1] = str[3];

		if (!str[2].trim().equals("")) {
			env.writeln("Unijeli ste nešto između pathova s navodnicima što nije prazan prostor: " + str[2]);
			throw new IllegalArgumentException();
		}
		if (!str[0].trim().equals("")) {
			env.writeln("Unijeli ste nešto prije prvog navodnika što nije prazan prostor: " + str[0]);
			throw new IllegalArgumentException();
		}
		if (str.length > 4) {
			env.writeln("Previše argumenata ste unijeli!");
			throw new IllegalArgumentException();
		}

		return returnField;
	}

	/**
	 * Metoda za najopćenitije vraćanje ekstrahiranih unosa korisnika.
	 * 
	 * @param env
	 *            okruženje ljuske.
	 * @param arguments
	 *            unesena korisnikova linija.
	 * @return polje od 2 Stringa gdje svaki odgovara ekstrahiranom dijelu
	 *         unesenih staza/opcija od strane korisnika.
	 * @throws IOException
	 *             u slučaju nedozvoljenih situacija u skladu s propozicijama
	 *             zadatka.
	 */
	public String[] takePathTwoArgument(Environment env, String arguments) throws IOException {
		String[] returnField = new String[2];
		String[] str = null;

		if (arguments.contains("\"")) { // imamo 2 ili 4 navodnika
			if (countQuotes(env, arguments) == 4) { // 4 je specijalan slučaj za
													// copy
				return extraCopyCheck(env, arguments);
			} else {// 2 navodnika
				str = arguments.trim().split("\"");
				// potencijalno "....." .... situacija za copy
				if (arguments.trim().startsWith("\"")) {
					returnField[0] = str[1].trim();
					if (str.length == 3) {// postoji nešto desno od zadnjeg
											// navodnika
						returnField[1] = arguments.substring(arguments.lastIndexOf("\"") + 1).trim();
						// za blankove nekakve
						if (returnField[1].contains(" ")) {
							env.writeln("Nije dozvoljena nikakva praznina u drugom dijelu patha!");
							throw new IllegalArgumentException();
						}
					} else if (str.length == 2) {
						returnField[1] = null;// po potrebi ćemo upravljati ovom
												// info
												// kasnije
					} else {
						env.writeln("Previše argumenata!");
						throw new IllegalArgumentException();
					}

				} // potencijalno ..... "...." situacija za copy
				else if (arguments.trim().endsWith("\"")) {
					returnField[0] = str[0].trim();
					// za blankove nekakve
					if (returnField[0].contains(" ")) {
						env.writeln("Nije dozvoljena nikakva praznina u prvom dijelu patha!");
						throw new IllegalArgumentException();
					}
					if (str.length == 2) {
						returnField[1] = str[1].trim();
					} else {
						env.writeln("Previše argumenata!");
						throw new IllegalArgumentException();
					}
				} else {
					env.writeln("Navodnici su na krivim mjestima!");
					throw new IllegalArgumentException();
				}
			}
		} else { // imamo 0
			if (arguments.trim().equals("")) {
				env.writeln("Niste unijeli argumente!");
				throw new IllegalArgumentException();
			}
			str = arguments.trim().split("\\s+");
			if (str.length > 2) {
				env.writeln("Previše argumenata!");
				throw new IllegalArgumentException();
			} else { // ili 1 ili 2 argumenta
				returnField[0] = str[0].trim();
				if (str.length == 1) {
					returnField[1] = null; // po potrebi ćemo upravljati ovom
											// info
											// kasnije
				} else {
					returnField[1] = str[1];
				}
			}
		}

		return returnField;
	}

	/**
	 * Metoda za provjeru valjanosti staze. Koristeći
	 * {@link #checkPath (Environment, String) checkPath} i
	 * {@link #takePathTwoArgument (Environment, String) takePathTwoArgument}.
	 * 
	 * @param env
	 *            okruženje ljuske.
	 * @param arguments
	 *            unesena korisnikova linija.
	 * @return polje od 2 Stringa gdje svaki odgovara ekstrahiranom dijelu
	 *         unesenih staza/opcija od strane korisnika.
	 * @throws IOException
	 *             u slučaju nedozvoljenih situacija u skladu s propozicijama
	 *             zadatka.
	 */
	public String[] verifyAndParseArguments(Environment env, String arguments) throws IOException {
		checkPath(env, arguments);
		return takePathTwoArgument(env, arguments);
	}

	/**
	 * Provjerava se je li proslijeđeni parametar direktorij.
	 * 
	 * @param env
	 *            okruženje ljuske.
	 * @param arguments
	 *            unesena korisnikova linija.
	 * @return istinu ako jest.
	 * @throws IOException
	 *             u slučaju da nije direktorij.
	 */
	public boolean directoryCheck(Environment env, String arguments) throws IOException {
		Path home = Paths.get(arguments);
		if (!Files.isDirectory(home)) {
			env.writeln("Argument mora biti direktorij!");
			throw new IllegalArgumentException();
		}
		return true;
	}

	/**
	 * Provjerava se je li proslijeđeni parametar datoteka.
	 * 
	 * @param env
	 *            okruženje ljuske.
	 * @param arguments
	 *            unesena korisnikova linija.
	 * @throws IOException
	 *             u slučaju da nije direktorij.
	 */
	public void fileCheck(Environment env, String arguments) throws IOException {
		// Svjestan sam da se može izvesti ova funkcionalnost preko gornje
		// metode, ali sam izračunao da je ovako manje ukupno pozvanih linija
		// što daje + u odnosu na čitljivost metoda za komande, ali - za
		// čitljivost ovoga, a opet + zbog ukupnog broja linija koje se
		// izvršavaju. Ugl,
		// long story, ali nisam baš bzvz napisao sličnu funkcionalnost kroz 2
		// metode.
		// Da, znam i da se ovdje ne pišu baš ovakvi komentari, ali bi
		// moglo biti zbunjujuće na prvi pogled onome tko čita kod zašto imam
		// ovakav rasplet, pa sam htio pojasniti. Ovi komentari su ionako, zar
		// ne, namijenjeni podrobnijem razumijevanju impl. detalja za što
		// smatram ovu situaciju.
		Path home = Paths.get(arguments);
		if (Files.isDirectory(home)) {
			env.writeln("Argument mora biti datoteka, a ne direktorij!");
			throw new IllegalArgumentException();
		}

	}

}
