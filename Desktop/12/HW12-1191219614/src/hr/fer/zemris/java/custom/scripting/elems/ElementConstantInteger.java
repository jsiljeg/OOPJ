package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Klasa koja predstavlja validni integer.
 * 
 * @author Jure Šiljeg
 *
 */
public class ElementConstantInteger extends Element {
	/** Pohranjena vrijednost. */
	private int value;

	/**
	 * Konstruktor.
	 * 
	 * @param value
	 *            inicijalizacijska varijabla.
	 */
	public ElementConstantInteger(int value) {
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
	public int getValue() {
		return value;
	}

}
