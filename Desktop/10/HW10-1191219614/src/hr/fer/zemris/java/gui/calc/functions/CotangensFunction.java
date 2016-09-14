package hr.fer.zemris.java.gui.calc.functions;

/**
 * Razred koji implementira sučelje IFunction i računa kotangens od argumenta u
 * radijanima.
 * 
 * @author Jure Šiljeg
 *
 */
public class CotangensFunction implements IFunction {

	/**
	 * Metoda računa koatangens argumenta. Bacaju se iznimke u slučaju
	 * dijeljenja s nulom.
	 * 
	 * @see hr.fer.zemris.java.gui.calc.functions.IFunction#processFunction(boolean,
	 *      java.lang.String)
	 */
	@Override
	public double processFunction(boolean isInverse, String input) {
		if (!isInverse) {
			// radi se o kotangensu i sin != 0 mora biti
			if (Math.sin(Double.parseDouble(input)) == 0.) {
				throw new ArithmeticException("Dogodilo se dijeljenje s 0!");
			}
			return Math.atan(Double.parseDouble(input));
		} else { // inverz je pa cos != 0 mora biti
			if (Math.cos(Double.parseDouble(input)) == 0.) {
				throw new ArithmeticException("Dogodilo se dijeljenje s 0!");
			}
			return Math.tan(Double.parseDouble(input));
		}
	}

}
