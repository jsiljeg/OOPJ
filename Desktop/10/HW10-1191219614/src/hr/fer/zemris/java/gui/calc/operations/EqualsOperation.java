package hr.fer.zemris.java.gui.calc.operations;

/**
 * Razred koji implementira operator jednakosti.
 * 
 * @author Jure Šiljeg
 *
 */
public class EqualsOperation implements IOperation {

	/**
	 * Vraća rezultat prethodnih operacija ili sam broj nad kojim je pozvan ovaj
	 * operator i ukoliko nije bilo drugog sudionika pri vršenju operacije.
	 * 
	 * @see hr.fer.zemris.java.gui.calc.operations.IOperation#calculate(double,
	 *      double)
	 */
	@Override
	public double calculate(double first, double second) {
		return first;
	}

}
