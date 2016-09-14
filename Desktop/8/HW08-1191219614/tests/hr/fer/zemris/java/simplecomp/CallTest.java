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

import hr.fer.zemris.java.simplecomp.impl.instructions.InstrCall;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;
import hr.fer.zemris.java.simplecomp.models.Memory;
import hr.fer.zemris.java.simplecomp.models.Registers;

/**
 * Klasa za testiranje call instrukcije.
 * 
 * @author Jure Šiljeg
 *
 */

@SuppressWarnings("javadoc")
public class CallTest {
	List<InstructionArgument> arguments;

	@Mock
	Computer c = mock(Computer.class);
	Registers r = mock(Registers.class);
	Memory m = mock(Memory.class);
	InstructionArgument arg1 = mock(InstructionArgument.class);

	@Before
	public void setThingsUp() {
		MockitoAnnotations.initMocks(this);
		arguments = new ArrayList<InstructionArgument>();
		when(arg1.getValue()).thenReturn(1); // adress = 1
		when(arg1.isNumber()).thenReturn(true);
		arguments.add(arg1);
		when(c.getRegisters()).thenReturn(r);
		when(c.getMemory()).thenReturn(m);

		when(r.getProgramCounter()).thenReturn(50); // pcvalue = 50
		when(r.getRegisterValue(15)).thenReturn(15); // valueOfFifteenthRegister
														// = 15

	}

	@Test
	public void executeTest() {
		Instruction call = new InstrCall(arguments);
		call.execute(c);
		verify(c, times(4)).getRegisters();
		verify(m, times(1)).setLocation(15, 50);
		verify(r, times(1)).setRegisterValue(15, 14);
		verify(r, times(1)).setProgramCounter(1);
	}
}
