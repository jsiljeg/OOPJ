package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.RegisterUtil;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Razred koji je zadužen za ispisivanje sadržaja u (in)rirektnom adresiranju na
 * konzolu.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class InstrEcho implements Instruction {
	/** Indeks registra nad kojim se vrši operacija. */
	private int registerIndex;
	/** Pomak u slučaju indirektnog adresiranja. */
	private int registerOffset;
	/** Vodi brigu o tome je li adresiranje (in)direktno. */
	private boolean isIndirect;

	/**
	 * Konstruktor za naredbu ispisa. Vadi podatke o indeksu registra i
	 * eventualnom pomaku ako postoji (za slučaj indirektnog adresiranja).
	 * 
	 * @param arguments
	 *            argumenti uz komandu echo.
	 */
	public InstrEcho(List<InstructionArgument> arguments) {
		if (arguments.size() != 1) {
			throw new IllegalArgumentException("Očekiva se 1 argument!");
		}
		if (!arguments.get(0).isRegister()) {
			throw new IllegalArgumentException("Argument " + arguments.get(0) + " mora biti registar!");
		}
		if (RegisterUtil.isIndirect((Integer) arguments.get(0).getValue())) {
			this.registerOffset = RegisterUtil.getRegisterOffset((Integer) arguments.get(0).getValue());
			this.isIndirect = true;
		} else {
			this.isIndirect = false;
		}
		this.registerIndex = RegisterUtil.getRegisterIndex((Integer) arguments.get(0).getValue());
	}

	@SuppressWarnings("javadoc")
	@Override
	public boolean execute(Computer computer) {
		Object valueOfRegister = computer.getRegisters().getRegisterValue(registerIndex);
		Object indirect = null;
		if (isIndirect) {
			indirect = computer.getMemory().getLocation(registerOffset + (int) valueOfRegister);
		}
		System.out.print(isIndirect ? indirect : valueOfRegister);
		return false;
	}
}
