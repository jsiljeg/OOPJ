package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.RegisterUtil;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;
import hr.fer.zemris.java.simplecomp.models.Registers;

/**
 * Razred koji brine u stavljanju sadržaja registra na memorijsku lokaciju koja
 * je trenutnivrh stoga.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class InstrPush implements Instruction {
	/** Indeks registra čiji sadržaj guramo u stog. */
	private int registerIndex;

	/**
	 * Konstruktor u kojem inicijaliziramo indeks registra iz kojeg stavljamo
	 * podatke u stog.
	 * 
	 * @param arguments
	 *            argumenti uz komandu push. Mora biti točno jedan.
	 */
	public InstrPush(List<InstructionArgument> arguments) {
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
		Object value = computer.getRegisters().getRegisterValue(registerIndex);
		computer.getMemory().setLocation((int) valueOfFifteenthRegister, value);
		computer.getRegisters().setRegisterValue(Registers.STACK_REGISTER_INDEX, (int) valueOfFifteenthRegister - 1);
		return false;
	}
}
