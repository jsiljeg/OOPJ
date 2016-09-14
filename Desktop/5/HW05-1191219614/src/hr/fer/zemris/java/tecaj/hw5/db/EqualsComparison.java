package hr.fer.zemris.java.tecaj.hw5.db;

import java.text.Collator;
import java.util.Locale;

/**
 * Razred koji implementira operator jednako.
 * 
 * @author Jure Šiljeg
 * @version 1.0.
 */
public class EqualsComparison implements IComparisonOperator {

	/**
	 * {@inheritDoc} Provjerava se jednakost stringova.
	 */
	@Override
	public boolean satisfied(String value1, String value2) {
		Collator hrCollator = Collator.getInstance(new Locale("hr"));
		hrCollator.setStrength(Collator.SECONDARY);

		return hrCollator.compare(value1, value2) == 0;
	}

}
