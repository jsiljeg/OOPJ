package hr.fer.zemris.java.tecaj.hw07.crypto;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Razred koji implementira funkcionalnosti za enkripciju i dekripciju datoteke.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class CryptoFunctionalities {
	/** Algoritam koji se koristi prilikom (de)šifriranja. */
	private static final String ALGORITHM = "AES";
	/** Transformacija podataka. */
	private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

	/**
	 * Metoda koja obavlja enkripciju.
	 * 
	 * @param password
	 *            šifra koju koristimo za kriptiranje.
	 * @param vector
	 *            inicijalizacijski vektor.
	 * @param inputFile
	 *            ulazna datoteka.
	 * @param outputFile
	 *            izlazna datoteka.
	 * @throws CryptoException
	 *             sveobuhvatna iznimka za problem bilo kakve prirode u
	 *             (de)šifriranju.
	 */
	public static void encrypt(String password, String vector, File inputFile, File outputFile) throws CryptoException {
		crypting(Cipher.ENCRYPT_MODE, password, vector, inputFile, outputFile);
	}

	/**
	 * Metoda koja obavlja dekripciju.
	 * 
	 * @param password
	 *            šifra koju koristimo za kriptiranje.
	 * @param vector
	 *            inicijalizacijski vektor.
	 * @param inputFile
	 *            ulazna datoteka.
	 * @param outputFile
	 *            izlazna datoteka.
	 * @throws CryptoException
	 *             sveobuhvatna iznimka za problem bilo kakve prirode u
	 *             (de)šifriranju.
	 */
	public static void decrypt(String password, String vector, File inputFile, File outputFile) throws CryptoException {
		crypting(Cipher.DECRYPT_MODE, password, vector, inputFile, outputFile);
	}

	/**
	 * Metoda koja pretvara String reprezentiran heksadekadskim znamenkama u
	 * polje bajtova. Dobivaju se 32 hex-znamenke.
	 * 
	 * @param keyText
	 *            String čija bajt-reprezentacija nas zanima.
	 * @return traženo polje bajtova.
	 */
	public static byte[] hextobyte(String keyText) {
		int len = keyText.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(keyText.charAt(i), 16) << 4)
					+ Character.digit(keyText.charAt(i + 1), 16));
		}
		return data;
	}

	/**
	 * Metoda koja obavlja enkripciju/dekripciju podataka.
	 * 
	 * @param cipherMode
	 *            identifikator za enkripciju/dekripciju.
	 * @param password
	 *            šifra koju koristimo za kriptiranje.
	 * @param vector
	 *            inicijalizacijski vektor.
	 * @param inputFile
	 *            ulazna datoteka.
	 * @param outputFile
	 *            izlazna datoteka.
	 * @throws CryptoException
	 *             sveobuhvatna iznimka za problem bilo kakve prirode u
	 *             (de)šifriranju.
	 */
	private static void crypting(int cipherMode, String password, String vector, File inputFile, File outputFile)
			throws CryptoException {
		try {
			SecretKeySpec secretKey = new SecretKeySpec(hextobyte(password), ALGORITHM);
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(hextobyte(vector));
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(cipherMode, secretKey, paramSpec);

			FileInputStream inputStream = new FileInputStream(inputFile);
			FileOutputStream outputStream = new FileOutputStream(outputFile);
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile));

			byte[] buffer = new byte[4096];
			int bytes = 0;
			while ((bytes = bis.read(buffer, 0, buffer.length)) > 0) {
				byte[] ba2 = cipher.update(buffer, 0, bytes);
				outputStream.write(ba2);
			}
			bis.close();
			inputStream.close();
			outputStream.close();

		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IOException
				| InvalidAlgorithmParameterException ex) {
			throw new CryptoException("Grješka prilikom enkripcije/dekripcije!", ex);
		}
	}

	/**
	 * Metoda koja obavlja "digest" izračun nad zadanom datotekom.
	 * 
	 * @param fileName
	 *            ime datoteke nad kojom vršimo ovu provjeru.
	 * @return SHA-256 "digest" datoteke.
	 * @throws CryptoException
	 *             sveobuhvatna iznimka za problem bilo kakve prirode u
	 *             (de)šifriranju.
	 */
	public static StringBuffer processInputSha(String fileName) throws CryptoException {
		// byte ->hex
		StringBuffer sb;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			FileInputStream fis = new FileInputStream(fileName);

			byte[] dataBytes = new byte[1024];

			int nread = 0;
			while ((nread = fis.read(dataBytes)) != -1) {
				md.update(dataBytes, 0, nread);
			}
			byte[] mdbytes = md.digest();

			sb = new StringBuffer();
			for (int i = 0; i < mdbytes.length; i++) {
				sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			fis.close();
		} catch (NoSuchAlgorithmException | IOException e) {

			throw new CryptoException("Grješka prilikom enkripcije/dekripcije!", e);
		}
		return sb;
	}
}
