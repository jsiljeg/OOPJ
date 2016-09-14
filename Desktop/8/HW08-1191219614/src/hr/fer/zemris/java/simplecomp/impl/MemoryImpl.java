package hr.fer.zemris.java.simplecomp.impl;

import hr.fer.zemris.java.simplecomp.models.Memory;

/**
 * Razred koji implementira sučelje koje simulira memoriju računala.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class MemoryImpl implements Memory {
	/** Memorijske lokacije. */
	private Object[] location;

	/**
	 * Metoda provjerava valjanost indeksa memorije.
	 * 
	 * @param index
	 *            proslijeđeni indeks.
	 */
	private void checkIndex(int index) {
		if (index < 0 || index >= location.length) {
			throw new IndexOutOfBoundsException(
					"Indeks mora biti u skupu [0.." + (location.length - 1) + "], a vi ste unijeli " + index);
		}
	}

	/**
	 * Konstruktor koji inicijalizira memoriju na određenuveličinu.
	 * 
	 * @param size
	 *            ta veličina.
	 */
	public MemoryImpl(int size) {
		location = new Object[size];
	}

	@SuppressWarnings("javadoc")
	@Override
	public void setLocation(int location, Object value) {
		checkIndex(location);
		this.location[location] = value;
	}

	@SuppressWarnings("javadoc")
	@Override
	public Object getLocation(int location) {
		checkIndex(location);
		return this.location[location];
	}

}
