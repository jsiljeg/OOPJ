package hr.fer.zemris.java.tecaj.hw07.shell;

import java.io.IOException;

import hr.fer.zemris.java.tecaj.hw07.shell.commands.ShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.environments.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.environments.MyEnvironment;

/**
 * Razred zadužen za implementaciju ponašanja ljuske.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class MyShell {

	/**
	 * Glavna metoda koja se pokreće prilikom pokretanja programa i simulira rad
	 * s ljuskom.
	 * 
	 * @param args
	 *            srgumenti komandne linije
	 * @throws IOException
	 *             u slučaju nedozvoljenih situacija u skladu s propozicijama
	 *             zadatka.
	 */
	public static void main(String[] args) throws IOException {

		String totalInput = "";

		Environment environment = new MyEnvironment();

		((MyEnvironment) environment).writeln("Welcome to MyShell v 1.0");
		((MyEnvironment) environment).write(environment.getPromptSymbol() + " ");

		while (((MyEnvironment) environment).getCurrStatus() == ShellStatus.CONTINUE) {
			String input = environment.readLine().trim();
			if (input.charAt(input.length() - 1) == (char) environment.getMorelinesSymbol()) {
				((MyEnvironment) environment).write(environment.getMultilineSymbol() + " ");
				totalInput += input.substring(0, input.length() - 1);
				continue;
			}
			totalInput += input;
			// konačno imam gotovu lajnu

			// ekstrakcija imena komande
			String commandName = totalInput.split("\\s+")[0].trim();

			// ekstrakcija argumenata
			String arguments = totalInput.substring(commandName.length()).trim();

			if (((MyEnvironment) environment).getCommands().get(commandName) == null) {
				((MyEnvironment) environment).writeln("Unijeli ste nepostojeću komandu");
				totalInput = "";
				((MyEnvironment) environment).write(environment.getPromptSymbol() + " ");
				continue;
			}
			ShellCommand command = ((MyEnvironment) environment).getCommands().get(commandName);
			try {
				((MyEnvironment) environment).setCurrStatus(command.executeCommand(environment, arguments));
			} catch (Exception e) {
				((MyEnvironment) environment)
						.writeln("Unijeli ste pogrešan format komande " + command.getCommandName());
				totalInput = "";
				((MyEnvironment) environment).write(environment.getPromptSymbol() + " ");
				continue;
			}
			// da ne ispiše još jednom PROMPT nakon exita
			if (((MyEnvironment) environment).getCurrStatus() != ShellStatus.TERMINATE) {
				totalInput = "";
				((MyEnvironment) environment).write(environment.getPromptSymbol() + " ");
			}
		}

	}
}
