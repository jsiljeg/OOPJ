package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.IOException;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;
import hr.fer.zemris.java.tecaj.hw07.shell.environments.Environment;

/**
 * Sučelje koje definira funkcionalnost svake pojedine komande.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public interface ShellCommand {

	/**
	 * Metoda koja izvršava zadanu komandu.
	 * 
	 * @param env
	 *            okruženje ljuske.
	 * @param arguments
	 *            unesena korisnikova linija.
	 * @return status ljuske.
	 * @throws IOException
	 *             u slučaju nedozvoljenih situacija u skladu s propozicijama
	 *             zadatka.
	 */
	ShellStatus executeCommand(Environment env, String arguments) throws IOException;

	/**
	 * Getter za komandno ime po kojem svaku identificiramo.
	 * 
	 * @return to ime.
	 */
	String getCommandName();

	/**
	 * Metoda za dobivane opisa svake komande.
	 * 
	 * @return zadani opis u obliku popisa.
	 */
	List<String> getCommandDescription();
}
