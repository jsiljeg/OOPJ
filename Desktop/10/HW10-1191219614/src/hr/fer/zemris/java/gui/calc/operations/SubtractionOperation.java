package hr.fer.zemris.java.gui.calc.operations;

/**
 * Razred koji implementira operaciju oduzimanja.
 * 
 * @author Jure Šiljeg
 *
 */
public class SubtractionOperation implements IOperation {

	/**
	 * Izračunava se razlika dvaju pribrojnika.
	 * 
	 * @see hr.fer.zemris.java.gui.calc.operations.IOperation#calculate(double,
	 *      double)
	 */
	@Override
	public double calculate(double first, double second) {
		return first - second;
	}

}
