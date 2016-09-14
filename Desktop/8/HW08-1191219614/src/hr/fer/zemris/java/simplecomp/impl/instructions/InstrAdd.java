package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.RegisterUtil;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Razred koji brine o zbrajanju sadržaja dvaju registara i pohrane u treći.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class InstrAdd implements Instruction {
	/** Indeks prvog registra. */
	private int indexRegistra1;
	/** Indeks drugog registra. */
	private int indexRegistra2;
	/** Indeks trećeg registra. */
	private int indexRegistra3;

	/**
	 * Konstruktor koji iz proslijeđenih argumenata čupa vrijednosti ideksa
	 * registara kojima manipuliramo.
	 * 
	 * @param arguments
	 *            argumenti uz komandu add.
	 */
	public InstrAdd(List<InstructionArgument> arguments) {
		if (arguments.size() != 3) {
			throw new IllegalArgumentException("Očekivaju se 2 argumenta!");
		}
		for (int i = 0; i < 3; i++) {
			if (!arguments.get(i).isRegister() || RegisterUtil.isIndirect((Integer) arguments.get(i).getValue())) {
				throw new IllegalArgumentException("Tipovi argumenata se ne podudaraju!");
			}
		}
		this.indexRegistra1 = RegisterUtil.getRegisterIndex((Integer) arguments.get(0).getValue());
		this.indexRegistra2 = RegisterUtil.getRegisterIndex((Integer) arguments.get(1).getValue());
		this.indexRegistra3 = RegisterUtil.getRegisterIndex((Integer) arguments.get(2).getValue());
	}

	@SuppressWarnings("javadoc")
	@Override
	public boolean execute(Computer computer) {
		Object value1 = computer.getRegisters().getRegisterValue(indexRegistra2);
		Object value2 = computer.getRegisters().getRegisterValue(indexRegistra3);
		computer.getRegisters().setRegisterValue(indexRegistra1, Integer.valueOf((Integer) value1 + (Integer) value2));
		return false;
	}
}
