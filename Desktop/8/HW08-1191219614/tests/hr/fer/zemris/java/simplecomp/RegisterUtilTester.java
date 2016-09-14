package hr.fer.zemris.java.simplecomp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class RegisterUtilTester {

	@Test
	public void testUnpacking1() {
		int registerDescriptor = 0x1000102;
		assertEquals(2, RegisterUtil.getRegisterIndex(registerDescriptor));
		assertEquals(1, RegisterUtil.getRegisterOffset(registerDescriptor));
		assertEquals(true, RegisterUtil.isIndirect(registerDescriptor));
	}

	@Test
	public void testUnpacking2() {
		int registerDescriptor = 0x1FFFF02;
		assertEquals(2, RegisterUtil.getRegisterIndex(registerDescriptor));
		assertEquals(-1, RegisterUtil.getRegisterOffset(registerDescriptor));
		assertEquals(true, RegisterUtil.isIndirect(registerDescriptor));
	}

	@Test
	public void testUnpacking3() {
		int registerDescriptor = 0x18000FE;
		assertEquals(254, RegisterUtil.getRegisterIndex(registerDescriptor));
		assertEquals(-32768, RegisterUtil.getRegisterOffset(registerDescriptor));
		assertEquals(true, RegisterUtil.isIndirect(registerDescriptor));
	}

	@Test
	public void testUnpacking4() {
		int registerDescriptor = 0x17FFFFF;
		assertEquals(255, RegisterUtil.getRegisterIndex(registerDescriptor));
		assertEquals(32767, RegisterUtil.getRegisterOffset(registerDescriptor));
		assertEquals(true, RegisterUtil.isIndirect(registerDescriptor));
	}

}
