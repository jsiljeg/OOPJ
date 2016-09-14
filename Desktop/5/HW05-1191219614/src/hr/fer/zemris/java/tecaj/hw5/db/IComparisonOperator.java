package hr.fer.zemris.java.tecaj.hw5.db;

/**
 * Sučelje koje predstavlja operacije nad nekim operandima.
 * 
 * @author Jure Šiljeg
 * @version 1.0.
 */
public interface IComparisonOperator {

	/**
	 * Metoda koja provjerava rezultate usporedbe raznih operatore nad dva
	 * stringa.
	 * 
	 * @param value1
	 *            prvi parametar.
	 * @param value2
	 *            drugi parametar.
	 * @return istinu ako je uvjet zadovoljen, inače laž.
	 */
	public boolean satisfied(String value1, String value2);
}
