package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.RegisterUtil;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Razred koji simulira smanjenje vrijednosti unutar registra za 1.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class InstrDecrement implements Instruction {
	/** Indeks registra nad kojim se vrši dekrementiranje. */
	private int registerIndex;

	/**
	 * Konstruktor za komandu dekrementiranja. Pronalazi indeks registra nad
	 * kojim obavljamo operaciju.
	 * 
	 * @param arguments
	 *            argumenti uz komandu decrement.
	 */
	public InstrDecrement(List<InstructionArgument> arguments) {
		if (arguments.size() != 1) {
			throw new IllegalArgumentException("Očekiva se 1 argument!");
		}
		if (!arguments.get(0).isRegister() || RegisterUtil.isIndirect((Integer) arguments.get(0).getValue())) {
			throw new IllegalArgumentException(
					"Argument " + arguments.get(0) + " mora biti registar i ne podržava indirektno adresiranje!");
		}
		this.registerIndex = RegisterUtil.getRegisterIndex((Integer) arguments.get(0).getValue());
	}

	@SuppressWarnings("javadoc")
	@Override
	public boolean execute(Computer computer) {
		Object value = computer.getRegisters().getRegisterValue(registerIndex);
		computer.getRegisters().setRegisterValue(registerIndex, (int) value - 1);
		return false;
	}
}
