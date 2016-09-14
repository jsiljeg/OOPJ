package hr.fer.zemris.java.gui.layouts;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Razred za demonstraciju izgleda kalkulatora. Moguća su prazna mjesta i "rupe"
 * unutar samog dizajna kalkulatora u ovoj demonstraciji.
 * 
 * @author Jure Šiljeg
 *
 */
public class CalcLayoutDemo {
	/**
	 * Dodaju se komponente u kalkulator.
	 * 
	 * @param pane
	 *            glavni kontejner u koji ubacujemo.
	 */
	public static void addComponentsToPane(Container pane) {
		// neke komponente možete otkomentirati ili zakomentirati da se uvjerite
		// da radi što i treba
		pane.setLayout(new BorderLayout());
		JPanel p = new JPanel(new CalcLayout(3));
		pane.add(p, BorderLayout.CENTER);

		p.add(new JLabel("0"), new RCPosition(1, 1));
		p.add(new JButton("="), new RCPosition(1, 6));
		p.add(new JButton("clr"), new RCPosition(1, 7));
		p.add(new JButton("1/x"), new RCPosition(2, 1));
		p.add(new JButton("sin"), "2,2");
		p.add(new JButton("7"), new RCPosition(2, 3));
		p.add(new JButton("8"), new RCPosition(2, 4));
		p.add(new JButton("9"), new RCPosition(2, 5));
		p.add(new JButton("/"), new RCPosition(2, 6));
		// p.add(new JButton("res"), new RCPosition(2, 7));
		p.add(new JButton("log"), new RCPosition(3, 1));
		p.add(new JLabel("cos"), new RCPosition(3, 2));
		p.add(new JLabel("4"), new RCPosition(3, 3));
		p.add(new JLabel("5"), new RCPosition(3, 4));
		p.add(new JLabel("6"), new RCPosition(3, 5));
		// p.add(new JLabel("*"), new RCPosition(3, 6));
		// p.add(new JLabel("push"), new RCPosition(3, 7));
		p.add(new JLabel("ln"), new RCPosition(4, 1));
		p.add(new JLabel("tan"), new RCPosition(4, 2));
		// p.add(new JLabel("1"), new RCPosition(4, 3));
		p.add(new JLabel("2"), new RCPosition(4, 4));
		p.add(new JLabel("3"), new RCPosition(4, 5));
		p.add(new JLabel("-"), new RCPosition(4, 6));
		// p.add(new JLabel("pop"), new RCPosition(4, 7));
		p.add(new JLabel("x^n"), new RCPosition(5, 1));
		p.add(new JLabel("ctg"), new RCPosition(5, 2));
		p.add(new JLabel("0"), new RCPosition(5, 3));
		// p.add(new JLabel("+/-"), new RCPosition(5, 4));
		// p.add(new JLabel("."), new RCPosition(5, 5));
		// p.add(new JLabel("+"), new RCPosition(5, 6));
		p.add(new JLabel("inv"), new RCPosition(5, 7));

	}

	/**
	 * Metoda za inicijalizaciju grafičkog sučelja.
	 */
	private static void createAndShowGUI() {

		JFrame frame = new JFrame("Calculator");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		addComponentsToPane(frame.getContentPane());

		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Metoda koja služi za pokretanje demonstracijskog programa.
	 * 
	 * @param args
	 *            argumenti komandne linije (nisu očekivani).
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
