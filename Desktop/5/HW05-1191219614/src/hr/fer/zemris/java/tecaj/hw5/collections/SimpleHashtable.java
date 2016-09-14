package hr.fer.zemris.java.tecaj.hw5.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Razred koji implementira neke osnovne funkcionalnosti hash tablica.
 * 
 * @author Jure Šiljeg
 * @version 1.0.
 * @param <K>
 *            tip elementa
 * @param <V>
 *            vrijednost elementa
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {

	/** Variabla za defaultnu vrijednost hash tablice. */
	private final static int defaultSize = 16;

	/** Interna reprezentacija hash tablice. */
	private TableEntry<K, V>[] table;
	/** Broj elemenata u tablici. */
	private int size;

	private int modificationCount;

	/**
	 * Konstruktor koji inicijalizira čitavu tablicu na specifičnu veličinu
	 * slotova kojih mora biti barem 1. Dodatno, postavlja se na veličinu koja
	 * je jednaka vrijednosti prve potencije broja 2 koja je velika barem koliko
	 * i proslijeđeni parametar.
	 * 
	 * @param capacity
	 *            broj na koji barem treba postaviti veličinu, a sve u skladu sa
	 *            specifikacijom metode
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int capacity) {
		if (capacity < 1) {
			throw new IllegalArgumentException("Mora biti unesena veličina barem 1");
		}
		int indicator = 1;
		while (capacity > indicator) {
			indicator *= 2;
		}
		this.size = 0;
		this.modificationCount = 0;
		table = (TableEntry<K, V>[]) new TableEntry[indicator];
	}

	/**
	 * Konstruktor koji po defaultu postavlja veličinu tablice na vrijednost
	 * jedine statičke varijable razreda.
	 */
	public SimpleHashtable() {
		this(defaultSize);
	}

	/**
	 * Metoda koja koristi {@link #basicPutOnEnd <K, V> basicPutOnEnd} i
	 * {@link #resizeOldTable() resizeOldTable} metode za dodavanje.
	 * 
	 * @param key
	 *            ključ koji ne smije biti null vrijednost.
	 * @param value
	 *            vrijednost elementa koju želimo postaviti.
	 */
	public void put(K key, V value) {
		if (size >= 0.75 * table.length) {
			resizeOldTable();
		}
		basicPutOnEnd(key, value);
	}

	/**
	 * Metoda koja dodaje par na korektno mjesto u hash tablicu na kraj slota.
	 * Ne stvara se novi element ukoliko ključ već postoji, nego se samo
	 * ažurira.
	 * 
	 * @param key
	 *            ključ koji ne smije biti null vrijednost.
	 * @param value
	 *            vrijednost elementa koju želimo postaviti.
	 */
	private void basicPutOnEnd(K key, V value) {
		if (key == null) {
			throw new IllegalArgumentException("Ne smije se proslijediti null key!");
		}
		int slot = hash(key);

		TableEntry<K, V> listNode = table[slot];
		TableEntry<K, V> prevNode = table[slot];

		while (listNode != null) {
			// traži jel node već postoji
			if (listNode.key.equals(key)) {
				break;
			}
			prevNode = listNode;
			listNode = listNode.next;
		}

		if (listNode != null) { // nije null, nego već postoji taj key
			listNode.value = value;
		} else { // dodavanje se treba napraviti
			TableEntry<K, V> newNode = new TableEntry<K, V>(key, value, table[slot]);
			if (prevNode == null) {
				table[slot] = newNode;
			} else {
				prevNode.next = newNode;
			}
			++size;
			++modificationCount;
		}
	}

	/**
	 * Metoda koja po potrebi mijenja samu veličinu tablice u skladu s
	 * propisanim tehnikama za izbegavanje preljeva u tablicama raspršenog
	 * adresiranja. Brine o pravilnom raspršivanju i u novokreiranoj tablici.
	 */
	@SuppressWarnings("unchecked")
	private void resizeOldTable() {

		TableEntry<K, V>[] oldTable = table;

		table = (TableEntry<K, V>[]) new TableEntry[2 * table.length];
		size = 0;

		for (int i = 0; i < oldTable.length; ++i) {
			TableEntry<K, V> listNode = oldTable[i];
			while (listNode != null) {
				basicPutOnEnd(listNode.key, listNode.value);
				listNode = listNode.next;
			}
		}
	}

	/**
	 * Metoda računa hash vrijednost za zadani ključ.
	 * 
	 * @param key
	 *            ključ za koji se provjera vrši.
	 * @return slot u koji treba ubaciti naš uređeni par
	 */
	private int hash(K key) {
		return Math.abs(key.hashCode()) % table.length;
	}

	/**
	 * Metoda pretražuje vrijednost elementa za zadani ključ.
	 * 
	 * @param key
	 *            ključ koji smije biti null.
	 * @return vraća vrijednost ili null ako nije pronađen.
	 */
	public V get(Object key) {
		if (key == null) {
			return null;
		}
		@SuppressWarnings("unchecked")
		int slot = hash((K) key);
		TableEntry<K, V> listNode = table[slot];

		while (listNode != null) {
			if (listNode.key.equals(key)) {
				return listNode.value;
			}
			listNode = listNode.next;
		}
		return null;
	}

	/**
	 * Metoda kojom provjeravamo popunjenost tablice.
	 * 
	 * @return broj elemenata u tablici.
	 */
	public int size() {
		return size;
	}

	/** Metoda koja izbriše sve uređene parove iz kolekcije. */
	@SuppressWarnings("unchecked")
	public void clear() {
		table = (TableEntry<K, V>[]) new TableEntry[table.length];
		size = 0;
		++modificationCount;
	}

	/**
	 * Vrši se provjera postoji li element sa zadanim ključem u tablici.
	 * 
	 * @param key
	 *            ključ čije postojanje pretražujemo.
	 * @return istinu ako postoji, inače laž.
	 */
	public boolean containsKey(Object key) {
		if (key == null) {
			return false;
		}
		if (get(key) != null) {
			return true;
		}
		return false;
	}

	/**
	 * Vrši se provjera postoji li element sa zadanom vrijednosti u tablici.
	 * 
	 * @param value
	 *            vrijednost elementa koju potražujemo.
	 * @return istinu ako postoji, inače laž.
	 */
	public boolean containsValue(Object value) {

		for (int i = 0; i < table.length; ++i) {
			TableEntry<K, V> listNode = table[i];
			while (listNode != null) {
				if (listNode.value == value) {
					return true;
				}
				listNode = listNode.next; // Move on to next node in the list.
			}
		}
		return false;
	}

	/**
	 * Uklanja element specificiran jedinstvenim ključem iz tablice.
	 * 
	 * @param key
	 *            identifikator po kojem pretražujemo i vršimo radnju
	 *            uklanjanja.
	 */
	public void remove(Object key) {

		if (key == null) {
			return;
		}

		@SuppressWarnings("unchecked")
		int slot = hash((K) key);

		// key se uopće ne pojavljuje u tablici
		if (table[slot] == null) {
			return;
		}

		// uklanjamo s početka
		if (table[slot].key.equals(key)) {
			table[slot] = table[slot].next;
			--size;
			++modificationCount;
			return;
		}

		// ne-početak liste
		TableEntry<K, V> prev = table[slot];
		TableEntry<K, V> curr = prev.next;
		while (curr != null && !curr.key.equals(key)) {
			prev = curr;
			curr = curr.next;
		}

		// ako je cur == null ništa ne radim, a ako je curr != null...
		if (curr != null) {
			prev.next = curr.next;
			--size;
			++modificationCount;
		}
	}

	/**
	 * Provjerava se je li tablica prazna.
	 * 
	 * @return istinu ako jest, inače laž.
	 */
	public boolean isEmpty() {
		return size == 0 ? true : false;
	}

	/**
	 * Metoda koja služi za specifičan ispis čitave tablice.
	 * 
	 * @return String koji sadrži traženi ispis.
	 */
	public String toString() {
		int count = 0;
		String output = "[";
		for (int i = 0; i < table.length; i++) {
			TableEntry<K, V> start = table[i];
			while (start != null) {
				++count;
				if (count == size) {
					output += start.key + "=" + start.value;
				} else {
					output += start.key + "=" + start.value + ", ";
				}
				start = start.next;
			}
		}
		return output + "]";
	}

	/**
	 * Metoda tvornica koja proizvodi iteratore koje potom koristimo za
	 * obilazak. po tablici.
	 * 
	 * @return iterator koji služi za obilazak.
	 */
	public Iterator<SimpleHashtable.TableEntry<K, V>> iterator() {
		return new IteratorImpl();
	}

	/**
	 * Ugniježđeni razred kojim ostvarujemo iterator za kretanje po rablici.
	 * 
	 * @author Jure Šiljeg
	 * @version 1.0.
	 */
	private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K, V>> {

		/**
		 * Varijabla koja će nam služiti kao indikator smijemo li pozvati remove
		 * ili ne.
		 */
		private boolean isRemoved = false;
		/** Varijabla pamti u kojem smo slotu tablice trenutno. */
		private int startSlot = 0;
		/** Varijabla za kontrolu kad smo završili obilazak tablice. */
		private int currSize = 0;
		/**
		 * Varijabla za kontrolu potencijalnog modificiranja izvana dok traje
		 * iteriranje.
		 */
		private int numberOfChanges = modificationCount;
		/** Trenutni čvor. */
		private TableEntry<K, V> listNode;
		/** Čvor prije prethodnog. */
		private TableEntry<K, V> prev;

		/**
		 * Metoda provjerava postoji li sljedeći član u tablici.
		 * 
		 * @return istinu ako postoji, inače laž.
		 */
		public boolean hasNext() {
			return currSize != size;
		}

		/**
		 * Pronalazi sljedeći element u iteriranju.
		 * 
		 * @return pronađeni element.
		 */
		public SimpleHashtable.TableEntry<K, V> next() {
			if (!hasNext()) {
				throw new NoSuchElementException("Nema više elemenata");
			}
			if (numberOfChanges != modificationCount) {
				throw new ConcurrentModificationException("Modificira se izvana!");
			}
			++currSize;

			// pomak do početno nepraznih slotova
			for (int i = startSlot; i < table.length; i++) {
				if (listNode != null) {
					break;
				}
				listNode = table[i];
				if (listNode == null) {
					++startSlot;
					continue;
				} else {
					break;
				}
			} // ovdje mi listNode != null
			prev = listNode;
			listNode = listNode.next;
			if (listNode == null) { // došao sam do zadnjeg čvora u slotu, pa se
									// pomakni na idući slot
				++startSlot;
			}
			isRemoved = false;
			return prev;
		}

		/**
		 * Uklanja iz tablice zadnji element vraćen next() metodom. Može biti
		 * pozvana samo jednom na svaki pozvani next(). U slučaju da su dva ili
		 * više remove() poziva bez poziva next() između - baca iznimku.
		 */
		public void remove() {
			if (isRemoved) {
				throw new IllegalStateException("Ne smijete 2 poziva removea u nizu imati!");
			}
			K key = (K) prev.getKey();
			SimpleHashtable.this.remove(key);
			numberOfChanges = modificationCount;
			isRemoved = true;
		}
	}

	/**
	 * Razred koji predstavlja jedan slot tablice i koji se implementira
	 * jednostruko povezanom listom.
	 * 
	 * @author Jure Šiljeg
	 * @version 1.0.
	 * @param <K>
	 *            tip elementa
	 * @param <V>
	 *            vrijednost elementa
	 */
	public static class TableEntry<K, V> {
		/** Tip elementa */
		private K key;
		/** Vrijednost elementa */
		private V value;
		/** Pokazivač na sljedeći element vezale liste. */
		private TableEntry<K, V> next;

		/**
		 * Konstruktor koji kreira čvor.
		 * 
		 * @param key
		 *            ključ u čvoru.
		 * @param value
		 *            vrijednost koju ključ ima u čvoru.
		 * @param first
		 *            pokazivač na element čvora.
		 */
		public TableEntry(K key, V value, TableEntry<K, V> first) {
			this.key = key;
			this.value = value;
			// iz specifikacije sam vidio da treba 3 elementa sadržavati, no ja
			// ne vidim svrhu ovog, pa stoga nije ni implementirano
		}

		/**
		 * Getter za vrijednost čvora.
		 * 
		 * @return tu vrijednost.
		 */
		public V getValue() {
			return value;
		}

		/**
		 * Setter za vrijednost čvora.
		 * 
		 * @param value
		 *            ta vrijednost.
		 */
		public void setValue(V value) {
			this.value = value;
		}

		/**
		 * Getter za ključ čvora.
		 * 
		 * @return taj ključ
		 */
		public K getKey() {
			return key;
		}

	}
}
