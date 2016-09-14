package hr.fer.zemris.java.simplecomp;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import hr.fer.zemris.java.simplecomp.impl.instructions.InstrRet;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;
import hr.fer.zemris.java.simplecomp.models.Memory;
import hr.fer.zemris.java.simplecomp.models.Registers;

/**
 * Klasa za testiranje ret instrukcije.
 * 
 * @author Jure Šiljeg
 *
 */

@SuppressWarnings("javadoc")
public class RetTest {

	@Mock
	Computer c = mock(Computer.class);
	Registers r = mock(Registers.class);
	Memory m = mock(Memory.class);
	@SuppressWarnings("unchecked")
	List<InstructionArgument> arguments = mock(List.class);

	@Before
	public void setThingsUp() {
		MockitoAnnotations.initMocks(this);
		when(c.getRegisters()).thenReturn(r);
		when(c.getMemory()).thenReturn(m);

		when(arguments.size()).thenReturn(0);
		when(r.getRegisterValue(15)).thenReturn(15);// valueOfFifteenthRegister
		// = 15
		when(m.getLocation(16)).thenReturn(20); // storingValue = 20

	}

	@Test
	public void executeTest() {
		Instruction ret = new InstrRet(arguments);
		ret.execute(c);
		verify(c, times(3)).getRegisters();
		verify(r, times(1)).setRegisterValue(15, 16);
		verify(r, times(1)).setProgramCounter(20);
	}
}
