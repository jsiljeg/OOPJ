package hr.fer.zemris.java.gui.layouts;

/**
 * Razred koji modelira određene lokacije za razmještaj komponenata u
 * kalkulator: (redak, stupac).
 * 
 * @author Jure Šiljeg
 *
 */
public class RCPosition {
	/** Označava indeks retka. */
	private int row;
	/** Označava indeks stupca. */
	private int column;

	/**
	 * Konstruktor za inicijalizaciju retka i stupca.
	 * 
	 * @param row
	 *            indeks retka na koji postavljamo.
	 * @param column
	 *            indeks stupca na koji postavljamo.
	 */
	public RCPosition(int row, int column) {
		super();
		this.row = row;
		this.column = column;
	}

	/**
	 * Getter za redak.
	 * 
	 * @return traženi indeks.
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Getter za stupac.
	 * 
	 * @return traženi indeks.
	 */
	public int getColumn() {
		return column;
	}

}
