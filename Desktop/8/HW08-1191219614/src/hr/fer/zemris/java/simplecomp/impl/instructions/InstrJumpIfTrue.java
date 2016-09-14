package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.RegisterUtil;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Razred koji brine o nastavku izvršavanja programa na drugoj lokaciji u kodu
 * ako su zadovoljeni uvjeti. Konkretno, jednakost sadržaja registara jedina
 * modificira uvjete za ovakvo ponašanje.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class InstrJumpIfTrue implements Instruction {
	/** Memorijska lokacija na koju se ide. */
	private int location;

	/**
	 * Konstruktor koji inicijalizira traženu lokaciju iz argumenata.
	 * 
	 * @param arguments
	 *            argumenti uz komandu jumpIfTrue. Mora biti samo jedan.
	 */
	public InstrJumpIfTrue(List<InstructionArgument> arguments) {
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
		if (computer.getRegisters().getFlag()) {
			computer.getRegisters().setProgramCounter(location);
		}
		return false;
	}

}
