package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;
import hr.fer.zemris.java.simplecomp.models.Registers;

/**
 * Razred koji brine o prelasku na ispravnu adresu prilikom izvođenja podrutine.
 * Poziv potprograma.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class InstrCall implements Instruction {
	/**
	 * Adresa na koju trebamo ići i koju ćemo upisati u registar s programskim
	 * brojilom.
	 */
	private int adress;

	/**
	 * Konstruktor koji in argumenata vadi adresu na koju trebamo ići.
	 * 
	 * @param arguments
	 *            argumenti uz komandu call.
	 */
	public InstrCall(List<InstructionArgument> arguments) {
		if (arguments.size() != 1) {
			throw new IllegalArgumentException("Očekiva se 1 argument!");
		}
		if (!arguments.get(0).isNumber()) {
			throw new IllegalArgumentException("Argument " + arguments.get(0) + " mora biti adresa!");
		}
		this.adress = (Integer) arguments.get(0).getValue();
	}

	@SuppressWarnings("javadoc")
	@Override
	public boolean execute(Computer computer) {
		int pcValue = computer.getRegisters().getProgramCounter();
		Object valueOfFifteenthRegister = computer.getRegisters().getRegisterValue(Registers.STACK_REGISTER_INDEX);
		computer.getMemory().setLocation((int) valueOfFifteenthRegister, pcValue);
		computer.getRegisters().setRegisterValue(Registers.STACK_REGISTER_INDEX, (int) valueOfFifteenthRegister - 1);
		computer.getRegisters().setProgramCounter(adress);
		return false;
	}

}
