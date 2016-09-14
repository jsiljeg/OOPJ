package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.RegisterUtil;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Razred koji brine o pohrani nekog sadržaja u određeni registar.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class InstrLoad implements Instruction {
	/** Indeks registra u koji se pohrana vrši. */
	private int registerIndex;
	/** Lokacija s koje podatak ubacujemo u zadani registar. */
	private int content;

	/**
	 * Konstruktor za zadanu instrukciju. Vadi zadani indeks registra i lokaciju
	 * s koje punimo registar.
	 * 
	 * @param arguments
	 *            argumenti uz komandu load.
	 */
	public InstrLoad(List<InstructionArgument> arguments) {
		if (arguments.size() != 2) {
			throw new IllegalArgumentException("Očekivao sam 2 argumenta!");
		}
		if (!arguments.get(0).isRegister() || RegisterUtil.isIndirect((Integer) arguments.get(0).getValue())) {
			throw new IllegalArgumentException(
					"Argument " + arguments.get(0) + " mora biti registar i ne podržava indirektno adresiranje!");
		}
		if (!arguments.get(1).isNumber()) {
			throw new IllegalArgumentException("Argument " + arguments.get(1).getValue() + " mora biti broj!");
		}

		this.registerIndex = RegisterUtil.getRegisterIndex((Integer) arguments.get(0).getValue());
		this.content = (int) arguments.get(1).getValue();
	}

	@SuppressWarnings("javadoc")
	@Override
	public boolean execute(Computer computer) {
		Object value = computer.getMemory().getLocation(content);
		computer.getRegisters().setRegisterValue(registerIndex, value);
		return false;
	}

}
