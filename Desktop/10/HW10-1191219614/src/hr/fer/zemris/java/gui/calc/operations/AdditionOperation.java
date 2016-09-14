package hr.fer.zemris.java.gui.calc.operations;

/**
 * Razred koji implementira operaciju zbrajanja.
 * 
 * @author Jure Šiljeg
 *
 */
public class AdditionOperation implements IOperation {

	/**
	 * Izračunava se suma dvaju pribrojnika.
	 * 
	 * @see hr.fer.zemris.java.gui.calc.operations.IOperation#calculate(double,
	 *      double)
	 */
	@Override
	public double calculate(double first, double second) {
		return first + second;
	}

}
