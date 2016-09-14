package hr.fer.zemris.java.tecaj.hw5.db;

/**
 * Razred koji implementira operator manje od.
 * 
 * @author Jure Šiljeg
 * @version 1.0.
 */
public class LikeComparison implements IComparisonOperator {
	/**
	 * {@inheritDoc} Provjerava nalazi li se pojedini dio drugog stringa nekako
	 * unutar prvoga.
	 */
	@Override
	public boolean satisfied(String value1, String value2) {

		if (value2.lastIndexOf("*") != value2.indexOf("*") || !value2.contains("*")) {
			throw new IllegalArgumentException();
		}
		if (value2.startsWith("*")) { // * na početku
			return value1.endsWith(value2.substring(1));
		} else if (value2.endsWith("*")) { // * na kraju
			return value1.startsWith(value2.substring(0, value2.length() - 1));
		} else { // * u sredini
			return value1.startsWith(value2.substring(0, value2.indexOf("*")))
					&& value1.endsWith(value2.substring(value2.indexOf("*") + 1, value2.length()));

		}
	}

}
