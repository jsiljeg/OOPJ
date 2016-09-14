package hr.fer.zemris.java.fractals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;

/**
 * Razred koji služi za promatranje slika fraktala izvedenih pomoću
 * Newton-Raphsonovih iteracija.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class Newton {
	/** Pozdravna poruka koja će se pokazati prilikom pokretanja programa. */
	private static final String WELCOME_MESSAGE = "Welcome to Newton-Raphson iteration-based fractal viewer. "
			+ "Please enter at least two roots, one root per line. " + "Enter 'done' when done.";
	/**
	 * Odzdravna poruka koja će se prikazati netom prije prikazivanja slike
	 * fraktala.
	 */
	private final static String GOODBYE_MESSAGE = "Image of fractal will appear shortly. Thank you.";
	/** Poruka koja će se pojaviti u slučaju nepravilnog unosa. */
	private final static String ERROR_MESSAGE = "Unijeli ste manje od 2 korijena! pokušajte ponovo!";
	/** Varijabla koja pomaže pri ispisu. */
	private final static String ROOT = "Root ";
	/** Komandni znak koji označava početak korisnikovog unosa u konzolu. */
	private final static String PROMPT = "> ";
	/** Glavni broj koji predstavlja koji po redu kompleksni broj unosimo. */
	private static int ROOT_COUNTER = 1;

	/** Lista u koju spremamo unesene kompleksne brojeve iz konzole. */
	private static List<Complex> listOfRoots;
	/** Polinom. */
	public static ComplexRootedPolynomial polynomial;

	/**
	 * Osnovna metoda za demosntraciju Newton-Raphsonove metode.
	 * 
	 * @param args
	 *            argumenti komandne linije (nisu predviđeni)
	 * @throws IOException
	 *             U slučaju problema s iščitavanjem linije.
	 */
	public static void main(String[] args) throws IOException {
		System.out.println(WELCOME_MESSAGE);
		System.out.print(ROOT + ROOT_COUNTER + PROMPT);

		listOfRoots = new ArrayList<Complex>();
		BufferedReader buffReader = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			String input = buffReader.readLine();

			if (input.toLowerCase().trim().equals("done")) {
				if (listOfRoots.size() < 2) {
					System.out.println(ERROR_MESSAGE);
					ROOT_COUNTER = 1;
					listOfRoots = new ArrayList<Complex>();
					System.out.print(ROOT + ROOT_COUNTER + PROMPT);
					continue;
				} else {
					break;
				}
			}
			Complex ithRoot;

			try {
				ithRoot = Complex.parse(input);
			} catch (NumberFormatException e) {
				System.out.print(ROOT + ROOT_COUNTER + PROMPT);
				continue;
			}

			listOfRoots.add(ithRoot);
			System.out.print(ROOT + ++ROOT_COUNTER + PROMPT);
		}

		System.out.println(GOODBYE_MESSAGE);
		// crtanje
		Complex[] roots = new Complex[listOfRoots.size()];
		roots = listOfRoots.toArray(roots);
		polynomial = new ComplexRootedPolynomial(roots);
		// za ne-paraleliziranu verziju
		// FractalViewer.show(new FractalProducerNoSpeedUpImpl());
		FractalViewer.show(new FractalProducerImpl());
	}

	/**
	 * Razred koji predstavlja kompleksni broj i operacije nad grupom.
	 * 
	 * @author Jure Šiljeg
	 * @version 1.0
	 */
	public static class Complex {
		/** Konstanta koja predstavlja kompleksni broj z = 0 */
		public static final Complex ZERO = new Complex(0, 0);
		/** /** Konstanta koja predstavlja kompleksni broj z = 1 */
		public static final Complex ONE = new Complex(1, 0);
		/** Konstanta koja predstavlja kompleksni broj z = -1 */
		public static final Complex ONE_NEG = new Complex(-1, 0);
		/** Konstanta koja predstavlja kompleksni broj z = i */
		public static final Complex IM = new Complex(0, 1);
		/** Konstanta koja predstavlja kompleksni broj z = -i */
		public static final Complex IM_NEG = new Complex(0, -1);

		/** Realni dio kompleksnog broja z. */
		private double re;
		/** Imaginarni dio kompleksnog broja z. */
		private double im;
		/** Kut kojeg kompleksni broja z zatvara s pozitivnom granom x-osi. */
		private double angle;
		/** Udaljenost kompleksnog broja z od ishodišta koordinatnog sustava. */
		private double module;

		/**
		 * Defaultni konstruktor koji stvara kompleksni broj z = 0.
		 */
		public Complex() {
			this(0., 0.);
		}

		/**
		 * Konstruktor koji instancira naš kompleksni broj.
		 * 
		 * @param re
		 *            Realni dio kompleksnog broja.
		 * @param im
		 *            Imaginarni dio kompleksnog broja.
		 */
		public Complex(double re, double im) {
			this.re = re;
			this.im = im;
			this.angle = Math.atan2(im, re);
			this.module = Math.sqrt(re * re + im * im);
		}

		/**
		 * Metoda računa modul kompleksnog broja.
		 * 
		 * @return traženi modul.
		 */
		public double module() {
			return module;
		}

		/**
		 * Metoda koja množi dva kompleksna broja.
		 * 
		 * @param c
		 *            predstavlja instancu drugog faktora.
		 * @return umnožak dva kompleksna broja.
		 */
		public Complex multiply(Complex c) {
			return new Complex(re * c.re - im * c.im, re * c.im + im * c.re);
		}

		/**
		 * Metoda pronalazi kompleksan broj recipročan broju nad kojim se poziva
		 * metoda.
		 * 
		 * @return vraća taj recipročni broj.
		 */
		private Complex reciprocal() {
			double scale = re * re + im * im;
			return new Complex(re / scale, -im / scale);
		}

		/**
		 * Metoda dijeli dva kompleksna broja.
		 * 
		 * @param c
		 *            djelitelj.
		 * @return količnik dva kompleksna broja.
		 */
		public Complex divide(Complex c) {
			if (c.re == 0.0 && c.im == 0.0) {
				throw new IllegalArgumentException("Nedozvoljen argument! Dogodit će se dijeljenje s 0!");
			}
			return new Complex(this.multiply(c.reciprocal()).re, this.multiply(c.reciprocal()).im);
		}

		/**
		 * Metoda koja zbraja dva kompleksna broja.
		 * 
		 * @param c
		 *            predstavlja instancu drugog pribrojnika.
		 * @return zbroj dva kompleksna broja.
		 */
		public Complex add(Complex c) {
			return new Complex(re + c.re, im + c.im);
		}

		/**
		 * Metoda koja oduzima dva kompleksna broja.
		 * 
		 * @param c
		 *            predstavlja instancu umanjitelja.
		 * @return razlika dva kompleksna broja.
		 */
		public Complex sub(Complex c) {
			// izbegnuto korištenje negate() jer se stvara objekt viška
			// bespotrebno
			return new Complex(re - c.re, im - c.im);
		}

		/**
		 * Metoda koja vraća negiran kompleksni broj.
		 * 
		 * @return takav broj.
		 */
		public Complex negate() {
			return new Complex(-re, -im);
		}

		/**
		 * Metoda računa kompleksni broj na neku potenciju.
		 * 
		 * @param n
		 *            koja potencija je u pitanju koja mora biti barem 0.
		 * @return kompleksni broj dobiven potenciranjem.
		 */
		public Complex power(int n) {
			if (n < 0) {
				throw new IllegalArgumentException("Nedozvoljen argument (potencija mora biti veća ili jednaka 0!)");
			}
			double r = this.module;
			r = Math.pow(r, n);
			double phi = angle;
			return new Complex(r * Math.cos(phi * n), r * Math.sin(phi * n));
		}

		/**
		 * Metoda koja računa n-te korijene kompleksnog broja.
		 * 
		 * @param n
		 *            koji korijen (mora biti barem 1).
		 * @return polje n-tih korijena.
		 */
		public List<Complex> root(int n) {
			if (n <= 0) { // za n == 1 vrati this, jasno
				throw new IllegalArgumentException("Nedozvoljen argument (korijen mora biti veći od 0!)");
			}
			List<Complex> roots = new ArrayList<>(n);
			double r = this.module;
			double phi = this.angle;
			for (int i = 0; i < n; i++) {
				double k = (phi + 2 * i * Math.PI) / n;
				roots.add(new Complex(Math.pow(r, 1. / n) * Math.cos(k), Math.pow(r, 1. / n) * Math.sin(k)));
			}
			return roots;
		}

		/**
		 * Pomoćna metoda koja je zadužena da obradi imaginarni dio.
		 * 
		 * @param item
		 *            string koji obrađujemo.
		 * @return ekstrahirani, imaginarni dio iz zapisa.
		 */
		public static double doImaginaryPart(String item) {
			double im = 0.0;
			if (item.equals("i") || item.equals("+i")) {
				im = 1;
			} else if (item.contains("-i")) {
				im = -1;
			} else {
				String[] splited = item.split("i");
				if (splited.length != 2) {
					throw new NumberFormatException("Unijeli ste neispravan format imaginarnog dijela!");
				} else {
					im = Double.parseDouble(splited[1]);
				}
			}
			return im;
		}

		/**
		 * Metoda zadužena da iz zapisa parsira kompleksni broj iz stringa
		 * dobivenog.
		 * 
		 * @param s
		 *            izraz kojeg obrađujemo.
		 * @return isparsirani, kompleksni broj.
		 */
		public static Complex parse(String s) {

			double re = 0.0, im = 0.0;
			try {
				String field[] = s.split("(?=[-+])");

				if (field.length == 1) {

					if (field[0].contains("i")) {
						im = doImaginaryPart(field[0].trim());
					} else {
						re = Double.parseDouble(field[0].trim());
					}

				} else if (field.length == 2) {

					re = Double.parseDouble(field[0].trim());
					im = doImaginaryPart(field[1].trim());

				} else {

					throw new NumberFormatException("Unijeli ste previše +/- znakova!");

				}
			} catch (NumberFormatException e) {

				System.out.println(e.getMessage());
				System.out.println("Pogrešan unos se dogodio.");
				throw new NumberFormatException();

			}
			return new Complex(re, im);
		}

		/**
		 * Metoda pronalazi realni dio kompleksnog broja.
		 * 
		 * @return traženi dio.
		 */
		public double getRe() {
			return re;
		}

		/**
		 * Metoda pronalazi imaginarni dio kompleksnog broja.
		 * 
		 * @return traženi dio.
		 */
		public double getIm() {
			return im;
		}

		/**
		 * Metoda za pametan ispis kompleksnog broja.
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			if (im == 0) {
				return re + "";
			}
			if (re == 0) {
				if (im < 0) {
					return "-i" + -im;
				} else {
					return "i" + im;
				}
			}
			if (im < 0) {
				return re + " - " + "i" + (-im);
			}
			return re + " + " + "i" + im;
		}

	}

	/**
	 * Razred koji reprezentira polinom dobiven iz popisa njegovih korijena.
	 * 
	 * @author Jure Šiljeg
	 * @version 1.0
	 */
	public static class ComplexRootedPolynomial {

		/** Konstanta koja predstavlja kompleksni broj z = 0 */
		public static final Complex ZERO_COMPLEX = new Complex(0, 0);
		/** /** Konstanta koja predstavlja kompleksni broj z = 1 */
		public static final Complex ONE = new Complex(1, 0);
		/** Konstanta koja predstavlja kompleksni broj z = -1 */
		public static final Complex ONE_NEG = new Complex(-1, 0);

		/** Polinom s kompleksnim koeficjentima. */
		ComplexPolynomial polynom;
		/** Polje u kojem se čuvaju korijeni kompleksnogbroja. */
		Complex[] roots;

		/**
		 * Konstruktor koji kreira polje kompleksnih korijena polinoma i sam
		 * polinom iz tih korijena.
		 * 
		 * @param roots
		 *            Kompleksni korijeni nekog polinoma.
		 */
		public ComplexRootedPolynomial(Complex[] roots) {
			this.roots = roots;
			Complex[] factors = new Complex[2];
			factors[0] = roots[0].negate();
			factors[1] = Complex.ONE;
			polynom = new ComplexPolynomial(factors);
			// vrlo čudno ponašanje na prvu, ali potrebno je napraviti novo
			// polje neededFactors
			for (int i = 1; i < roots.length; i++) {
				Complex[] neededFactors = new Complex[2];
				neededFactors[0] = roots[i].negate();
				neededFactors[1] = Complex.ONE;

				polynom = polynom.multiply(new ComplexPolynomial(neededFactors));
			}
		}

		/**
		 * Računa vrijednost polinoma s kompleksnim koeficjentima u nekoj
		 * kompleksnoj točki.
		 * 
		 * @param z
		 *            točka u kojoj računamo vrijednost polinoma.
		 * @return tu vrijednost.
		 */
		public Complex apply(Complex z) {
			return polynom.apply(z);
		}

		/**
		 * Konvertira polje korijena u polinom s kompleksnim koeficjentima.
		 * 
		 * @return taj polinom.
		 */
		public ComplexPolynomial toComplexPolynom() {
			return polynom;
		}

		/**
		 * Pametan ispis jednog faktora kojeg koristimo pri množenju i dobivanju
		 * polinoma iz njegovih korijena.
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (Complex z : roots) {
				sb.append("(z - (");
				sb.append(z);
				sb.append(")) ");
			}
			return sb.toString();
		}

		/**
		 * Pronalazi indeks najbližeg korijena za zadani kompleksni broj (točku
		 * ravnine). Udaljenost tog broja i korijena mora biti unutar zadanog
		 * okruženja (kugle).
		 * 
		 * @param z
		 *            točka za koju tražimo najbliži korijen.
		 * @param treshold
		 *            Maksimalna propisana udaljenost za koju ćemo pridijeliti
		 *            točki z indeks odgovarajućeg korijena koji je u ovoj
		 *            okolini (modul se gleda).
		 * @return indeks tog korijena u polju korijena ili -1 ako ne postoji
		 *         takav unutar zadanog okruženja.
		 */
		public int indexOfClosestRootFor(Complex z, double treshold) {
			int index = 0;
			double minDistance = roots[index].add(z.negate()).module;

			for (int i = 1; i < roots.length; ++i) {
				double someDistance = roots[i].add(z.negate()).module;
				if (someDistance < minDistance) {
					minDistance = someDistance;
					index = i;
				}
			}
			return minDistance < treshold ? index : -1;
		}
	}

	/**
	 * Razred koji predstavlja polinom s kompleksnim koeficjentima.
	 * 
	 * @author Jure Šiljeg
	 * @version 1.0
	 */
	public static class ComplexPolynomial {
		/** Varijabla koja predstavlja kompleksni broj z = 0. */
		private final static Complex ZERO_COMPLEX = new Complex(0, 0);

		/**
		 * Koeficjenti kompleksnog polinoma. Na prvo mjesto se stavi koeficjent
		 * slobodnog člana itd.
		 */
		private Complex[] factors;
		/** Stupanj polinoma. */
		private short order;

		/**
		 * Konsturktor za polinom s nul-koeficjentima.
		 * 
		 * @param size
		 *            predstavlja stupanj + 1 polinoma odnosno koliko
		 *            koeficjenata će biti.
		 */
		public ComplexPolynomial(int size) {
			// nema smisla delegiranje jer svakako moramo napisati barem
			// funkcionalnost naredne dvije linije
			factors = new Complex[size];
			Arrays.fill(factors, ZERO_COMPLEX);
			order = (short) (factors.length - 1);
		}

		/**
		 * Konstruktor za polinom iz dobivenih koeficjenata.
		 * 
		 * @param factors
		 *            koeficjenti polinoma.
		 */
		public ComplexPolynomial(Complex[] factors) {
			this.factors = new Complex[factors.length];
			this.factors = factors;
			order = (short) ((short) factors.length - 1);
		}

		/**
		 * Vraća stupanj polinoma. Npr. za f(z) = (7+2i)z^3+2z^2+5z+1 vraća 3.
		 * 
		 * @return taj stupanj.
		 */
		public short order() {
			return order;
		}

		/**
		 * Računa umnožak polinoma. Prvi faktor u umnošku je polinom nad kojim
		 * je pozvana metoda.
		 * 
		 * @param q
		 *            Drugi faktor u umnošku.
		 * @return Izračunati umnožak
		 */
		public ComplexPolynomial multiply(ComplexPolynomial q) {
			ComplexPolynomial productPolynom = new ComplexPolynomial((int) (order + q.order + 1));

			for (int i = 0; i <= order; ++i) {
				for (int j = 0; j <= q.order; ++j) {
					productPolynom.factors[i + j] = productPolynom.factors[i + j]
							.add((factors[i].multiply(q.factors[j])));
				}
			}
			return productPolynom;
		}

		/**
		 * Getter za koeficjente polinoma.
		 * 
		 * @return Polje koeficjenata.
		 */
		public Complex[] getFactors() {
			return factors;
		}

		/**
		 * Računa prvu derivaciju polinoma. Za npr. (7+2i)z^3+2z^2+5z+1 vraća
		 * polinom (21+6i)z^2+4z+5.
		 * 
		 * @return Prvu derivaciju polinoma.
		 */
		public ComplexPolynomial derive() {
			// nema potrebe provjeravati diferencijabilnost jer su polinomi
			// klase C^inf
			ComplexPolynomial derivedPolynom = new ComplexPolynomial((int) (order));
			for (int i = 1; i <= order; ++i) {
				derivedPolynom.factors[i - 1] = new Complex(i, 0).multiply(factors[i]);
			}
			return derivedPolynom;
		}

		/**
		 * Računa se vrijednost polinoma u zadanoj točki z.
		 * 
		 * @param z
		 *            točka u kojoj se računa vrijednost polinoma.
		 * @return Tu vrijednost.
		 */
		public Complex apply(Complex z) {
			Complex functionValue = ZERO_COMPLEX;
			for (int i = 0; i <= order; ++i) {
				functionValue = functionValue.add(factors[i].multiply(z.power(i)));
			}
			return functionValue;
		}

		/**
		 * Pametan ispis polinoma.
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			String output = "";
			if (!factors[0].equals(ZERO_COMPLEX)) {
				output += factors[0].toString();
			}
			for (int i = 1; i <= order; ++i) {
				if (!factors[i].equals(ZERO_COMPLEX)) {
					output += " + (" + factors[i].toString() + ")z^" + i;
				}
			}
			return output;
		}
	}

}
