package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Razred kojim ćemo reprezentirati jednu logičku i semantičku cjelinu.
 * 
 * @author Jure Šiljeg
 * @version 1.0.
 */
public class Token {

	/**
	 * Označava tip cjeline.
	 */
	private TokenType type;
	/**
	 * Označava vrijednost koju cjelina ima.
	 */
	private Object value;

	/**
	 * Konstruktor koji inicijalizira našu cjelinu.
	 * 
	 * @param type
	 *            tip cjeline.
	 * @param value
	 *            vrijednost koju cjelina ima.
	 */
	public Token(TokenType type, Object value) {
		this.type = type;
		this.value = value;
	}

	/**
	 * Predstavlja getter za dohvaćanje vrijednosti cjeline.
	 * 
	 * @return tu vrijednost koju smo potraživali.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Predstavlja getter za dohvaćanje tipa cjeline.
	 * 
	 * @return taj potraživani tip.
	 */
	public TokenType getType() {
		return type;
	}
}
