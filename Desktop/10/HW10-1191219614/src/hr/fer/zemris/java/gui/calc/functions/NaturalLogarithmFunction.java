package hr.fer.zemris.java.gui.calc.functions;

/**
 * Razred koji implementira sučelje IFunction i računa prirodni logaritam od
 * argumenta.
 * 
 * @author Jure Šiljeg
 *
 */
public class NaturalLogarithmFunction implements IFunction {

	/**
	 * Metoda računa prirodni logaritam argumenta. Bacaju se iznimke u da
	 * argument nije iz domene!
	 * 
	 * @see hr.fer.zemris.java.gui.calc.functions.IFunction#processFunction(boolean,
	 *      java.lang.String)
	 */
	@Override
	public double processFunction(boolean isInverse, String input) {
		if (!isInverse) {
			return Math.log(Double.parseDouble(input));
		} else {
			return Math.pow(Math.E, Double.parseDouble(input));
		}
	}

}
