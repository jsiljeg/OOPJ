package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.RegisterUtil;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;
import hr.fer.zemris.java.simplecomp.models.Registers;

/**
 * Razred koji brine o komandi koja s vrha stoga skida podatak i pohranjuje ga u
 * registar sa zadanim indeksom.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class InstrPop implements Instruction {
	/** Indeks registra. */
	private int registerIndex;

	/**
	 * Konstruktor koji pronalazi zadani indeks registra iz proslijeđenog
	 * argumenta. Točno jedan argument mora biti.
	 * 
	 * @param arguments
	 *            argumenti uz komandu pop. Točno jedan mora biti.
	 */
	public InstrPop(List<InstructionArgument> arguments) {
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
		Object valueOfFifteenthRegister = computer.getRegisters().getRegisterValue(Registers.STACK_REGISTER_INDEX);
		Object storingValue = computer.getMemory().getLocation((int) valueOfFifteenthRegister + 1);
		computer.getRegisters().setRegisterValue(registerIndex, storingValue);
		computer.getRegisters().setRegisterValue(Registers.STACK_REGISTER_INDEX, (int) valueOfFifteenthRegister + 1);
		return false;
	}

}
