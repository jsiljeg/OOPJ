package hr.fer.zemris.java.tecaj.hw07.shell.environments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.CatCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.CharsetCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.CopyCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.ExitCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.HelpCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.HexdumpCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.LsCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.MkdirCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.ShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.SymbolCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.TreeCommand;

/**
 * Sučelje koje reprezentira specifične funkcionalnosti okruženja u kojem radimo
 * ovu našu ljusku. Koristi se za komunikaciju s korisnikom.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class MyEnvironment implements Environment, Iterable<ShellCommand> {

	/** Mapa dopuštenih komandi u ljusci. */
	private static Map<String, ShellCommand> commands;
	/** Trenutni status ljuske. */
	private static ShellStatus currStatus;
	/** Znak koji se prikazue prije svake komande koju korisnik unosi. */
	private Character prompt;
	/** Znak koji označava višelinijski unos. */
	private Character multiline;
	/** Terminalni znak za detekciju kraja unosa u trenutnom retku konzole. */
	private Character morelines;
	/** Varijabla koja omogućuje učitavanje iz konzole. */
	private BufferedReader br;

	/**
	 * Konstruktor za inicijalizaciju svih članskih varijabli. Inicijaliziraju
	 * se dopustive komande, specijalni znakovi i varijabla u koju spremamo
	 * učitano iz konzole.
	 */
	public MyEnvironment() {
		commands = new LinkedHashMap<>();
		commands.put("cat", new CatCommand());
		commands.put("charsets", new CharsetCommand());
		commands.put("copy", new CopyCommand());
		commands.put("exit", new ExitCommand());
		commands.put("help", new HelpCommand());
		commands.put("hexdump", new HexdumpCommand());
		commands.put("ls", new LsCommand());
		commands.put("mkdir", new MkdirCommand());
		commands.put("tree", new TreeCommand());
		commands.put("symbol", new SymbolCommand());
		currStatus = ShellStatus.CONTINUE;
		prompt = '>';
		multiline = '|';
		morelines = '\\';
		br = new BufferedReader(new InputStreamReader(System.in));
	}

	/**
	 * Getter za mapu komandi.
	 * 
	 * @return te komande.
	 */
	public Map<String, ShellCommand> getCommands() {
		return commands;
	}

	/**
	 * Getter za trenutni status ljuske.
	 * 
	 * @return taj status.
	 */
	public ShellStatus getCurrStatus() {
		return currStatus;
	}

	/**
	 * Setter za status ljuske.
	 * 
	 * @param currStatus
	 *            novistatus.
	 */
	public void setCurrStatus(ShellStatus currStatus) {
		MyEnvironment.currStatus = currStatus;
	}

	@SuppressWarnings("javadoc")
	@Override
	public void writeln(String text) throws IOException {
		System.out.println(text);
	}

	@SuppressWarnings("javadoc")
	@Override
	public void write(String text) throws IOException {
		System.out.print(text);
	}

	@SuppressWarnings("javadoc")
	@Override
	public void setPromptSymbol(Character symbol) {
		prompt = symbol;
	}

	@SuppressWarnings("javadoc")
	@Override
	public void setMultilineSymbol(Character symbol) {
		multiline = symbol;
	}

	@SuppressWarnings("javadoc")
	@Override
	public void setMorelinesSymbol(Character symbol) {
		morelines = symbol;
	}

	@SuppressWarnings("javadoc")
	@Override
	public String readLine() throws IOException {
		return br.readLine();
	}

	@SuppressWarnings("javadoc")
	@Override
	public Character getPromptSymbol() {
		return prompt;
	}

	@SuppressWarnings("javadoc")
	@Override
	public Character getMultilineSymbol() {
		return multiline;
	}

	@SuppressWarnings("javadoc")
	@Override
	public Character getMorelinesSymbol() {
		return morelines;
	}

	@SuppressWarnings({ "unchecked", "javadoc" })
	@Override
	public Iterable<ShellCommand> commands() {
		getCommands().forEach((k, v) -> System.out.println(k));
		return (Iterable<ShellCommand>) ((MyEnvironment) commands).iterator();
	}

	@SuppressWarnings("javadoc")
	@Override
	public Iterator<ShellCommand> iterator() {
		return ((MyEnvironment) commands).iterator();
	}

}
