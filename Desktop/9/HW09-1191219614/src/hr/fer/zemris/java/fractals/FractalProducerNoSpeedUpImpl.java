package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.Newton.Complex;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;

/**
 * Razred koji služi za demonstraciju ne-paraleliziranog rada pri renderiranju.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class FractalProducerNoSpeedUpImpl implements IFractalProducer {

	/**
	 * @see hr.fer.zemris.java.fractals.viewer.IFractalProducer#produce(double,
	 *      double, double, double, int, int, long,
	 *      hr.fer.zemris.java.fractals.viewer.IFractalResultObserver)
	 */
	@Override
	public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height, long requestNo,
			IFractalResultObserver observer) {
		// Inicijalizacija varijabli
		int offset = 0;
		int maxIter = 16 * 16 * 16;
		short[] data = new short[width * height];
		double convergenceTreshold = 0.001;
		double rootTreshold = 0.002;

		// Algoritam
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Complex c = mapToComplexPlain(x, y, 0, width, 0, height, reMin, reMax, imMin, imMax);
				Complex zn = c;

				int iter = 0;
				Complex zn1;
				double module;
				do {
					Complex numerator = Newton.polynomial.apply(zn);
					Complex denominator = Newton.polynomial.toComplexPolynom().derive().apply(zn);
					Complex fraction = numerator.divide(denominator);

					zn1 = zn.sub(fraction);
					module = zn1.sub(zn).module();
					zn = zn1;
					iter++;
				} while (module > convergenceTreshold && iter < maxIter);

				short index = (short) (Newton.polynomial.indexOfClosestRootFor(zn1, rootTreshold) + 1);

				data[offset++] = (index == -1) ? 0 : index;

			}
		}

		observer.acceptResult(data, (short) (Newton.polynomial.toComplexPolynom().order() + 1), requestNo);
	}

	/**
	 * Mapiranje kompleksne ravnine.
	 * 
	 * @param x
	 *            Trenutna x koordinata.
	 * @param y
	 *            Trenutna y koordinata.
	 * @param xMin
	 *            Minimalna vrijednost kad se ide po x-osi.
	 * @param xMax
	 *            Maksimalna vrijednost kad se ide po x-osi.
	 * @param yMin
	 *            Minimalna vrijednost kad se ide po y-osi.
	 * @param yMax
	 *            Maksimalna vrijednost kad se ide po y-osi.
	 * @param reMin
	 *            Minimalna realna vrijednost.
	 * @param reMax
	 *            Maksimalna realna vrijednost.
	 * @param imMin
	 *            Minimalna imaginarna vrijednost.
	 * @param imMax
	 *            Maksimalna imaginarna vrijednost.
	 * @return Odgovarajući inicijalni kompleksni broj koji se promatra za
	 *         iteriranje.
	 */
	private Complex mapToComplexPlain(int x, int y, int xMin, int xMax, int yMin, int yMax, double reMin, double reMax,
			double imMin, double imMax) {
		double cre = x / (xMax - 1.0) * (reMax - reMin) + reMin;
		double cim = (yMax - 1.0 - y) / (yMax - 1) * (imMax - imMin) + imMin;
		return new Complex(cre, cim);
	}

}
