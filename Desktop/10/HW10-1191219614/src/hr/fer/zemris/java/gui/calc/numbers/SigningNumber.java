package hr.fer.zemris.java.gui.calc.numbers;

/**
 * Razred koji brine o ispravnom zapisivanju predznaka nekog broja.
 * 
 * @author Jure Šiljeg
 *
 */
public class SigningNumber implements INumber {

	/**
	 * Mijenja predznak nekom broju.
	 * 
	 * @see hr.fer.zemris.java.gui.calc.numbers.INumber#apply(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public String apply(String input, String current) {
		if (current.startsWith("-")) {
			return current.substring(1);
		} else {
			return "-" + current;
		}
	}
}
