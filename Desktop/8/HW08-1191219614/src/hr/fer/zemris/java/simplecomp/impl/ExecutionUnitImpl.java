package hr.fer.zemris.java.simplecomp.impl;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.ExecutionUnit;
import hr.fer.zemris.java.simplecomp.models.Instruction;

/**
 * Razred koji implementira upravljački sklop računala.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class ExecutionUnitImpl implements ExecutionUnit {

	@SuppressWarnings("javadoc")
	@Override
	public boolean go(Computer computer) {
		computer.getRegisters().setProgramCounter(0);
		while (true) {
			Instruction instruction = (Instruction) computer.getMemory()
					.getLocation(computer.getRegisters().getProgramCounter());
			computer.getRegisters().incrementProgramCounter();
			if (instruction.execute(computer)) {
				break;
			}
		}
		return true;
	}
}
