package hr.fer.zemris.java.tecaj.hw07.crypto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class CryptoTests {
	// Ako sam dobro shvatio tekst zadatka, piše da samo treba JUnit testove za
	// ovu metodu i pritom se iz konteksta vidi da se to odnosi na metodu
	// hextobyte. Zašto ovo pišem? Pa, zato što ima još par sati do deadlinea i
	// ne stignem dobit info je li treba još nekakve testove pisati, a ovo mi se
	// čini prečudno da samo treba ovo napisati. Em jedna metoda koja i ne
	// zahtjeva puno testova. Mislim, da, uvijek se može za
	// bilo što sam nadodati funkcionalnost i testiranje, al dobro. Ovo pišem
	// samo jer sam
	// razumio da treba samo za tu metodu koju bih, osobno, valjda zadnju
	// testirao od silnih stvari koje je trebalo implementirati. No, bilo kako
	// bilo. Ne znam kako da pristojnije i poniznije napišem i jedino čega sam
	// se sjetio je ovaj komentar ovdje. NHF, dear reader! :)

	@Test
	public void firstTestHexToByte() {
		String str = "a52217e3ee213ef1ffdee3a192e2ac7e";
		byte[] arr = CryptoFunctionalities.hextobyte(str);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < arr.length; ++i) {
			sb.append(Integer.toHexString(arr[i] & 0xFF));
		}
		assertEquals(str, sb.toString());
	}

	@Test
	public void secondTestHexToByte() {
		String str = "000102030405060708090a0b0c0d0e0f";
		byte[] arr = CryptoFunctionalities.hextobyte(str);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < arr.length; ++i) {
			sb.append(Integer.toHexString(arr[i] & 0xFF));
		}
		assertEquals("0123456789abcdef", sb.toString());
	}

	@Test
	public void thirdTestHexToByte() {
		String str = "000102030405060708090a0b0c0d0e0f2302";
		byte[] arr = CryptoFunctionalities.hextobyte(str);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < arr.length; ++i) {
			sb.append(Integer.toHexString(arr[i] & 0xFF));
		}
		assertTrue("0123456789abcdef232".length() == sb.length());

	}

}
