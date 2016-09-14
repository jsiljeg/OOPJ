package hr.fer.zemris.java.custom.collections;

/**
 * Implementira se klasa koja reprezentira polje koje moze mijenjati velicinu i
 * prirodne metode nad njom.
 * 
 * @author Jure Šiljeg
 * @version 1.0.
 */
public class ArrayIndexedCollection extends Collection {

	/**
	 * Varijabla koja označava stvaran broj elemenata u polju
	 */
	private int size;

	/**
	 * Varijabla označava koliko ukupno može stati elemenata u polje.
	 */
	private int capacity;

	/**
	 * Varijabla koja označava našu kolekciju - polje.
	 */
	private Object[] elements;

	/**
	 * Defaultni kontruktor koji instancira objekt klase na predefinirane
	 * vrijednosti.
	 */

	public ArrayIndexedCollection() {
		this(16);
	}

	/**
	 * Konstruktor koji instancira objekt klase i postavlja mu zadani maksimalni
	 * kapacitet.
	 * 
	 * @param initialCapacity
	 *            reprezentira maksimalni kapacitet polja objekata veličina mu
	 *            mora biti barem 1.
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		if (initialCapacity < 1)
			throw new IllegalArgumentException();
		this.size = 0;
		this.capacity = initialCapacity;
		this.elements = new Object[this.capacity];
	}

	/**
	 * Konstruktor koji instancira objekt klase sa zadanim kapacitetom i puni
	 * polje zadanim vrijednostima iz col.
	 * 
	 * @param col
	 *            predstavlja kolekciju pomoću koje instanciramo objekt klase
	 * @param initialCapacity
	 *            predstavlja kapacitet novog polja
	 */
	public ArrayIndexedCollection(Collection col, int initialCapacity) {
		// namjera je osigurati svakakvo prebacivanje
		this(initialCapacity);
		while (initialCapacity <= col.size()) {
			initialCapacity *= 2;
			doubleIt();
		}
		System.arraycopy(col.toArray(), 0, elements, 0, col.size());
	}

	/**
	 * Konstruktor koji instancira objekt klase sa defaultnim kapacitetom i puni
	 * polje zadanim vrijednostima iz col.
	 * 
	 * @param col
	 *            predstavlja kolekciju pomoću koje instanciramo objekt klase
	 */
	public ArrayIndexedCollection(Collection col) {
		this(col, 16);
	}

	/**
	 * Metoda koja napravi polje duplo veće duljine, ali sa sačuvanim ne-null
	 * referencama i prethodno uspostavljenim redoslijedom.
	 */
	private void doubleIt() {
		Object[] tmp = new Object[size];
		System.arraycopy(elements, 0, tmp, 0, size);
		capacity *= 2;
		elements = new Object[capacity];
		System.arraycopy(tmp, 0, elements, 0, size);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return size;
	};

	/**
	 * {@inheritDoc} Dodaje se element na prvo prazno mjesto u polju.
	 * 
	 * @param value
	 *            ne-null referenca koja se dodaje
	 */
	@Override
	public void add(Object value) {
		if (value == null)
			throw new IllegalArgumentException("Pokušali ste dodati nul vrijednost! Greška!");
		if (size == capacity) {
			doubleIt();
		}
		elements[size++] = value;
		// O(1)
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Object value) {
		if (value == null)
			throw new IllegalArgumentException("Pokušavate pronaći indeks null vrijednost? Neće ići!");
		for (int i = 0; i < size; ++i) {
			if (elements[i].equals(value))
				return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Object value) {
		if (contains(value)) {
			remove(indexOf(value));
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		Object[] tmp = new Object[size];
		System.arraycopy(elements, 0, tmp, 0, size);
		return tmp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void forEach(Processor processor) {
		for (int i = 0; i < size; i++) {
			processor.process(get(i));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		for (int i = 0; i < size; ++i)
			elements[i] = null;
		size = 0;
	}

	/**
	 * Metoda dohvaca objekt na nekoj poziciji.
	 * 
	 * @param index
	 *            označava mjesto u polju 0..size-1
	 * @return objekt s korektne pozicije
	 */
	public Object get(int index) {
		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException();
		return elements[index];
		// O(1)
	}

	/**
	 * Metoda koja umeće objekt na zadano mjesto bez da se ikakvi podaci izgube,
	 * a prethodno uspostavljeni redoslijed elemenata naruši.
	 * 
	 * @param value
	 *            ne-null vrijednost elementa kojeg želimo umetnuti u polje.
	 * @param position
	 *            lokacija umetanja 0..size
	 */
	public void insert(Object value, int position) {
		if (position < 0 || position > size) {
			throw new IndexOutOfBoundsException();
		}

		if (value == null) {
			throw new IllegalArgumentException("Pokušavate dodati null vrijednost? Neće ići!");
		}

		if (position == size)
			add(value); // kad ne moram pomicat elemente
		else {
			if (size == capacity)
				doubleIt();
			for (int i = size; i > position; i--) {
				elements[i] = elements[i - 1];
			}
			elements[position] = value;
			size++;
		}
		// O(n)
	}

	/**
	 * Metoda dohvaća indeks prvog poljavljivanja željenog elementa u polju.
	 * 
	 * @param value
	 *            ne-null vrijednost reference na element čiju lokaciju tražimo.
	 * @return tražena lokacija unutar našeg polja
	 */
	public int indexOf(Object value) {
		if (value == null)
			throw new IllegalArgumentException("Pokušavate pronaći indeks null vrijednost? Neće ići!");
		for (int i = 0; i < size; i++) {
			if (this.elements[i].equals(value))
				return i;
		}
		return -1;
		// O(size)
	}

	/**
	 * Metoda uklanja element sa specificne lokacije u polju uz ocuvanje
	 * sekvencijalno zapisanih ne-null elemenata bez "praznih mjesta" u polju
	 * koje je moglo brisanje prouzrokovati.
	 * 
	 * @param index
	 *            lokacija s koje se uklanja element 0..size-1
	 */
	public void remove(int index) {
		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException();
		for (int i = index; i < size - 1; i++)
			elements[i] = elements[i + 1];
		elements[--size] = null;
	}

}
