package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Klasa za generiranje iznimki uparseru.
 * 
 * @author Jure Šiljeg
 *
 */
public class SmartScriptParserException extends RuntimeException {
	/** Serijski broj UID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Kontruktor za ispisivane greške prilikom parsiranja.
	 * 
	 * @param s
	 *            poruka koja se ispisuje.
	 */
	public SmartScriptParserException(String s) {
		super(s);
	}
}
