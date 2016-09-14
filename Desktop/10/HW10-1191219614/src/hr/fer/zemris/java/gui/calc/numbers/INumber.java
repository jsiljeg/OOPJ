package hr.fer.zemris.java.gui.calc.numbers;

/**
 * Sučelje brine o svim inputima koji se unesu u ulazni prozor kalkulatora. Tu
 * je riječ o svim brojevnim zapisima decimalnih brojeva koji se trebaju
 * izvršavati korektno, kao i brojeva kojima želimo smao promijeniti predznak.
 * 
 * @author Jure Šiljeg
 *
 */
public interface INumber {

	/**
	 * Primjenjuje se input na trenutni input(current) - spajanje ili
	 * konkatenacija.
	 * 
	 * @param input
	 *            tekstualni unos.
	 * @param current
	 *            trenutni unos.
	 * @return spojeni input i trenutni string i takav string se vrati.
	 */
	String apply(String input, String current);
}
