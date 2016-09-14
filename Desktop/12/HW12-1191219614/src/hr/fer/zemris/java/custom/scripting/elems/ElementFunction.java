package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Klasa predstavlja validnu funkciju
 * 
 * @author Jure Šiljeg
 *
 */
public class ElementFunction extends Element {
	/** Pohranjeno ime funkcije. */
	private String name;

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 *            inicijalizacijska varijabla
	 */
	public ElementFunction(String name) {
		this.name = name;
	}

	/**
	 * Za ispis metoda.
	 */
	@Override
	public String asText() {
		return name;
	}

	/**
	 * Getter za člansku variablu
	 * 
	 * @return tu variablu.
	 */
	public String getName() {
		return name;
	}
}
