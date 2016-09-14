package hr.fer.zemris.java.gui.charts;

/**
 * Razred koji pohranjuje vrijednosti koordinata za x i y os.
 * 
 * @author Jure Šiljeg
 *
 */
public class XYValue {

	/** x koordinata za nacrtati. */
	private int x;
	/** y-koordinata za nacrtati. */
	private int y;

	/**
	 * Inicijalizacija koordinata.
	 * 
	 * @param x
	 *            vrijednot na x-osi.
	 * @param y
	 *            vrijednost na y-osi.
	 */
	public XYValue(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * Getter za x-koordinatu.
	 * 
	 * @return tu koordinatu.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Getter za y-koordinatu.
	 * 
	 * @return tu koordinatu.
	 */
	public int getY() {
		return y;
	}

}
