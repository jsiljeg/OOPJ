package hr.fer.zemris.java.webserver;

/**
 * Sučelje prema bilo kojem objektu koji može procesirati trenutni zahtjev.
 * 
 * @author Jure Šiljeg
 *
 */
public interface IWebWorker {

	/**
	 * Metoda kojom pojedini radnik procesira zahtjev. U opisu svakog razreda
	 * koji implementira ovo sučelje nalazi se opis.
	 * 
	 * @param context
	 *            parametar zadužen za kreiranje konteksta prema klijentu.
	 */
	public void processRequest(RequestContext context);

}
