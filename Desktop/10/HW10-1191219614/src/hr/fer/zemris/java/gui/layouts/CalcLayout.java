package hr.fer.zemris.java.gui.layouts;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 * Razred koji gradi grafičku reprezentaciju izgleda kalkulatora.
 * 
 * @author Jure Šiljeg
 *
 */
public class CalcLayout implements java.awt.LayoutManager2 {
	/** Boja obruba svih komponenata unutar kalkulatora. */
	private static final Border BORDER = BorderFactory.createLineBorder(Color.blue, 1);
	/** Pozdainska boja roditelja. */
	private static final Color PARENT_BACKGROUND_COLOR = Color.lightGray;
	/** Pozadinska boja svih komponenti u kalkulatoru osim ekrana za unos. */
	private static final Color CHILDREN_COLOR = new Color(114, 159, 207);
	/** Pozadinska boja ekrana za unos. */
	private static final Color INPUT_FIELD = new Color(205, 222, 1);
	/** Broj redaka u kalkulatoru. */
	private static final int NUM_OF_ROWS = 5;
	/** Broj stupaca u kalkulatoru. */
	private static final int NUM_OF_COLS = 7;

	/** Širina rešetke. */
	private Dimension gridSize;
	/** Vertikalni razmak između komponenata unutar kalkulatora. */
	private int verticalGap;
	/** Horizontalni razmak između komponenata unutar kalkulatora. */
	private int horizontalGap;
	/**
	 * Mapa pomoću koje pamtimo unesene i uklonjene komponente za renderiranje
	 */
	private Map<Component, Object> map;

	/**
	 * Inicijalizira se univerzalni razmak između komponenata unutar kalkulatora
	 * (horizontalni = vertikalni).
	 * 
	 * @param gap
	 *            razmak na koji se postavlja.
	 */
	public CalcLayout(int gap) {
		horizontalGap = verticalGap = gap;
		map = new HashMap<>();
		gridSize = new Dimension(15, 15);
	}

	/**
	 * Defaultni razmak između komponenata koji se postavlja na 0 (nema
	 * razmaka).
	 */
	public CalcLayout() {
		this(0);
	}

	/**
	 * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String,
	 *      java.awt.Component)
	 */
	@Override
	public void addLayoutComponent(String name, Component comp) {
		// nije potrebna implementacija
	}

	/**
	 * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
	 */
	@Override
	public void layoutContainer(Container parent) {
		parent.setBackground(PARENT_BACKGROUND_COLOR);

		Insets insets = parent.getInsets();
		int ncomponents = parent.getComponentCount();

		if (ncomponents == 0) {
			return;
		}

		// Ukupna dimenzija roditelja
		Dimension size = parent.getSize();
		int totalW = size.width - (insets.left + insets.right);
		int totalH = size.height - (insets.top + insets.bottom);

		// Dimenzija ćelije s uključenim "popunama"
		int totalCellW = totalW / NUM_OF_COLS;
		int totalCellH = totalH / NUM_OF_ROWS;

		for (int i = 0; i < ncomponents; i++) {
			Component c = parent.getComponent(i);
			Object position = map.get(c);
			int col = 0, row = 0;

			if (position != null) {
				row = getRowIndex(position);
				col = getColIndex(position);

				int x, y, w, h;
				if (row == 1 && col == 1) {
					x = insets.left + horizontalGap;
					y = insets.top + verticalGap;
					w = 5 * totalCellW - horizontalGap;
					h = totalCellH - verticalGap;
				} else {
					x = insets.left + (totalCellW * (col - 1)) + horizontalGap;
					y = insets.top + (totalCellH * (row - 1)) + verticalGap;
					w = (totalCellW) - horizontalGap;
					h = (totalCellH) - verticalGap;
				}
				c.setBounds(x, y, w, h);

				if (c instanceof JLabel) {
					((JLabel) c).setBorder(BORDER);
					if (row == 1 && col == 1) {
						((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);
						((JLabel) c).setBackground(INPUT_FIELD);
					} else {
						((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
						((JLabel) c).setBackground(CHILDREN_COLOR);
					}
					((JLabel) c).setOpaque(true);
					((JLabel) c).setFont(new Font("Serif", Font.PLAIN, 16));
				} else if (c instanceof JCheckBox) {
					((JCheckBox) c).setBorder(BORDER);
					((JCheckBox) c).setHorizontalAlignment(SwingConstants.CENTER);
					((JCheckBox) c).setBackground(CHILDREN_COLOR);
					((JCheckBox) c).setBorderPainted(true);
					((JCheckBox) c).setFont(new Font("Serif", Font.PLAIN, 16));
				} else if (c instanceof JButton) {
					((JButton) c).setBorder(BORDER);
					((JButton) c).setFont(new Font("Serif", Font.PLAIN, 16));
					((JButton) c).setHorizontalAlignment(SwingConstants.CENTER);
					((JButton) c).setBackground(CHILDREN_COLOR);
					((JButton) c).setOpaque(true);
				}

			}
		}

	}

	/**
	 * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
	 */
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return getLayoutSize(parent, false);
	}

	/**
	 * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
	 */
	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return getLayoutSize(parent, true);
	}

	/**
	 * Metoda koja postavlja veličinu ukupnog razmještaja komponenata.
	 * 
	 * @param parent
	 *            komponenta roditelj (unutar nje gledamo kompoenente).
	 * @param isPreferred
	 *            radi li se o preferiranoj veličini ili minimalnoj (
	 *            <code> true </code> za preferiranu, <code> false </code> za
	 *            minimalnu).
	 * @return traženu dimenziju.
	 */
	private Dimension getLayoutSize(Container parent, boolean isPreferred) {
		Dimension largestSize = getLargestCellSize(parent, isPreferred);
		Insets insets = parent.getInsets();
		largestSize.width = (largestSize.width * gridSize.width) + (horizontalGap * (gridSize.width + 1)) + insets.left
				+ insets.right;
		largestSize.height = (largestSize.height * gridSize.height) + (verticalGap * (gridSize.height + 1)) + insets.top
				+ insets.bottom;
		return largestSize;
	}

	/**
	 * Metoda potražuje širinu i visinu najveće komponente. Bira se maksimum
	 * između prefeferiranih/minimalnih širina/visina.
	 * 
	 * @param parent
	 *            komponenta roditelj (unutar nje gledamo kompoenente).
	 * @param isPreferred
	 *            radi li se o preferiranoj veličini ili minimalnoj (
	 *            <code> true </code> za preferiranu, <code> false </code> za
	 *            minimalnu).
	 * @return traženu dimenziju.
	 */
	private Dimension getLargestCellSize(Container parent, boolean isPreferred) {
		int ncomponents = parent.getComponentCount();
		Dimension maxCellSize = new Dimension(0, 0);
		for (int i = 0; i < ncomponents; i++) {
			Component c = parent.getComponent(i);
			Object position = map.get(c);
			if (c != null && position != null) {
				Dimension componentSize;
				if (isPreferred) {
					componentSize = c.getPreferredSize();
				} else {
					componentSize = c.getMinimumSize();
				}
				maxCellSize.width = Math.max(maxCellSize.width, componentSize.width);
				maxCellSize.height = Math.max(maxCellSize.height, componentSize.height);
			}
		}
		return maxCellSize;
	}

	/**
	 * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
	 */
	@Override
	public void removeLayoutComponent(Component comp) {
		map.remove(comp);
	}

	/**
	 * @see java.awt.LayoutManager2#addLayoutComponent(java.awt.Component,
	 *      java.lang.Object)
	 */
	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		int row = getRowIndex(constraints);
		int col = getColIndex(constraints);
		checkBounds(row, col);
		setConstraints(comp, constraints);
	}

	/**
	 * Metoda parsira string tako da vraća određenu koordinatu.
	 * 
	 * @param i
	 *            koordinata unosa koju potražujemo.
	 * @param constraints
	 *            ograničenje koje promatramo.
	 * @return broj koji je ili koordinata retka ili stupca naše komponente
	 *         određene sa constraints.
	 */
	private int parseString(int i, String constraints) {
		String[] field = constraints.trim().split(",");
		return Integer.parseInt(field[i].trim());
	}

	/**
	 * Provjerava se legalnost granica.
	 * 
	 * @param row
	 *            indeks retka za koji provjeravamo je li legalan. Iznimka u
	 *            slučaju ilegalnosti.
	 * @param column
	 *            indeks stupca za koji provjeravamo je li legalan. Iznimka u
	 *            slučaju ilegalnosti.
	 */
	private void checkBounds(int row, int column) {
		if (column <= 0 || row <= 0 || column > NUM_OF_COLS || row > NUM_OF_ROWS) {
			throw new IndexOutOfBoundsException("Nije moguće dodati u layout element na poziciji (" + row + ", "
					+ column + "): nepravilne koordinate");
		}
	}

	/**
	 * Ekstrahira se indeks retka iz proslijeđenog ograničenja.
	 * 
	 * @param position
	 *            zadano ograničenje (lokacija na koju bismo htjeli smjestiti
	 *            objekt).
	 * @return indeks retka ukoliko je legalan. Inače iznimka.
	 */
	private int getRowIndex(Object position) {
		if (position instanceof RCPosition) {
			return ((RCPosition) position).getRow();
		} else if (position instanceof String) {
			return parseString(0, (String) position);
		} else {
			throw new IndexOutOfBoundsException("Dodali ste pod vrijednost nešto što nije ni String ni RCPosition");
		}
	}

	/**
	 * Ekstrahira se indeks stupca iz proslijeđenog ograničenja.
	 * 
	 * @param position
	 *            zadano ograničenje (lokacija na koju bismo htjeli smjestiti
	 *            objekt).
	 * @return indeks stupca ukoliko je legalan. Inače iznimka.
	 */
	private int getColIndex(Object position) {
		if (position instanceof RCPosition) {
			return ((RCPosition) position).getColumn();
		} else if (position instanceof String) {
			return parseString(1, (String) position);
		} else {
			throw new IndexOutOfBoundsException("Dodali ste pod vrijednost nešto što nije ni String ni RCPosition");
		}
	}

	/**
	 * Metoda ubacuje u mapu za renderiranje komponente u skladu s ograničenjem.
	 * Ovdje je ograničenje lokacija na koju se postavlja.
	 * 
	 * @param comp
	 *            komponenta koju postavljamo.
	 * @param position
	 *            lokacija na koju je želimo postaviti. Ilegalne lokacije su one
	 *            gdje već nešto postoji ili koje su van rubova.
	 */
	private void setConstraints(Component comp, Object position) {

		int row = getRowIndex(position);
		int col = getColIndex(position);

		for (Object value : map.values()) {
			int rowValue = getRowIndex(value);
			int colValue = getColIndex(value);

			if (row == rowValue && col == colValue) {
				throw new IllegalArgumentException("Nije moguće dodati u layout element na poziciji (" + row + ", "
						+ col + "): već postoji element na toj poziciji!");
			}
		}
		// provjera ubacujemo li na prozor u slučaju da je prozor već
		if (row == 1 && col < 6 && col > 1) {
			throw new IllegalArgumentException("Pokušali ste dodati komponentu na ekran za upisaivanje (na poziciju ("
					+ row + ", " + col + ")). To nije dozvoljeno");
		}
		map.put(comp, position);

	}

	/**
	 * @see java.awt.LayoutManager2#getLayoutAlignmentX(java.awt.Container)
	 */
	@Override
	public float getLayoutAlignmentX(Container target) {
		// nije potrebna implementacija
		return 0;
	}

	/**
	 * @see java.awt.LayoutManager2#getLayoutAlignmentY(java.awt.Container)
	 */
	@Override
	public float getLayoutAlignmentY(Container target) {
		// nije potrebna implementacija
		return 0;
	}

	/**
	 * @see java.awt.LayoutManager2#invalidateLayout(java.awt.Container)
	 */
	@Override
	public void invalidateLayout(Container target) {
		// nije potrebna implementacija
	}

	/**
	 * @see java.awt.LayoutManager2#maximumLayoutSize(java.awt.Container)
	 */
	@Override
	public Dimension maximumLayoutSize(Container target) {
		return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

}
