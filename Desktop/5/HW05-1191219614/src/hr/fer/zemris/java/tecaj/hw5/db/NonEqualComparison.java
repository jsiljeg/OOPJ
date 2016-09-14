package hr.fer.zemris.java.tecaj.hw5.db;

import java.text.Collator;
import java.util.Locale;

/**
 * Razred koji implementira operator za različitost stringova.
 * 
 * @author Jure Šiljeg
 * @version 1.0.
 */
public class NonEqualComparison implements IComparisonOperator {

	/**
	 * {@inheritDoc} Provjerava se je li prvi string različit od drugoga.
	 */
	@Override
	public boolean satisfied(String value1, String value2) {
		Collator hrCollator = Collator.getInstance(new Locale("hr"));
		hrCollator.setStrength(Collator.SECONDARY);

		return hrCollator.compare(value1, value2) != 0;
	}

}
