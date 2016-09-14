package hr.fer.zemris.java.gui.charts;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

/**
 * Komponenta stupčastog grafa..
 * 
 * @author Jure Šiljeg
 */
public class BarChartComponent extends JComponent {
	/** Serijski broj ID. */
	private static final long serialVersionUID = 1L;
	/** Instanca stupčastog grafa. */
	private BarChart chart;

	/**
	 * Konsturktor zakKomponentu stupčastog grafa.
	 * 
	 * @param chart
	 *            stupčasti graf.
	 */
	public BarChartComponent(BarChart chart) {
		this.chart = chart;
	}

	/**
	 * 
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		chart.setSize(getWidth(), getHeight());
		chart.draw(g2d);
	}
}
