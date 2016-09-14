package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Razred koji reprezentira našu posebno dizajniranu grješku koju želimo da se
 * baci u slučaju pojdinih pogrješaka.
 * 
 * @author Jure Šiljeg
 * @version 1.0.
 */
public class LexerException extends IllegalArgumentException {
	/**
	 * Serijska verzia UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor za našu iznimku.
	 * 
	 * @param message
	 *            tekst koji želimo da nam se prikaže.
	 */
	public LexerException(String message) {
		super(message);
	}

}
