package hr.fer.zemris.java.simplecomp.impl.instructions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import hr.fer.zemris.java.simplecomp.RegisterUtil;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;

/**
 * Razred koji brine o učitavanju integera iz komandne linije i spra ga na
 * određenu memorijsku lokaciju.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class InstrIinput implements Instruction {

	/** Zadana lokacija na koju spremamo učitani integer. */
	private int location;

	/**
	 * Konstruktor za komandu. Mora primiti točno jedan argument.
	 * 
	 * @param arguments
	 *            argumenti uz komandu iinput. Dobiveni broj se tumači kao
	 *            memorijska lokacija.
	 */
	public InstrIinput(List<InstructionArgument> arguments) {
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
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input = "";
		int readInteger = 0;
		try {
			input = br.readLine();
			readInteger = Integer.parseInt(input);
		} catch (IOException | NumberFormatException e) {
			computer.getRegisters().setFlag(false);
			return false;
		}
		computer.getRegisters().setFlag(true);
		computer.getMemory().setLocation(location, readInteger);

		return false;
	}

}
