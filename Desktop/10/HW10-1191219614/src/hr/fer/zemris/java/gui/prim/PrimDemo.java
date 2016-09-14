package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Razred koji služi za demosnraciju slijednog ispisa prostih brojeva na klik
 * gumba koji se nalazi na dnu okvira. Ispisuje se paralelno u dvije liste i
 * imamo mogućnost scrollanja.
 * 
 * @author Jure Šiljeg
 *
 */
public class PrimDemo extends JFrame {
	/** Serijski broj ID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Defaultni konstruktor koji postavlja ime okvira i poziva metodu za
	 * inicijalizaciju grafičkog sučelja.
	 */
	public PrimDemo() {
		setTitle("Prosti brojevi");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		initGUI();
	}

	/**
	 * Razred koji implementira sučelje ListModel. Služimo se samo Integerima
	 * zbog prirode zadatka.
	 * 
	 * @author Jure Šiljeg
	 *
	 */
	public class PrimListModel implements ListModel<Integer> {
		/** Lista dosadašnjih prostih brojeva- */
		private List<Integer> primeNumbers;
		/** Lista promatrača. */
		private List<ListDataListener> observers;

		/**
		 * Defaultni konstruktor koji inicijalizira privatne članske varijable.
		 * U listu koja pamti proste brojeve se inicijalno stavalj broj 1 koji
		 * će se prvi ispisati.
		 */
		public PrimListModel() {
			primeNumbers = new ArrayList<>();
			observers = new ArrayList<>();
			primeNumbers.add((Integer) 1);
		}

		/**
		 * @see javax.swing.ListModel#addListDataListener(javax.swing.event.ListDataListener)
		 */
		@Override
		public void addListDataListener(ListDataListener l) {
			observers.add(l);
		}

		/**
		 * @see javax.swing.ListModel#getElementAt(int)
		 */
		@Override
		public Integer getElementAt(int index) {
			return primeNumbers.get(index);
		}

		/**
		 * @see javax.swing.ListModel#getSize()
		 */
		@Override
		public int getSize() {
			return primeNumbers.size();
		}

		/**
		 * @see javax.swing.ListModel#removeListDataListener(javax.swing.event.ListDataListener)
		 */
		@Override
		public void removeListDataListener(ListDataListener l) {
			observers.remove(l);

		}

		/**
		 * Metoda koja generira sljedeći broj u nizu svih prostih brojeva.
		 * 
		 * @param lastPrime
		 *            posljednji od kojeg na dalje treba gledati.
		 * @return sljedeći prosti broj.
		 */
		private Integer generateNextPrime(Integer lastPrime) {
			int nextPossible = (int) lastPrime;
			while (true) {
				if (isPrime(nextPossible)) {
					return (Integer) nextPossible;
				} else
					++nextPossible;
			}
		}

		/**
		 * Metoda provjerava eventualnu prostost broja.
		 * 
		 * @param nextPossible
		 *            broj čija nas prostost zanima.
		 * @return <code> true </code> ako je promatrani broj prost, inače
		 *         <code> false </code>.
		 */
		private boolean isPrime(int nextPossible) {
			if (nextPossible == 2) {
				return true;
			}
			if (nextPossible % 2 == 0)
				return false;

			int border = (int) Math.sqrt(nextPossible);
			for (int i = 3; i <= border; i += 2) {
				if (nextPossible % i == 0) {
					return false;
				}
			}
			return true;
		}

		/**
		 * Metoda generira sljedeći prosti broj, obavještava sve promatrače i
		 * sprema pronađeni prosti broj u internu listu.
		 */
		private void next() {
			int pos = primeNumbers.size();
			Integer lastPrime = primeNumbers.get(pos - 1);
			primeNumbers.add(generateNextPrime(lastPrime + 1));

			ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, pos, pos);
			for (ListDataListener l : observers) {
				l.intervalAdded(event);
			}
		}

	}

	/**
	 * Metoda koja služi za inicijalizaciju grafičkog sučelja.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		PrimListModel model = new PrimListModel();
		JList<Integer> leftList = new JList<>(model);
		JList<Integer> rgihtList = new JList<>(model);

		JPanel panel = new JPanel(new GridLayout(1, 0));
		panel.add(new JScrollPane(leftList));
		panel.add(new JScrollPane(rgihtList));
		JButton next = new JButton("Sljedeći");

		next.addActionListener(e -> {
			model.next();
		});

		cp.add(panel, BorderLayout.CENTER);
		cp.add(next, BorderLayout.PAGE_END);
	}

	/**
	 * Osnovna metoda koja se pokreće prilikom pokretanja programa.
	 * 
	 * @param args
	 *            argumenti komandne linije (nisu predviđeni zadatkom).
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new PrimDemo();
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.pack();
			frame.setVisible(true);
		});
	}

}
