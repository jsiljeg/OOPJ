package hr.fer.zemris.java.tecaj.hw07.shell;

/**
 * Enumeracia koa označava popis stanja ljuske.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public enum ShellStatus {
	/** Predstavlja stanje u kojem nastavljamo pitati korisnika za unos. */
	CONTINUE,
	/** Predtsvlja stanje u kojem prekidamo komunikaciju luske i korisnika. */
	TERMINATE;
}
