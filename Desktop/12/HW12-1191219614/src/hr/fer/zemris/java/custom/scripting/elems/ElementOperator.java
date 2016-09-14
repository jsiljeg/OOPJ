package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Klasa predstavlja validan operator.
 * 
 * @author Jure Šiljeg
 *
 */
public class ElementOperator extends Element {
	/** Pohranjeno ime operatora. */
	private String symbol;

	/**
	 * Predstavlja konstruktor za naš operator.
	 * 
	 * @param symbol
	 *            zadani simbol kojim se instancira.
	 */
	public ElementOperator(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * Metoda za vraćanje stringa.
	 */
	@Override
	public String asText() {
		return symbol;
	}

	/**
	 * Getter za čl. var. name.
	 * 
	 * @return to ime
	 */
	public String getName() {
		return symbol;
	}
}
