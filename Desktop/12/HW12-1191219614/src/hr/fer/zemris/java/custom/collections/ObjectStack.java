package hr.fer.zemris.java.custom.collections;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementacija klase koja seponaša kao stog.
 * 
 * @author Jure Šiljeg
 * @version 1.0.
 */
public class ObjectStack {

	/**
	 * Instanca ove klase zapravo služi za pohranu elemenata.
	 */
	private List<Object> myColl;

	/**
	 * Konstruktor koji instancira naš objekt.
	 */
	public ObjectStack() {
		myColl = new ArrayList<>();
	}

	/**
	 * Metoda provjerava je li stog prazan.
	 * 
	 * @return true za slučaj da jest, inače false
	 */
	public boolean isEmpty() {
		return myColl.isEmpty();
	}

	/**
	 * Metoda računa broj elemenata u stogu.
	 * 
	 * @return tu duljinu
	 */
	public int size() {
		return myColl.size();
	}

	/**
	 * Metoda koja postavlja element na vrh stoga.
	 * 
	 * @param value
	 *            referenca na taj element
	 * 
	 */
	public void push(Object value) { // provjera za null tamo
		myColl.add(value);
	}

	/**
	 * Metoda uklanja vršni element sa stoga i vraća ga.
	 * 
	 * @return najgornji element
	 */
	public Object pop() {
		if (isEmpty())
			throw new EmptyStackException("Stog je prazan!");

		Object obj = myColl.get(size() - 1);
		myColl.remove(size() - 1);
		return obj;
	}

	/**
	 * Metoda vraća najgornji element, ali ga ne izbacuje iz stoga.
	 * 
	 * @return vršni element.
	 */
	public Object peek() {
		if (isEmpty())
			throw new EmptyStackException("Nema vrha jer je stog prazan!");

		return myColl.get(size() - 1);
	}

	/**
	 * Metoda uklanja sve elemente sa stoga.
	 */
	public void clear() {
		myColl.clear();
	}

}
