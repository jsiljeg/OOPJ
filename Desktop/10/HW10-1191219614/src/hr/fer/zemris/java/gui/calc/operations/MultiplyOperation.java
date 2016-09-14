package hr.fer.zemris.java.gui.calc.operations;

/**
 * Razred koji implementira operaciju množenja.
 * 
 * @author Jure Šiljeg
 *
 */
public class MultiplyOperation implements IOperation {

	/**
	 * Izračunava se produkt dvaju faktora.
	 * 
	 * @see hr.fer.zemris.java.gui.calc.operations.IOperation#calculate(double,
	 *      double)
	 */
	@Override
	public double calculate(double first, double second) {
		return first * second;
	}

}
