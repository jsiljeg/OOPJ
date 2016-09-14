package hr.fer.zemris.java.gui.charts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.List;

/**
 * Implementacija stupčastog grafa.
 * 
 * @author Jure Šiljeg
 */
public class BarChart {
	/** Odmak od početka. */
	final int START_SPACING = 19;
	/** Razmak između stupaca u grafu. */
	final int BORDER_WIDTH = 4;
	/** Širina linije koja označava x i y os. */
	final int LINE_WIDTH = 2;
	/** Odmak od dna. */
	final int BOTTOM_SPACE = 13;
	/** Font za labele što označavaju x i y os (opisi). */
	private final Font AXES_LABEL_FONT = new Font("SansSerif", Font.BOLD, 13);
	/** Boja tih istih labela. */
	private final Color AXES_LABEL_COLOR = new Color(0, 138, 0);
	/** Boja kojom crtamo x i y os. */
	private final Color AXES_COLOR = new Color(79, 113, 176);
	/** Boja ispune stupaca. */
	private final Color BAR_FILL_IN = new Color(57, 198, 68);
	/** Boja rešetke. */
	private final Color GRID = new Color(221, 217, 34);
	/** Lista (x, y) koordinata. */
	List<XYValue> values;
	/** Oznaka koja piše uz x-os. */
	String xAxisName;
	/** Oznaka koja piše uz y-os. */
	String yAxisName;
	/** Minimalna y-koordinata */
	int minY;
	/** Maksimalna y-koordinata. */
	int maxY;
	/** Razmak između dvije linije mreže na y-osi. */
	int space;
	/** Širina glavnog prozora. */
	int width;
	/** Visina glavnog prozora. */
	int height;

	/**
	 * Konstruktor za stupčasti graf. Inicijaliziraju se sve veličine potrebne
	 * za crtanje grafa.
	 * 
	 * @param values
	 *            Ulazne vrijednosti (parovi koordinata).
	 * @param xAxisName
	 *            Oznaka koja piše uz x-os.
	 * @param yAxisName
	 *            Oznaka koja piše uz y-os.
	 * @param minY
	 *            Minimalna y-koordinata.
	 * @param maxY
	 *            Maksimalna y-koordinata.
	 * @param space
	 *            Razmak između dvije linije mreže na y-osi.
	 */
	public BarChart(List<XYValue> values, String xAxisName, String yAxisName, int minY, int maxY, int space) {
		super();
		this.values = values;
		this.xAxisName = xAxisName;
		this.yAxisName = yAxisName;
		this.minY = minY;
		this.maxY = maxY;
		if (space == 0) {
			throw new IllegalArgumentException("Razmak mora biti veći od 0!");
		}
		if ((maxY - minY) % space != 0) {
			this.space = (int) Math.floor((double) (maxY - minY) / (double) space);
		} else {
			this.space = space;
		}
	}

	/**
	 * Postavlja se širina i visina prozora.
	 * 
	 * @param width
	 *            vrijednost koja odgovara širini prozora.
	 * @param height
	 *            vrijednost koja odgovara visina prozora.
	 */
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Crta naš stupčasti graf tako što crta pojedine komponente.
	 * 
	 * @param g
	 *            Ulazna grafika.
	 */
	public void draw(Graphics g) {

		int tmpHeight = this.height;
		int tmpWidth = this.width - 2 * START_SPACING;
		Graphics2D g2d = (Graphics2D) g;
		// potrebno je pamtiti defaultnu transformaciju da se znamo "vratiti".
		AffineTransform defaultAt = g2d.getTransform();

		drawYLabel(g, g2d, tmpHeight);
		g2d.setTransform(defaultAt);

		drawXLabel(g, tmpWidth, tmpHeight);
		drawAxes(g2d, tmpWidth, tmpHeight);

		tmpWidth -= START_SPACING * 2;
		tmpHeight -= START_SPACING * 2;

		drawBars(g2d, tmpWidth, tmpHeight);
	}

	/**
	 * Iscrtava x i y os (s pripadnim strjelicama na kraju).
	 * 
	 * @param g2d
	 *            Ulazna 2D grafika.
	 * @param width
	 *            Ulazna širina.
	 * @param height
	 *            Ulazna visina.
	 */
	private void drawAxes(Graphics2D g2d, int width, int height) {
		g2d.setColor(AXES_COLOR);
		g2d.setStroke(new BasicStroke(LINE_WIDTH));

		// x-os
		int x1 = 3 * START_SPACING;
		int y1 = height - 3 * START_SPACING;
		int x2 = width - START_SPACING;
		int y2 = y1;
		g2d.drawLine(x1, y1, x2, y2);
		// strelica od x-osi
		g2d.drawLine(x2, y2, x2 - 2 * BORDER_WIDTH, y2 + LINE_WIDTH);
		g2d.drawLine(x2, y2, x2 - 2 * BORDER_WIDTH, y2 - LINE_WIDTH);

		// y-os
		x2 = x1;
		y2 = START_SPACING;
		g2d.drawLine(x1, y1, x2, y2);
		// strelica od y-osi
		g2d.drawLine(x2, y2, x2 + LINE_WIDTH, y2 + 2 * BORDER_WIDTH);
		g2d.drawLine(x2, y2, x2 - LINE_WIDTH, y2 + 2 * BORDER_WIDTH);
	}

	/**
	 * Iscrtava oznaku uz x os (opisnik).
	 * 
	 * @param g
	 *            Ulazna grafika.
	 * @param width
	 *            Ulazna širina.
	 * @param height
	 *            Ulazna visina.
	 */
	private void drawXLabel(Graphics g, int width, int height) {
		g.setFont(AXES_LABEL_FONT);
		g.setColor(AXES_LABEL_COLOR);
		g.drawString(xAxisName, width / 2 - 4 * START_SPACING, height - BOTTOM_SPACE);
	}

	/**
	 * Metoda iscrtava oznaku uz y-os (opisnik).
	 * 
	 * @param g
	 *            Ulazna grafika.
	 * @param g2d
	 *            Ulazna 2D grafika.
	 * @param height
	 *            Ulazna visina.
	 */
	private void drawYLabel(Graphics g, Graphics2D g2d, int height) {
		// za y os nam je potrebna rotacija za 90 stupnjeva u smjeru obrnutom od
		// smjera kazaljke na satu.
		AffineTransform at = new AffineTransform();
		at.rotate(-Math.PI / 2);
		g2d.setTransform(at);
		g.setFont(AXES_LABEL_FONT);
		g.setColor(AXES_LABEL_COLOR);
		g.drawString(yAxisName, -height / 2 - START_SPACING, START_SPACING);
	}

	/**
	 * Metoda koja crta stupce našeg grafa.
	 * 
	 * @param g2d
	 *            Ulazna 2D grafika.
	 * @param width
	 *            Ulazna širina.
	 * @param height
	 *            Ulazna visina.
	 */
	private void drawBars(Graphics2D g2d, int width, int height) {

		// vertikalno skliranje
		int scaleVertical = (height - 3 * START_SPACING) / maxY;
		// širina stupca grafa
		int w = (width - 3 * START_SPACING) / values.size();
		int x = 3 * START_SPACING;

		drawGrid(g2d, scaleVertical, w, x, width, height);
		for (XYValue val : values) {
			int y = (int) (height - scaleVertical * val.getY());
			// Ispisivanje oznaka
			g2d.setColor(Color.BLACK);
			g2d.drawString(String.valueOf(val.getX()), x + w / 2, height);
			// Popuna šipki
			g2d.setColor(BAR_FILL_IN);
			g2d.fillRect(x + LINE_WIDTH, y - START_SPACING - BORDER_WIDTH, w - BORDER_WIDTH,
					scaleVertical * val.getY() + BORDER_WIDTH);
			x += w;
		}
	}

	/**
	 * Metoda iscrtava rešetku u grafu zajedno s pripadnim oznakama brojeva na
	 * y-osi.
	 * 
	 * @param g2d
	 *            Ulazna 2D grafika.
	 * @param scaleVertical
	 *            Ulazni faktor skaliranja.
	 * @param w
	 *            Širina stupca.
	 * @param x
	 *            Početna pozicija unosa na x-osi.
	 * @param width
	 *            Širina ulaza.
	 * @param height
	 *            Visina ulaza.
	 */
	private void drawGrid(Graphics2D g2d, int scaleVertical, int w, int x, int width, int height) {
		FontMetrics fontMetrics = g2d.getFontMetrics();
		g2d.setStroke(new BasicStroke(1));

		// Iscrtavanje horizontalnih linija uz dodavanje brojeva
		int x1 = x - BORDER_WIDTH;
		int x2 = width + BORDER_WIDTH;
		int labelValue = space;
		for (int j = height - 3 * START_SPACING + BORDER_WIDTH / 2; j > START_SPACING + BORDER_WIDTH; j -= space
				* scaleVertical) {
			g2d.setColor(GRID);
			g2d.drawLine(x1, j, x2, j);
			// Brojevi
			g2d.setColor(GRID);
			g2d.drawString(String.valueOf(labelValue), x1 - fontMetrics.stringWidth(String.valueOf(labelValue)),
					j + BORDER_WIDTH);
			labelValue += space;
		}

		g2d.setColor(GRID);
		// Iscrtavanje vertikalnih linija
		int y1 = height + BOTTOM_SPACE - START_SPACING;
		int y2 = START_SPACING + BORDER_WIDTH;
		for (int i = x; i <= width; i += w) {
			g2d.drawLine(i, y1, i, y2);
		}
	}
}
