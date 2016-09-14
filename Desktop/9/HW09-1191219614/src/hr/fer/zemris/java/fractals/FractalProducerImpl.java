package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import hr.fer.zemris.java.fractals.Newton.Complex;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;

/**
 * Razred koji služi za demonstraciju paraleliziranog rada pri renderiranju.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */

public class FractalProducerImpl implements IFractalProducer {
	/** Broj dostupnih procesora. */
	private int numberOfProcs;
	/** Bazen dretvi. */
	private ExecutorService pool;

	/**
	 * Defaultni kontruktor za producenta fraktala na paralelizirani način.
	 */
	public FractalProducerImpl() {
		this.numberOfProcs = Runtime.getRuntime().availableProcessors();
		this.pool = Executors.newFixedThreadPool(numberOfProcs, new DaemonicThreadFactory());
	}

	/**
	 * Razred koji proizvodi demonske dretve. Osigurava terminiranje programa
	 * kad se GUI ugasi.
	 * 
	 * @author Jure Šiljeg
	 * @version 1.0
	 */
	private static class DaemonicThreadFactory implements ThreadFactory {

		/**
		 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
		 */
		@Override
		public Thread newThread(Runnable r) {
			Thread daemonicThread = new Thread(r);
			daemonicThread.setDaemon(true);
			return daemonicThread;
		}

	}

	/**
	 * Razred koji obavlja izračun posla.
	 * 
	 * @author Jure Šiljeg
	 * @version 1.0
	 */
	public static class JobCalculation implements Callable<Void> {
		/** Minimalna realna vrijednost. */
		double reMin;
		/** Maksimalna realna vrijednost. */
		double reMax;
		/** Minimalna imaginarna vrijednost. */
		double imMin;
		/** Maksimalna imaginarna vrijednost. */
		double imMax;
		/** Širina ekrana. */
		int width;
		/** Visina ekrana. */
		int height;
		/** Minimalna vrijednost kad se ide po y-osi. */
		int yMin;
		/** Maksimalna vrijednost kad se ide po y-osi. */
		int yMax;
		/** Maksimalan broj iteracija. */
		int maxIter;
		/**
		 * Polje s podacima. Čuvaju se indeksi korijena kojoj promatrana
		 * kompleksna točka c konvergira ili 0 ako ne konvergira.
		 */
		short[] data;
		/**
		 * Čuva se najveća dozvoljena udaljenost kompleksnih brojeva iz
		 * iteracija za terminalni uvjet. Iterira se dok se udaljenost između
		 * brojeva u iteracijama ne spusti ispod ove vrijednosti.
		 */
		double convergenceTreshold;
		/**
		 * Maksimalna dozvoljena udaljenost od korijena da točku c proglasimo da
		 * konvergira u korijen.
		 */
		double rootTreshold;

		/**
		 * Konstruktor za razred koji računa poslove.
		 * 
		 * @param reMin
		 *            Minimalna realna vrijednost.
		 * @param reMax
		 *            Maksimalna realna vrijednost.
		 * @param imMin
		 *            Minimalna imaginarna vrijednost.
		 * @param imMax
		 *            Maksimalna imaginarna vrijednost.
		 * @param width
		 *            Širina ekrana.
		 * @param height
		 *            Visina ekrana.
		 * @param yMin
		 *            Minimalna vrijednost kad se ide po y-osi.
		 * @param yMax
		 *            Maksimalna vrijednost kad se ide po y-osi.
		 * @param maxIter
		 *            Maksimalan broj iteracija.
		 * @param data
		 *            Polje s podacima. Čuvaju se indeksi korijena kojoj
		 *            promatrana kompleksna točka c konvergira ili 0 ako ne
		 *            konvergira.
		 * @param convergenceTreshold
		 *            Čuva se najveća dozvoljena udaljenost kompleksnih brojeva
		 *            iz iteracija za terminalni uvjet. Iterira se dok se
		 *            udaljenost između brojeva u iteracijama ne spusti ispod
		 *            ove vrijednosti.
		 * @param rootTreshold
		 *            Maksimalna dozvoljena udaljenost od korijena da točku c
		 *            proglasimo da konvergira u korijen.
		 */
		public JobCalculation(double reMin, double reMax, double imMin, double imMax, int width, int height, int yMin,
				int yMax, int maxIter, short[] data, double convergenceTreshold, double rootTreshold) {
			super();
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.maxIter = maxIter;
			this.data = data;
			this.convergenceTreshold = convergenceTreshold;
			this.rootTreshold = rootTreshold;
		}

		/**
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public Void call() {

			int offset = yMin * width;

			// Algoritam
			// <= ?
			for (int y = yMin; y <= yMax; y++) {
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

			return null;
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
		private Complex mapToComplexPlain(int x, int y, int xMin, int xMax, int yMin, int yMax, double reMin,
				double reMax, double imMin, double imMax) {
			double cre = x / (xMax - 1.0) * (reMax - reMin) + reMin;
			double cim = (yMax - 1.0 - y) / (yMax - 1) * (imMax - imMin) + imMin;
			return new Complex(cre, cim);
		}
	}

	/**
	 * @see hr.fer.zemris.java.fractals.viewer.IFractalProducer#produce(double,
	 *      double, double, double, int, int, long,
	 *      hr.fer.zemris.java.fractals.viewer.IFractalResultObserver)
	 */
	@Override
	public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height, long requestNo,
			IFractalResultObserver observer) {
		System.out.println("Zapocinjem izracun...");

		short[] data = new short[width * height];
		int maxIter = 16 * 16 * 16;
		double convergenceTreshold = 0.001;
		double rootTreshold = 0.002;

		final int numberOfTracks = 8 * numberOfProcs;
		int numberYPerTrack = height / numberOfTracks;

		List<Future<Void>> results = new ArrayList<>();

		for (int i = 0; i < numberOfTracks; i++) {
			int yMin = i * numberYPerTrack;
			int yMax = (i + 1) * numberYPerTrack - 1;
			if (i == numberOfTracks - 1) {
				yMax = height - 1;
			}
			JobCalculation posao = new JobCalculation(reMin, reMax, imMin, imMax, width, height, yMin, yMax, maxIter,
					data, convergenceTreshold, rootTreshold);
			results.add(pool.submit(posao));
		}
		for (Future<Void> posao : results) {
			try {
				posao.get();
			} catch (InterruptedException | ExecutionException e) {
			}
		}

		System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
		observer.acceptResult(data, (short) (Newton.polynomial.toComplexPolynom().order() + 1), requestNo);
	}

}
