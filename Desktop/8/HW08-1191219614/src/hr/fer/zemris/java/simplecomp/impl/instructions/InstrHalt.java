package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Razred koji brine o zaustavljanju programa.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class InstrHalt implements Instruction {

	/**
	 * Konstruktor instrukcije. Ne smije primati argumente.
	 * 
	 * @param arguments
	 *            argumenti uz komandu halt.
	 */
	public InstrHalt(List<InstructionArgument> arguments) {
		if (arguments.size() != 0) {
			throw new IllegalArgumentException("Expected 0 arguments!");
		}
	}

	@SuppressWarnings("javadoc")
	@Override
	public boolean execute(Computer computer) {
		return true;
	}

}
