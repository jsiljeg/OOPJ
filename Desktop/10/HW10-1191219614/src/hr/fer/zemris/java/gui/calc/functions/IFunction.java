package hr.fer.zemris.java.gui.calc.functions;

/**
 * Sučelje koji modelira pojedine funkcije nad tipkama kalkulatora koje
 * označavaju neke funkcije koje kad izvršimo, automatski se rezultat ispiše (za
 * rauliku od operacija). Ovo uključuje sljedeće matematičke funkcije:
 * sinus/kosinus/tangens/kotangens kuta, inverz arhumenta, te logaritam po bazi
 * 10/prirodni logaritam pojedinog argumenta.
 * 
 * @author Jure Šiljeg
 *
 */
public interface IFunction {

	/**
	 * Metoda koja izvršava funkciju.
	 * 
	 * @param isInverse
	 *            <code> true </code> ako je riječ o inverznoj operaciji zadane
	 *            funkcije, inače <code> false </code>.
	 * @param input
	 *            ulaz iz kojeg parsiramo broj tipa double i koji postane
	 *            argument pojedine funkcije.
	 * @return vrijednost funkcije u tom argumentu.
	 */
	double processFunction(boolean isInverse, String input);
}
