package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Razred koji služi za reprezentiranje enumeracije za tip prikazive cjeline.
 * 
 * @author Jure Šiljeg
 * @version 1.0.
 */
public enum TokenType {

	/**
	 * Token type containing text.
	 */
	TEXT,
	/**
	 * Token type containing a string.
	 */
	STRING,
	/**
	 * Token type containing a single symbol.
	 */
	SYMBOL,
	/**
	 * Token type containing a function representation.
	 */
	FUNCTION,
	/**
	 * Token type representing the end of the file.
	 */
	EOF,
	/**
	 * Token type containing a double value.
	 */
	DOUBLE,
	/**
	 * Token type containing an integer value.
	 */
	INTEGER,
	/**
	 * Token type containing a variable representation.
	 */
	VARIABLE;

}
