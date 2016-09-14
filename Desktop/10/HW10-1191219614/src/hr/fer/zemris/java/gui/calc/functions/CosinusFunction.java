package hr.fer.zemris.java.gui.calc.functions;

/**
 * Razred koji implementira sučelje IFunction i računa kosinus od argumenta u
 * radijanima.
 * 
 * @author Jure Šiljeg
 *
 */
public class CosinusFunction implements IFunction {

	/**
	 * Metoda računa kosinus argumenta.
	 * 
	 * @see hr.fer.zemris.java.gui.calc.functions.IFunction#processFunction(
	 *      boolean, java.lang.String)
	 */
	@Override
	public double processFunction(boolean isInverse, String input) {
		return isInverse ? Math.cos(Double.parseDouble(input)) : Math.acos(Double.parseDouble(input));
	}

}
