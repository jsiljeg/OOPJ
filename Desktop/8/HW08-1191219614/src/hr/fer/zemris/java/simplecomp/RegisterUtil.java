package hr.fer.zemris.java.simplecomp;

/**
 * Razred koji brine o vađenju podataka iz deskriptora.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class RegisterUtil {

	/** Bit u kojem je zapisan podatak o (in)direktnom adresiranju. */
	private static final int INDEX_BIT = 24;
	/** Bit na kojem počinje zapis o pomaku (offsetu). */
	private static final int FIRST_OFFSET_BIT = 8;

	/**
	 * Metoda dohvaća indeks registra iz deskriptora registra.
	 * 
	 * @param registerDescriptor
	 *            deskriptor registra.
	 * @return indeks registra.
	 */
	public static int getRegisterIndex(int registerDescriptor) {
		return registerDescriptor & 0xFF;
	}

	/**
	 * Metoda provjerava radi li se o direktnom ili indirektnom načinu
	 * adresiranja.
	 * 
	 * @param registerDescriptor
	 *            deskriptor registra.
	 * @return istinu ako je indirektno adresiranje, inače laž.
	 */
	public static boolean isIndirect(int registerDescriptor) {
		return ((registerDescriptor >> INDEX_BIT) & 1) == 1;
	}

	/**
	 * Metoda vadi pomak koji je potrebno koristiti.
	 * 
	 * @param registerDescriptor
	 *            deskriptor registra.
	 * @return pomak.
	 */
	public static int getRegisterOffset(int registerDescriptor) {
		return ((short) ((registerDescriptor >> FIRST_OFFSET_BIT) & 0xFFFF));
	}
}
