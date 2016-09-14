package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.RegisterUtil;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Razred koji brine o množenju sadržaja dvaju registara i produkt postavlja u
 * treći.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class InstrMul implements Instruction {
	/** Indeks odredišnog registra. */
	private int indexRegistra1;
	/** Indeks registra odakle vadimo prvi faktor. */
	private int indexRegistra2;
	/** Indeks registra odakle vadimo drugi faktor. */
	private int indexRegistra3;

	/**
	 * Konstruktor koji inicijalizira indekse registara koji sudjeluju u
	 * množenju.
	 * 
	 * @param arguments
	 *            argumenti uz komandu mul. Moraju biti 3.
	 */
	public InstrMul(List<InstructionArgument> arguments) {
		if (arguments.size() != 3) {
			throw new IllegalArgumentException("Expected 2 arguments!");
		}
		for (int i = 0; i < 3; i++) {
			if (!arguments.get(i).isRegister() || RegisterUtil.isIndirect((Integer) arguments.get(i).getValue())) {
				throw new IllegalArgumentException("Type mismatch for argument " + i + "!");
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
		computer.getRegisters().setRegisterValue(indexRegistra1, Integer.valueOf((Integer) value1 * (Integer) value2));
		return false;
	}

}
