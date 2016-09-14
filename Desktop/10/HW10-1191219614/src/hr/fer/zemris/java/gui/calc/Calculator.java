package hr.fer.zemris.java.gui.calc;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.gui.calc.functions.CosinusFunction;
import hr.fer.zemris.java.gui.calc.functions.CotangensFunction;
import hr.fer.zemris.java.gui.calc.functions.IFunction;
import hr.fer.zemris.java.gui.calc.functions.InverseFunction;
import hr.fer.zemris.java.gui.calc.functions.LogarithmFunction;
import hr.fer.zemris.java.gui.calc.functions.NaturalLogarithmFunction;
import hr.fer.zemris.java.gui.calc.functions.SinusFunction;
import hr.fer.zemris.java.gui.calc.functions.TangensFunction;
import hr.fer.zemris.java.gui.calc.numbers.INumber;
import hr.fer.zemris.java.gui.calc.numbers.DecimalNumber;
import hr.fer.zemris.java.gui.calc.numbers.SigningNumber;
import hr.fer.zemris.java.gui.calc.operations.AdditionOperation;
import hr.fer.zemris.java.gui.calc.operations.DivisionOperation;
import hr.fer.zemris.java.gui.calc.operations.EqualsOperation;
import hr.fer.zemris.java.gui.calc.operations.IOperation;
import hr.fer.zemris.java.gui.calc.operations.MultiplyOperation;
import hr.fer.zemris.java.gui.calc.operations.PowerOperation;
import hr.fer.zemris.java.gui.calc.operations.SubtractionOperation;
import hr.fer.zemris.java.gui.layouts.CalcLayout;

/**
 * Razred za demonstraciju izgleda i funkcionalnosti nekog kalkulatora.
 * 
 * @author Jure Šiljeg
 *
 */
public class Calculator {
	/** Vrijednost koja se prikazuje u prozoru za ispis unutar kalkulatora. */
	private static String displayValue = "";
	/** Konačan rezultat pojedinih operacija. */
	private static Double finalResult = null;
	/**
	 * Drugi operand koji sudjeluje u pojedinim operacijama u slučajevima koji
	 * zahtijevaju operaciju između 2 operanda.
	 */
	private static Double operand = null;
	/** Stog na koji se sprema pomoću push i pop. Modeliran je listom. */
	private static List<Double> stack = new ArrayList<>();

	/** Pojedina operacija koja će se izvršiti. */
	private static IOperation operation;
	/** Podatak koji nam govori želimo li inverze funkcije imati */
	private static boolean isInverse = false;
	/**
	 * Zastavica koja je indikator u višeargumentnim operacijama smijemo li
	 * parsirati vrijednost.
	 */
	private static boolean flag = true;

	/**
	 * Metoda prihvaća broj kao input.
	 * 
	 * @param number
	 *            Number strategiju.
	 * @param input
	 *            Ulazni string iz kojeg vadimo vrijednost.
	 * @param displayScreen
	 *            Oznaka za prozor u koji upisujemo i iz kojeg čitamo rezultate.
	 */
	private static void accept(INumber number, String input, JLabel displayScreen) {
		displayValue = number.apply(input, displayValue);
		displayScreen.setText(displayValue);
	}

	/**
	 * Metoda prihvaća operaciju kao input.
	 * 
	 * @param currOperation
	 *            trenutna operacija.
	 * @param displayScreen
	 *            Oznaka za prozor u koji upisujemo i iz kojeg čitamo rezultate.
	 */
	private static void accept(IOperation currOperation, JLabel displayScreen) {
		if (displayValue.equals("") && finalResult == null) {
			return;
		}

		if (finalResult == null) {
			finalResult = Double.parseDouble(displayValue);
			displayValue = "";
		} else if (!displayValue.equals("")) {
			operand = Double.parseDouble(displayValue);
			if (isInverse && operation instanceof PowerOperation) {
				operand = 1 / operand;
			}
			finalResult = operation.calculate(finalResult, operand);
			displayValue = String.valueOf(finalResult);
			displayScreen.setText(displayValue);
			displayValue = "";
		}
		operation = currOperation;

		flag = !flag;
	}

	/**
	 * Metoda prihvaća funkciju kao input.
	 * 
	 * @param func
	 *            Ulazna funkcija.
	 * @param label
	 *            Oznaka za prozor u koji upisujemo i iz kojeg čitamo rezultate.
	 */
	private static void accept(IFunction func, JLabel label) {
		displayValue = String.valueOf(func.processFunction(isInverse, label.getText()));
		label.setText(displayValue);
	}

	/**
	 * Razred za modeliranje glavnog prozora.
	 * 
	 * @author Jure Šiljeg
	 *
	 */
	static class CalcWindow extends JFrame {
		/** Defaultni serijski ID. */
		private static final long serialVersionUID = 1L;

		/**
		 * Defaultni konstruktor za glavni prozor. Postavlja se lokacija glavnog
		 * prozora; njegova širina i visina; ime koje će se prikazati te
		 * trenutak kada umire.
		 */
		public CalcWindow() {
			setLocation(20, 50);
			setSize(getWidth(), getHeight());
			setTitle("Calculator");
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

			initGUI();
		}

		/** Inicijalizira se grafičko sučelje. */
		private void initGUI() {
			Container cp = getContentPane();
			cp.setLayout(new BorderLayout());
			JPanel p = new JPanel(new CalcLayout(3));
			cp.add(p, BorderLayout.CENTER);

			addButtons(p);
		}

		/**
		 * Dodaju se gumbi u panel.
		 * 
		 * @param p
		 *            Ulazni panel.
		 */
		private void addButtons(JPanel p) {
			JLabel displayScreen = new JLabel();
			p.add(displayScreen, "1,1");

			addNumbers(p, displayScreen);
			addOperations(p, displayScreen);
			addFunctions(p, displayScreen);

			JButton push = new JButton("push");
			p.add(push, "3,7");
			push.addActionListener(button -> {
				stack.add(Double.parseDouble(displayValue));
			});

			JButton pop = new JButton("pop");
			p.add(pop, "4,7");
			pop.addActionListener(button -> {
				int len = stack.size();
				if (len == 0) {
					throw new IllegalArgumentException("Stog je prazan!");
				}
				displayValue = String.valueOf(stack.get(len - 1));
				stack.remove(len - 1);
				displayScreen.setText(displayValue);
			});

			JCheckBox inverse = new JCheckBox("inv");
			p.add(inverse, "5,7");
			inverse.addActionListener(button -> {
				isInverse = !isInverse;
			});

			JButton clr = new JButton("clr");
			p.add(clr, "1,7");
			clr.addActionListener(button -> {
				displayValue = "";
				displayScreen.setText(displayValue);
			});

			JButton res = new JButton("res");
			p.add(res, "2,7");
			res.addActionListener(button -> {
				displayValue = "";
				finalResult = null;
				inverse.setSelected(false);
				isInverse = false;
				displayScreen.setText(displayValue);
			});
		}

		/**
		 * Metoda koja dodaje gumbe koji predstavljaju neku funkciju u panel.
		 * 
		 * @param p
		 *            Ulazni panel.
		 * @param displayScreen
		 *            Oznaka za prozor u koji upisujemo i iz kojeg čitamo
		 *            rezultate.
		 */
		private void addFunctions(JPanel p, JLabel displayScreen) {

			JButton inverse = new JButton("1/x");
			p.add(inverse, "2,1");
			inverse.addActionListener(fun -> {
				accept(new InverseFunction(), displayScreen);
			});

			JButton sin = new JButton("sin");
			p.add(sin, "2,2");
			sin.addActionListener(fun -> {
				accept(new SinusFunction(), displayScreen);
			});

			JButton cos = new JButton("cos");
			p.add(cos, "3,2");
			cos.addActionListener(fun -> {
				accept(new CosinusFunction(), displayScreen);
			});

			JButton log = new JButton("log");
			p.add(log, "3,1");
			log.addActionListener(fun -> {
				accept(new LogarithmFunction(), displayScreen);
			});

			JButton ln = new JButton("ln");
			p.add(ln, "4,1");
			ln.addActionListener(fun -> {
				accept(new NaturalLogarithmFunction(), displayScreen);
			});

			JButton tan = new JButton("tan");
			p.add(tan, "4,2");
			tan.addActionListener(fun -> {
				accept(new TangensFunction(), displayScreen);
			});

			JButton ctg = new JButton("ctg");
			p.add(ctg, "5,2");
			ctg.addActionListener(fun -> {
				accept(new CotangensFunction(), displayScreen);
			});

		}

		/**
		 * Metoda koja dodaje gumbe koji predstavljaju neku operaciju u panel.
		 * 
		 * @param p
		 *            Ulazni panel.
		 * @param displayScreen
		 *            Oznaka za prozor u koji upisujemo i iz kojeg čitamo
		 *            rezultate.
		 */
		private void addOperations(JPanel p, JLabel displayScreen) {
			JButton equals = new JButton("=");
			p.add(equals, "1,6");
			equals.addActionListener(op -> {
				accept(new EqualsOperation(), displayScreen);
			});

			JButton add = new JButton("+");
			p.add(add, "5,6");
			add.addActionListener(op -> {
				accept(new AdditionOperation(), displayScreen);
			});

			JButton subtract = new JButton("-");
			p.add(subtract, "4,6");
			subtract.addActionListener(op -> {
				accept(new SubtractionOperation(), displayScreen);
			});

			JButton mul = new JButton("*");
			p.add(mul, "3,6");
			mul.addActionListener(op -> {
				accept(new MultiplyOperation(), displayScreen);
			});

			JButton div = new JButton("/");
			p.add(div, "2,6");
			div.addActionListener(op -> {
				accept(new DivisionOperation(), displayScreen);
			});

			JButton pow = new JButton("x^n");
			p.add(pow, "5,1");
			pow.addActionListener(op -> {
				accept(new PowerOperation(), displayScreen);
			});

		}

		/**
		 * Metoda koja dodaje gumbe koji predstavljaju nekakav broj u panel.
		 * 
		 * @param p
		 *            Ulazni panel.
		 * @param displayScreen
		 *            Oznaka za prozor u koji upisujemo i iz kojeg čitamo
		 *            rezultate.
		 */
		private void addNumbers(JPanel p, JLabel displayScreen) {
			JButton point = new JButton(".");
			point.addActionListener(num -> {
				accept(new DecimalNumber(), point.getText(), displayScreen);
			});
			p.add(point, "5,5");

			JButton zero = new JButton("0");
			zero.addActionListener(num -> {
				accept(new DecimalNumber(), zero.getText(), displayScreen);
			});
			p.add(zero, "5,3");

			JButton one = new JButton("1");
			one.addActionListener(num -> {
				accept(new DecimalNumber(), one.getText(), displayScreen);
			});
			p.add(one, "4,3");

			JButton two = new JButton("2");
			two.addActionListener(num -> {
				accept(new DecimalNumber(), two.getText(), displayScreen);
			});
			p.add(two, "4,4");

			JButton three = new JButton("3");
			three.addActionListener(num -> {
				accept(new DecimalNumber(), three.getText(), displayScreen);
			});
			p.add(three, "4,5");

			JButton four = new JButton("4");
			four.addActionListener(num -> {
				accept(new DecimalNumber(), four.getText(), displayScreen);
			});
			p.add(four, "3,3");

			JButton five = new JButton("5");
			five.addActionListener(num -> {
				accept(new DecimalNumber(), five.getText(), displayScreen);
			});
			p.add(five, "3,4");

			JButton six = new JButton("6");
			six.addActionListener(num -> {
				accept(new DecimalNumber(), six.getText(), displayScreen);
			});
			p.add(six, "3,5");

			JButton seven = new JButton("7");
			seven.addActionListener(num -> {
				accept(new DecimalNumber(), seven.getText(), displayScreen);
			});
			p.add(seven, "2,3");

			JButton eight = new JButton("8");
			eight.addActionListener(num -> {
				accept(new DecimalNumber(), eight.getText(), displayScreen);
			});
			p.add(eight, "2,4");

			JButton nine = new JButton("9");
			nine.addActionListener(num -> {
				accept(new DecimalNumber(), nine.getText(), displayScreen);
			});
			p.add(nine, "2,5");

			JButton sign = new JButton("+/-");
			sign.addActionListener(num -> {
				accept(new SigningNumber(), sign.getText(), displayScreen);
			});
			p.add(sign, "5,4");
		}
	}

	/**
	 * Glavna metoda koja pokrećegrafičko sučelje za demosntraciju rada i
	 * izgleda kalkulatora.
	 * 
	 * @param args
	 *            argumenti komandne linije (nisu predviđeni).
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new CalcWindow();
			frame.pack();
			frame.setVisible(true);
		});
	}

}
