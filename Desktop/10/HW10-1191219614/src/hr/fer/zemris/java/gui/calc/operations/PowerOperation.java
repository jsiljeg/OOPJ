package hr.fer.zemris.java.gui.calc.operations;

/**
 * Razred koji implementira operaciju potenciranja.
 * 
 * @author Jure Šiljeg
 *
 */
public class PowerOperation implements IOperation {

	/**
	 * Izračuna se i vrati first^second.
	 * 
	 * @see hr.fer.zemris.java.gui.calc.operations.IOperation#calculate(double,
	 *      double)
	 */
	@Override
	public double calculate(double first, double second) {
		return Math.pow(first, second);
	}
}
