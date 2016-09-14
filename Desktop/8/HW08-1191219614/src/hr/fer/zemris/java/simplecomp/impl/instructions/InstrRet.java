package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;
import hr.fer.zemris.java.simplecomp.models.Registers;

/**
 * Razred koji implementira instrukciju za povrat iz podrutine.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class InstrRet implements Instruction {

	/**
	 * Konstruktor koji kontrolira jesmo li unijeli ikakav argument ili nismo.
	 * 
	 * @param arguments
	 *            argumenti uz komandu ret. Ne smije ih biti.
	 */
	public InstrRet(List<InstructionArgument> arguments) {
		if (arguments.size() != 0) {
			throw new IllegalArgumentException("Ne očekuju se argumenti!");
		}
	}

	@SuppressWarnings("javadoc")
	public boolean execute(Computer computer) {

		Object valueOfFifteenthRegister = computer.getRegisters().getRegisterValue(Registers.STACK_REGISTER_INDEX);
		Object storingValue = computer.getMemory().getLocation((int) valueOfFifteenthRegister + 1);
		computer.getRegisters().setProgramCounter((int) storingValue);
		computer.getRegisters().setRegisterValue(Registers.STACK_REGISTER_INDEX, (int) valueOfFifteenthRegister + 1);
		return false;
	}

}
