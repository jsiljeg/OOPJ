package hr.fer.zemris.java.custom.collections;

/**
 * Klasa koja predstavlja neku generalnu kolekciju objekata.
 * 
 * @author Jure Šiljeg
 * @version 1.0.
 */
public class Collection {

	/**
	 * Defaultni konstruktor koji ne radi ništa.
	 */
	protected Collection() {

	}

	/**
	 * Metoda koja provjerava je li kolekcija prazna.
	 * 
	 * @return true ako je prazna, false inače
	 */
	public boolean isEmpty() {
		if (this.size() == 0)
			return true;
		return false;
	}

	/**
	 * Metoda koja računa broj pohranjenih objekata u kolekciji.
	 * 
	 * @return broj elemenata iz kolekcije
	 */
	public int size() {
		return 0;
	}

	/**
	 * Metoda koja zadani objekt ubacuje u kolekciju.
	 * 
	 * @param value
	 *            objekt koji namjeravamo ubaciti
	 */
	public void add(Object value) {

	}

	/**
	 * Metoda provjerava nalazi li se neki objekt u kolekciji.
	 * 
	 * @param value
	 *            referenca na objekt za koji se provjera vrši.
	 * @return true ako se nalazi unutar, inače false
	 */
	public boolean contains(Object value) {
		return false; // it is OK to ask if collection contains null?
	}

	/**
	 * Metoda koja uklanja neko pojavljivanje elementa u kolekciji.
	 * 
	 * @param value
	 *            referenca na objekt za koji se provjera vrši.
	 * @return true ako je uspješno uklonjeno, false inače
	 */
	public boolean remove(Object value) {
		return false;
	}

	/**
	 * Metoda koja alocira neko polje odgovarajuće veličine i popuni ga
	 * odgovarajućim elementima kolekcije.
	 * 
	 * @return to novo kreirano polje
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Metoda koja poziva drugu metodu nad svakim elementom kolekcije
	 * 
	 * @param processor
	 *            referenca na tip podatka
	 */
	public void forEach(Processor processor) {

	}

	/**
	 * Metoda koja u sebe dodaje sve elemente zadane kolekcije.
	 * 
	 * @param other
	 *            referenca na zadanu kolekciju iz koje se dodaje.
	 */
	public void addAll(Collection other) {
		if (other == null) {
			throw new IllegalArgumentException();
		}
		class LocalProcessor extends Processor {
			public void process(Object value) {
				add(value);
			}
		}
		LocalProcessor locProc = new LocalProcessor();
		other.forEach(locProc);
	}

	/**
	 * Metoda ispuni polje null-referencama (ekvivalent pražnjenju polja).
	 */
	public void clear() {

	}

}
