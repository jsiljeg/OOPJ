package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.RegisterUtil;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Razred koji brine o odlasku na određenu lokaciju u programu. To se postiže
 * stavljanjem te lokacije u registar programskog brojila.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class InstrJump implements Instruction {
	/** Lokacija na kojoj se treba nastaviti izvršavati program. */
	private int location;

	/**
	 * Konstruktor za instrukciju. Vadi lokaciju iz proslijeđenih parametara.
	 * 
	 * @param arguments
	 *            argumenti uz komandu jump. Treba biti točno jedan.
	 */
	public InstrJump(List<InstructionArgument> arguments) {
		if (arguments.size() != 1) {
			throw new IllegalArgumentException("Očekiva se 1 argument!");
		}
		if (!arguments.get(0).isNumber() || RegisterUtil.isIndirect((Integer) arguments.get(0).getValue())) {
			throw new IllegalArgumentException(
					"Argument " + arguments.get(0) + " mora biti broj i ne podržava indirektno adresiranje!");
		}
		this.location = (Integer) arguments.get(0).getValue();
	}

	@SuppressWarnings("javadoc")
	@Override
	public boolean execute(Computer computer) {
		computer.getRegisters().setProgramCounter(location);
		return false;
	}

}
