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

import hr.fer.zemris.java.simplecomp.impl.instructions.InstrLoad;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;
import hr.fer.zemris.java.simplecomp.models.Memory;
import hr.fer.zemris.java.simplecomp.models.Registers;

/**
 * Klasa za testiranje load instrukcije.
 * 
 * @author Jure Šiljeg
 *
 */
@SuppressWarnings("javadoc")
public class LoadTest {

	List<InstructionArgument> arguments;

	@Mock
	Computer c = mock(Computer.class);
	Registers r = mock(Registers.class);
	Memory m = mock(Memory.class);
	InstructionArgument arg0 = mock(InstructionArgument.class);
	InstructionArgument arg1 = mock(InstructionArgument.class);

	@Before
	public void setThingsUp() {
		MockitoAnnotations.initMocks(this);
		arguments = new ArrayList<InstructionArgument>();
		when(arg0.getValue()).thenReturn(0);
		when(arg1.getValue()).thenReturn(1);
		when(arg0.isRegister()).thenReturn(true);
		when(arg1.isNumber()).thenReturn(true);
		arguments.add(arg0);
		arguments.add(arg1);
		when(c.getRegisters()).thenReturn(r);
		when(c.getMemory()).thenReturn(m);
		when(r.getRegisterValue(0)).thenReturn(0);
		when(m.getLocation(1)).thenReturn(42);
	}

	@Test
	public void executeTest() {
		Instruction load = new InstrLoad(arguments);
		load.execute(c);
		verify(c, times(1)).getRegisters();
		verify(r, times(1)).setRegisterValue(0, 42);

	}

}
