package hr.fer.zemris.java.tecaj.hw07.shell.environments;

import java.io.IOException;

import hr.fer.zemris.java.tecaj.hw07.shell.commands.ShellCommand;

/**
 * Sučelje koje reprezentira funkcionalnosti okruženja u kojem radimo ljusku.
 * Koristi se za komunikaciju s korisnikom.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public interface Environment {

	/**
	 * Metoda zadužena za čitanje korisnikovih unosa iz konzole.
	 * 
	 * @return učitani String.
	 * @throws IOException
	 *             u slučaju nemogućnosti učitavanja.
	 */
	String readLine() throws IOException;

	/**
	 * Metoda koja ispisuje u komandnu liniju poruku, ali bez prijelaza (po
	 * završetku ispisivanja) u novi red.
	 * 
	 * @param text
	 *            tekst koji ispisujemo.
	 * @throws IOException
	 *             u slučaju nemogućnosti ispisivanja.
	 */
	void write(String text) throws IOException;

	/**
	 * Metoda koja ispisuje u komandnu liniju poruku, ali s prijelazom (po
	 * završetku ispisivanja) u novi red.
	 * 
	 * @param text
	 *            tekst koji ispisujemo.
	 * @throws IOException
	 *             u slučaju nemogućnosti ispisivanja.
	 */
	void writeln(String text) throws IOException;

	/**
	 * Metoda koja služi za vraćanje komande u iteriranju.
	 * 
	 * @return tu komandu.
	 */
	Iterable<ShellCommand> commands();

	/**
	 * Metoda za vraćanje simbola koji predstavlja mogućnost višelinijskog
	 * unosa.
	 * 
	 * @return znak koji to predstavlja.
	 */
	Character getMultilineSymbol();

	/**
	 * Metoda za postavljanje simbola koji predstavlja mogućnost višelinijskog
	 * unosa.
	 * 
	 * @param symbol
	 *            taj simbol.
	 */
	void setMultilineSymbol(Character symbol);

	/**
	 * Metoda za vraćanje simbola koji predstavlja prvu stvar koja će nam se
	 * ispisati prilikom pokretanja konzole i prije svakog novog komandnog
	 * naloga.
	 * 
	 * @return znak koji to predstavlja.
	 */
	Character getPromptSymbol();

	/**
	 * Metoda za postavljanje simbola koji predstavlja prvu stvar koja će nam se
	 * ispisati prilikom pokretanja konzole i prije svakog novog komandnog
	 * naloga.
	 * 
	 * @param symbol
	 *            taj simbol.
	 */
	void setPromptSymbol(Character symbol);

	/**
	 * Metoda za vraćanje simbola koji predstavlja kraj unosa u trenutnom retku.
	 * Može se nalaziti smao na zadnjoj poziciji prilikom unosa.
	 * 
	 * @return znak koji to predstavlja.
	 */
	Character getMorelinesSymbol();

	/**
	 * Metoda za postavljanje simbola koji predstavlja kraj unosa u trenutnom
	 * retku. Može se nalaziti smao na zadnjoj poziciji prilikom unosa.
	 * 
	 * @param symbol
	 *            taj simbol.
	 */
	void setMorelinesSymbol(Character symbol);
}
