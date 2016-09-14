package hr.fer.zemris.java.gui.calc.functions;

/**
 * Razred koji implementira sučelje IFunction i računa inverz argumenta.
 * 
 * @author Jure Šiljeg
 *
 */
public class InverseFunction implements IFunction {

	/**
	 * Metoda računa inverz argumenta. Bacaju se iznimke u slučaju dijeljenja s
	 * nulom!
	 * 
	 * @see hr.fer.zemris.java.gui.calc.functions.IFunction#processFunction(boolean,
	 *      java.lang.String)
	 */
	@Override
	public double processFunction(boolean isInverse, String input) {
		if (!isInverse) {
			if (Double.parseDouble(input) == 0) {
				throw new ArithmeticException("Dijeljenje s nulom u inverzu!");
			}
			return 1. / Double.parseDouble(input);
		} else {
			return Double.parseDouble(input);
		}

	}

}
