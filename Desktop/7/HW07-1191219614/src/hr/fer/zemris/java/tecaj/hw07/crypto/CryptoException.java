package hr.fer.zemris.java.tecaj.hw07.crypto;

/**
 * Razred koji brine o specijalnoj vrsti iznimke koja će se bacati u svim
 * slučajevima i koja obuhvaća sve ostale.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class CryptoException extends Exception {
	/** Defaultna verzija serije. */
	private static final long serialVersionUID = 1L;

	/**
	 * Defaultni konstruktor koji nema specijalno značenje.
	 */
	public CryptoException() {
	}

	/**
	 * Konstruktor zadužen za ispis grješke u određenom formatu.
	 * 
	 * @param message
	 *            poruka koju ćemo ispisati u konzolu.
	 * @param throwable
	 *            uzrok grješke.
	 */
	public CryptoException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
