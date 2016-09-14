package hr.fer.zemris.java.tecaj.hw07.crypto;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Razred koji služi za demonstraciju.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class Crypto extends CryptoFunctionalities {

	/** Konstrola riječ za provjeru digesta. */
	private static final String CHECK_SHA = "checksha";
	/** Kontrolna riječ za provjeru radi li se enkripcija. */
	private static final String ENCRIPT = "encrypt";
	/** Kontrolna riječ za provjeru radi li se dekripcija. */
	private static final String DECRYPT = "decrypt";

	/**
	 * Glavna metoda demonstrira rada AES kriptoalgoritma sa zadanim 128-bitnim
	 * ključem i za demonstraciju izračuna i provjere ispravnosti datotečnog
	 * "digesta" pomoću SHA-256. Svi izračuni i konverzije se obavljaju na .pdf
	 * datotekama.
	 * 
	 * @param args
	 *            argumenti komandne linije koji govore o kojoj ključnoj
	 *            funkcionalnosti se radi: digestanje, enkripcija ili
	 *            dekripcija. Ako se proslijede 2 argumenta radi se o digestu, a
	 *            ako su 3 riječ je o (de)šifriranju.
	 * @throws CryptoException
	 *             sveobuhvatna iznimka za problem bilo kakve prirode u
	 *             (de)šifriranju.
	 */
	public static void main(String[] args) throws CryptoException {

		try {
			if (args.length != 2 && args.length != 3) {
				System.out.println("Unijeli ste pogrešan broj argumenata!");
				return;
			} else if (args.length == 2) {

				if (!args[0].equals(CHECK_SHA)) {
					System.out.println("Nepodržan prvi argument! Trebali ste unijeti: " + CHECK_SHA);
					return;
				}

				System.out.println("Please provide expected sha-256 digest for hw07part2.pdf: ");
				System.out.print("> ");

				StringBuffer sb = processInputSha("files/" + args[1]);

				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				String s = br.readLine();

				if (s.equals(sb.toString())) {
					System.out.println("Digesting completed. Digest of hw07test.bin matches expected digest.");
				} else {
					System.out.println(
							"Digesting completed. Digest of hw07test.bin does not match the expected digest. Digest was: 0d3d4424461e22a458c6c716395f07dd9cea2180a996e78349985eda78e8b800");
				}
			} else { // 3 arg

				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				// prvi dio obrade
				System.out.println("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):");
				System.out.print("> ");
				String password = br.readLine();

				// drugi dio obrade
				System.out.println("Please provide initialization vector as hex-encoded text (32 hex-digits):");
				System.out.print("> ");
				String vector = br.readLine();

				String src = args[1];
				String dest = args[2];
				File inputFile = new File("files/" + src);

				if (args[0].equals(ENCRIPT)) {
					File encryptedFile = new File("files/" + dest);
					encrypt(password, vector, inputFile, encryptedFile);
					System.out.println("Encryption completed. Generated file " + dest + " based on file " + src);
				}
				if (args[0].equals(DECRYPT)) {
					File decryptedFile = new File("files/" + dest);
					decrypt(password, vector, inputFile, decryptedFile);
					System.out.println("Decryption completed. Generated file " + dest + " based on file " + src);
				}

			}
		} catch (IOException e) {
			throw new CryptoException("Grješka prilikom enkripcije/dekripcije!", e);
		}

	}

}
