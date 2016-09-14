package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Klasa predstavlaj validni string
 * 
 * @author Jure Šiljeg
 *
 */
public class ElementString extends Element {
	/** Pohranjena vrijednost stringa. */
	private String value;

	/**
	 * Konstruktor.
	 * 
	 * @param value
	 *            inicijalizacijska varijabla
	 */
	public ElementString(String value) {
		this.value = value;
	}

	/**
	 * Za ispis metoda.
	 */
	@Override
	public String asText() {
		return String.valueOf(value);
	}

	/**
	 * Getter za člansku variablu
	 * 
	 * @return tu variablu.
	 */
	public String getValue() {
		return value;
	}
}
