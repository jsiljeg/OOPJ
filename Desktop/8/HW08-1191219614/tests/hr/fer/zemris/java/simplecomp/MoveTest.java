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

import hr.fer.zemris.java.simplecomp.impl.instructions.InstrMove;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.Instruction;
import hr.fer.zemris.java.simplecomp.models.InstructionArgument;
import hr.fer.zemris.java.simplecomp.models.Registers;

/**
 * Klasa za testiranje move instrukcije.
 * 
 * @author Jure Šiljeg
 *
 */
@SuppressWarnings(value = { "javadoc" })
public class MoveTest {

	List<InstructionArgument> arguments;

	@Mock
	Computer c = mock(Computer.class);
	Registers r = mock(Registers.class);
	InstructionArgument r0 = mock(InstructionArgument.class);
	InstructionArgument r1 = mock(InstructionArgument.class);

	@Before
	public void setThingsUp() {
		MockitoAnnotations.initMocks(this);
		arguments = new ArrayList<InstructionArgument>();
		when(r0.getValue()).thenReturn(0);
		when(r1.getValue()).thenReturn(1);
		when(r0.isRegister()).thenReturn(true);
		when(r1.isRegister()).thenReturn(true);
		arguments.add(r0);
		arguments.add(r1);
		when(c.getRegisters()).thenReturn(r);
		when(r.getRegisterValue(0)).thenReturn(0);
		when(r.getRegisterValue(1)).thenReturn(100);
	}

	@Test
	public void executeTest() {
		Instruction move = new InstrMove(arguments);
		move.execute(c);
		verify(c, times(3)).getRegisters();
		verify(r, times(1)).setRegisterValue(0, 100);
		verify(r, times(1)).getRegisterValue(1);

	}
}
