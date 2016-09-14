package hr.fer.zemris.java.gui.calc.operations;

/**
 * Sučelje koji modelira pojedine operacije nad tipkama kalkulatora koje
 * označavaju neke operatore koje kad izvršimo, rezultat se ne prikazuje
 * automatski (za rauliku od funkcija). Potrebno je izračunati rezultantu dva
 * unesena broja. Ovo obuhvaća zbrajanje, oduzimanje, množenje, dijeljenje,
 * operator jednakosti, operator potenciranja.
 * 
 * @author Jure Šiljeg
 *
 */
public interface IOperation {
	/**
	 * Operacija koja se događa nad dva sudionika.
	 * 
	 * @param first
	 *            prvi sudionik zadane operacije.
	 * @param second
	 *            drugi sudionik zadane operacije.
	 * @return rezultat operacije.
	 */
	double calculate(double first, double second);
}
