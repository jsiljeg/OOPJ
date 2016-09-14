package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Demonstracijski program za iscrtavanje stupčastog grafa.
 * 
 * @author Jure Šiljeg
 */
public class BarChartDemo extends JFrame {

	/** Serijski broj ID. */
	private static final long serialVersionUID = 1L;
	/** Širina stupčastog grafa. */
	public static final int BAR_CHART_WIDTH = 700;
	/** Visina stupčastog grafa. */
	public final int BAR_CHART_HEIGHT = 450;
	/** Pozadinska boja. */
	public final static Color BACKGROUND = new Color(189, 151, 186);

	/**
	 * Defaultni konstruktor za čitav prikazivi ekran.
	 */
	public BarChartDemo() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(BAR_CHART_WIDTH, BAR_CHART_HEIGHT);
		setTitle("Bar chart");
	}

	/**
	 * Osnovna metoda za izvođenje demonstracijskog programa.
	 * 
	 * @param args
	 *            srgumenti komandne linije. Očekuje se staza do datoteke.
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Nedozvoljen broj argumenata ste unijeli! Treba biti 1 argument - staza do datoteke!");
			return;
		}

		initGui(args[0]);

	}

	/**
	 * Obavlja se inicijalizacija grafičkog sučelja.
	 * 
	 * @param pathFromArgs
	 *            staza do datoteke.
	 */
	private static void initGui(String pathFromArgs) {
		BarChart model = parseFileRepresentingBarChart(pathFromArgs);

		SwingUtilities.invokeLater(() -> {
			JFrame frame = new BarChartDemo();

			Container cp = frame.getContentPane();
			cp.setLayout(new BorderLayout());
			cp.setBackground(BACKGROUND);

			JLabel path = new JLabel(pathFromArgs.trim());
			frame.add(path, BorderLayout.PAGE_START);
			path.setHorizontalAlignment(SwingConstants.CENTER);

			BarChartComponent component = new BarChartComponent(model);
			frame.add(component);
			frame.setVisible(true);
		});

	}

	/**
	 * Parsira se ulazna datoteka i pune se potrebne komponente za iscrtavanje.
	 * 
	 * @param fileName
	 *            Staza do ulazne datoteke.
	 * @return Instancu stupčastog grafa. Baca se iznimka u slučaju nemogućnosti
	 *         parsiranja ili grješke u datoteci.
	 */
	private static BarChart parseFileRepresentingBarChart(String fileName) {
		File testExample = new File(fileName);
		if (!testExample.exists()) {
			System.out.println("Unijeli ste nepostojeću stazu do datoteke! Pokušajte ponovo!");
			System.exit(-1);
		}

		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				new BufferedInputStream(new FileInputStream(fileName)), StandardCharsets.UTF_8));) {

			List<XYValue> listOfXYValues = new ArrayList<>();
			String xAxisName = br.readLine().trim();
			String yAxisName = br.readLine().trim();
			String[] values = br.readLine().trim().split(" ");

			for (String val : values) {
				String[] arg = val.split(",");
				listOfXYValues.add(new XYValue(Integer.parseInt(arg[0].trim()), Integer.parseInt(arg[1].trim())));
			}

			int minY = Integer.parseInt(br.readLine().trim());
			int maxY = Integer.parseInt(br.readLine().trim());
			int offset = Integer.parseInt(br.readLine().trim());

			return new BarChart(listOfXYValues, xAxisName, yAxisName, minY, maxY, offset);

		} catch (NumberFormatException | PatternSyntaxException | IOException e) {
			System.out.println("Pogreška pri parsiranju redaka datoteke!");
			System.exit(-1);
		}
		return null;
	}
}
