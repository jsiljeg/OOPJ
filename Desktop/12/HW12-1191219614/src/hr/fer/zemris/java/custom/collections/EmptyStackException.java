package hr.fer.zemris.java.custom.collections;
/**
 * Klasa koja reprezentira novodefiniranu iznimku
 * @author Jure Šiljeg
 * @version 1.0.
 */
public class EmptyStackException extends RuntimeException {
	
	/**
	 * Univerzalni identifikator verzije za Serializable razred.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor koji obavlja 
	 * sve što i regularna iznimka 
	 * (pronađe putanju do pogreške i ispiše poruku).
	 * @param s poruka koja se ispisuje u konzolu
	 */
	public EmptyStackException(String s) {
		super(s);
	}
}
