package hr.fer.zemris.java.simplecomp.impl;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Memory;
import hr.fer.zemris.java.simplecomp.models.Registers;

/**
 * Implementacija razreda koji simulira računalo.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class ComputerImpl implements Computer {

	/** Registri računala. */
	private Registers registers;
	/** Memorija računala. */
	private Memory memory;

	/**
	 * Konstruktor koji inicijalizira broj registara i količinu memorije.
	 * 
	 * @param i
	 *            predstavlja broj registara.
	 * @param j
	 *            predstavlja količinu memorije.
	 */
	public ComputerImpl(int i, int j) {
		registers = new RegistersImpl(j);
		memory = new MemoryImpl(i);
	}

	/**
	 * Getter za registre.
	 */
	@Override
	public Registers getRegisters() {
		return registers;
	}

	/**
	 * Getter za memoriju.
	 */
	@Override
	public Memory getMemory() {
		return memory;
	}

}
