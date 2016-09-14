package hr.fer.zemris.java.simplecomp.impl;

import hr.fer.zemris.java.simplecomp.models.Registers;

/**
 * Razred koji implementira sučelje koje simulira registre jednostavnog
 * računala.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class RegistersImpl implements Registers {
	/** Registri računala. */
	private Object[] registerValue;
	/** Programsko brojilo. */
	private int programCounter;
	/** Zastavica za provjeru jednakosti registara. */
	private boolean flag;

	/**
	 * Metoda provjerava valjanost indeksa registra.
	 * 
	 * @param index
	 *            proslijeđeni indeks.
	 */
	private void checkIndex(int index) {
		if (index < 0 || index >= registerValue.length) {
			throw new IndexOutOfBoundsException(
					"Indeks mora biti u skupu [0.." + (registerValue.length - 1) + "], a vi ste unijeli " + index);
		}
	}

	/**
	 * Konstruktor koji inicijalizira registre na određenu veličinu.
	 * 
	 * @param regsLen
	 *            ta veličina.
	 */
	public RegistersImpl(int regsLen) {
		registerValue = new Object[regsLen];
	}

	@SuppressWarnings("javadoc")
	@Override
	public Object getRegisterValue(int index) {
		checkIndex(index);
		return registerValue[index];
	}

	@SuppressWarnings("javadoc")
	@Override
	public void setRegisterValue(int index, Object value) {
		checkIndex(index);
		registerValue[index] = value;
	}

	@SuppressWarnings("javadoc")
	@Override
	public int getProgramCounter() {
		return programCounter;
	}

	@SuppressWarnings("javadoc")
	@Override
	public void setProgramCounter(int value) {
		programCounter = value;
	}

	@SuppressWarnings("javadoc")
	@Override
	public void incrementProgramCounter() {
		programCounter++;

	}

	@SuppressWarnings("javadoc")
	@Override
	public boolean getFlag() {
		return flag;
	}

	@SuppressWarnings("javadoc")
	@Override
	public void setFlag(boolean value) {
		flag = value;
	}

}
