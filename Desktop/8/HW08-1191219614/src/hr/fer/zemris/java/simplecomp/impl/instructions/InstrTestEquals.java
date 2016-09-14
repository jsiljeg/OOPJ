package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.RegisterUtil;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Razred za provjeru jednakosti sadržaja dvaju registara.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class InstrTestEquals implements Instruction {
	/** Indeks prvog registra. */
	private int firstRegisterIndex;
	/** Indeks prvog registra. */
	private int secondRegisterIndex;

	/**
	 * Konstruktor za jednakost sadržaja registara. Inicijalizira indekse
	 * odgovarajući registara i provjerava ispravnost proslijeđenih parametara.
	 * 
	 * @param arguments
	 *            podaci iz parsera o registrima.
	 */
	public InstrTestEquals(List<InstructionArgument> arguments) {
		if (arguments.size() != 2) {
			throw new IllegalArgumentException("Očekivao sam 2 argumenta!");
		}
		if (!arguments.get(0).isRegister() || !arguments.get(1).isRegister()
				|| RegisterUtil.isIndirect((Integer) arguments.get(1).getValue())
				|| RegisterUtil.isIndirect((Integer) arguments.get(0).getValue())) {
			throw new IllegalArgumentException(
					"Argumenti " + arguments.get(0) + " moraju biti registri i ne podržavati indirektno adresiranje!");
		}

		this.firstRegisterIndex = RegisterUtil.getRegisterIndex((Integer) arguments.get(0).getValue());
		this.secondRegisterIndex = RegisterUtil.getRegisterIndex((Integer) arguments.get(1).getValue());
	}

	@SuppressWarnings("javadoc")
	@Override
	public boolean execute(Computer computer) {
		Object value1 = computer.getRegisters().getRegisterValue(firstRegisterIndex);
		Object value2 = computer.getRegisters().getRegisterValue(secondRegisterIndex);
		boolean flag = value1.equals(value2) ? true : false;
		computer.getRegisters().setFlag(flag);
		return false;
	}

}
