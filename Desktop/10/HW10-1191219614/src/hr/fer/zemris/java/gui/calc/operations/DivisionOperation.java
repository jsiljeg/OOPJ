package hr.fer.zemris.java.gui.calc.operations;

/**
 * Razred koji implementira operaciju dijeljenja.
 * 
 * @author Jure Šiljeg
 *
 */
public class DivisionOperation implements IOperation {

	/**
	 * Izračunava se količnik dvaju faktora.
	 * 
	 * @see hr.fer.zemris.java.gui.calc.operations.IOperation#calculate(double,
	 *      double)
	 */
	@Override
	public double calculate(double first, double second) {
		if (second == 0.) {
			throw new ArithmeticException("Dijeljenje nulom!");
		}
		return first / second;
	}

}
