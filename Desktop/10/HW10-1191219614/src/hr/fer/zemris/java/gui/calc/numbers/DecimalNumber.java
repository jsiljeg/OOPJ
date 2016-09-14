package hr.fer.zemris.java.gui.calc.numbers;

/**
 * Razred koji brine o ispravnom zapisivanju decimalnih brojeva.
 * 
 * @author Jure Šiljeg
 *
 */
public class DecimalNumber implements INumber {

	/**
	 * Spaja 2 stringa u funkcionalan string reprezentant nekog decimalnog
	 * broja.
	 * 
	 * @see hr.fer.zemris.java.gui.calc.numbers.INumber#apply(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public String apply(String input, String current) {
		if (input.equals(".") && current.contains(".")) {
			return current;
		}
		return current + input;
	}

}
