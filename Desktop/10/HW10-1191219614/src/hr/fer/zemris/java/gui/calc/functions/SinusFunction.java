package hr.fer.zemris.java.gui.calc.functions;

/**
 * Razred koji implementira sučelje IFunction i računa sinus od argumenta u
 * radijanima.
 * 
 * @author Jure Šiljeg
 *
 */
public class SinusFunction implements IFunction {

	/**
	 * Metoda računa sinus argumenta.
	 * 
	 * @see hr.fer.zemris.java.gui.calc.functions.IFunction#processFunction(boolean,
	 *      java.lang.String)
	 */
	@Override
	public double processFunction(boolean isInverse, String input) {
		return isInverse ? Math.sin(Double.parseDouble(input)) : Math.asin(Double.parseDouble(input));
	}
}
