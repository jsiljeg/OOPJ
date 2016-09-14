package hr.fer.zemris.java.gui.calc.functions;

/**
 * Razred koji implementira sučelje IFunction i računa logaritam po bazi 10 od
 * argumenta.
 * 
 * @author Jure Šiljeg
 *
 */
public class LogarithmFunction implements IFunction {

	/**
	 * Metoda računa logaritam p obazi 10 argumenta. Bacaju se iznimke u slučaju
	 * nedozvoljene domene!
	 * 
	 * @see hr.fer.zemris.java.gui.calc.functions.IFunction#processFunction(boolean,
	 *      java.lang.String)
	 */
	@Override
	public double processFunction(boolean isInverse, String input) {
		if (!isInverse) {
			return Math.log10(Double.parseDouble(input));
		} else {
			return Math.pow(10., Double.parseDouble(input));
		}
	}

}
