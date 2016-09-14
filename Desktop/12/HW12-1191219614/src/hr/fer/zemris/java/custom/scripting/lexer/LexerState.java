package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Razred koji služi za reprezentiranje enumeracije za biranje tipa
 * pregledavanja unosa.
 * 
 * @author Jure Šiljeg
 * @version 1.0.
 */
public enum LexerState {
	/**
	 * Prepoznaje određeni tip tokena i vraća ga. Također, preskače prazne
	 * prostore.
	 */
	TAG,

	/**
	 * Vraća dio od trenutnog indeksa do otvorenja taga ili do kraja datoteke.
	 */
	TEXT;
}
