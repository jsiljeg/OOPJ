package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.util.List;

import hr.fer.zemris.java.simplecomp.RegisterUtil;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Razred koji brine o prijenosu podatak iz registra u registar. Prvi argument
 * može biti registar ili indirektna adresa; drugi argument može biti registar,
 * indirektna adresa ili broj.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class InstrMove implements Instruction {
	/** Indeks prvog registra. */
	private int firstRegisterIndex;
	/** Indeks drugog registra. */
	private int secondRegisterIndex;
	/** Broj koji se eventualno nalazi kao drugi argument. */
	private int secondNumber;
	/** Eventualni pomak prvog registra. */
	private int firstRegisterOffset;
	/** Eventualni pomak drugog registra. */
	private int secondRegisterOffset;
	/** Identifikator korištenja indirektnog adresiranja u prvom argumentu. */
	private boolean firstIsIndirect;
	/** Identifikator korištenja indirektnog adresiranja u drugom argumentu. */
	private boolean secondIsIndirect;
	/** Identifikator je li drugi argument broj. */
	private boolean isSecondNumber;

	/**
	 * Konsturktor koji inicijalizira članske varijable iz argumenata.
	 * 
	 * @param arguments
	 *            argumenti uz komandu move.
	 */
	public InstrMove(List<InstructionArgument> arguments) {
		if (arguments.size() != 2) {
			throw new IllegalArgumentException("Očekivao sam 2 argumenta!");
		}
		// provjera uvjeta
		if (!arguments.get(0).isRegister() || (!arguments.get(1).isRegister() && !arguments.get(1).isNumber())) {
			throw new IllegalArgumentException("Argument " + arguments.get(0) + " mora biti"
					+ " ili registar ili podržavati indirektno " + "adresiranje, a argument" + " " + arguments.get(1)
					+ " mora biti ili" + " registar ili broj ili podržavati indirektno adresiranje!");
		}
		initializeFirst((Integer) arguments.get(0).getValue());
		if (arguments.get(1).isNumber()) {
			inilializeSecondNumber((Integer) arguments.get(1).getValue());
		} else {
			inilializeSecondRegister((Integer) arguments.get(1).getValue());
		}
	}

	/**
	 * Metoda koja obavlja inicijalizaciju u slučaju da je drugi argument broj.
	 * 
	 * @param value
	 *            vrijednost koju ima taj broj.
	 */
	private void inilializeSecondNumber(Integer value) {
		isSecondNumber = true;
		secondNumber = value;
	}

	/**
	 * Metoda koja obavlja inicijalizaciju u slučaju da je drugi argument
	 * registar.
	 * 
	 * @param value
	 *            vrijednost deskriptora.
	 */
	private void inilializeSecondRegister(Integer value) {
		if (RegisterUtil.isIndirect(value)) {
			this.secondRegisterOffset = RegisterUtil.getRegisterOffset(value);
			this.secondIsIndirect = true;
		} else {
			this.secondIsIndirect = false;
		}
		this.secondRegisterIndex = RegisterUtil.getRegisterIndex(value);
	}

	/**
	 * Metoda koja obavlja inicijalizaciju u slučaju da je prvi argument
	 * registar.
	 * 
	 * @param value
	 *            vrijednost deskriptora.
	 */
	private void initializeFirst(Integer value) {
		if (RegisterUtil.isIndirect(value)) {
			this.firstRegisterOffset = RegisterUtil.getRegisterOffset(value);
			this.firstIsIndirect = true;
		} else {
			this.firstIsIndirect = false;
		}
		this.firstRegisterIndex = RegisterUtil.getRegisterIndex(value);
	}

	@SuppressWarnings("javadoc")
	@Override
	public boolean execute(Computer computer) {
		Object valueOfFirstRegister = computer.getRegisters().getRegisterValue(firstRegisterIndex);
		Object valueOfSecondRegister = computer.getRegisters().getRegisterValue(secondRegisterIndex);

		if (firstIsIndirect) {
			if (isSecondNumber) {
				computer.getMemory().setLocation(firstRegisterOffset + (int) valueOfFirstRegister, secondNumber);
			} else if (secondIsIndirect) {
				Object secondFromMemoryLocation = computer.getMemory()
						.getLocation(secondRegisterOffset + (int) valueOfSecondRegister);
				computer.getMemory().setLocation(firstRegisterOffset + (int) valueOfFirstRegister,
						secondFromMemoryLocation);
			} else {
				computer.getMemory().setLocation(firstRegisterOffset + (int) valueOfFirstRegister,
						valueOfSecondRegister);
			}
		} else {
			if (isSecondNumber) {
				computer.getRegisters().setRegisterValue(firstRegisterIndex, secondNumber);
			} else if (secondIsIndirect) {
				int secondFromMemoryLocation = (int) computer.getMemory()
						.getLocation(secondRegisterOffset + (int) valueOfSecondRegister);
				computer.getRegisters().setRegisterValue(firstRegisterIndex, secondFromMemoryLocation);
			} else {
				computer.getRegisters().setRegisterValue(firstRegisterIndex, valueOfSecondRegister);
			}
		}

		return false;
	}

}
