package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Klasa predstavlja validni double format.
 * 
 * @author Jure Šiljeg
 *
 */
public class ElementConstantDouble extends Element {
	/** Vrijednost double broja. */
	private double value;

	/**
	 * Konstruktor za klasu.
	 * 
	 * @param name
	 *            inicijalizaciski element.
	 */
	public ElementConstantDouble(double name) {
		this.value = name;
	}

	/**
	 * Za ispis metoda.
	 */
	@Override
	public String asText() {
		return Double.toString(value);
	}

	/**
	 * Getter za člansku variablu
	 * 
	 * @return tu variablu.
	 */
	public double getName() {
		return value;
	}
}
