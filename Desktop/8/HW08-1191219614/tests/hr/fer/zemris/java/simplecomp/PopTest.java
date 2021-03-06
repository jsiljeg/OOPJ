package hr.fer.zemris.java.simplecomp;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import hr.fer.zemris.java.simplecomp.impl.instructions.InstrPop;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;
import hr.fer.zemris.java.simplecomp.models.Memory;
import hr.fer.zemris.java.simplecomp.models.Registers;

/**
 * Klasa za testiranje pop instrukcije.
 * 
 * @author Jure Šiljeg
 *
 */

@SuppressWarnings("javadoc")
public class PopTest {
	List<InstructionArgument> arguments;

	@Mock
	Computer c = mock(Computer.class);
	Registers r = mock(Registers.class);
	Memory m = mock(Memory.class);
	InstructionArgument r0 = mock(InstructionArgument.class);

	@Before
	public void setThingsUp() {
		MockitoAnnotations.initMocks(this);
		arguments = new ArrayList<InstructionArgument>();
		when(r0.getValue()).thenReturn(0); // registerIndex = 0
		when(r0.isRegister()).thenReturn(true);
		arguments.add(r0);

		when(c.getRegisters()).thenReturn(r);
		when(c.getMemory()).thenReturn(m);

		when(r.getRegisterValue(15)).thenReturn(10); // valueOfFifteenthRegister
														// = 10
		when(m.getLocation(11)).thenReturn(13);// storingValue = 13

	}

	@Test
	public void executeTest() {
		Instruction pop = new InstrPop(arguments);
		pop.execute(c);
		verify(c, times(3)).getRegisters();
		verify(m, times(1)).getLocation(11);
		verify(r, times(1)).setRegisterValue(0, 13);
		verify(r, times(1)).setRegisterValue(15, 11);
	}
}
