package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Klasa koa predstavlja validnu variablu.
 * 
 * @author Jure Šiljeg
 *
 */
public class ElementVariable extends Element {
	/** Pohranjena vrijednost imena varijable. */
	private String name;

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 *            inicializacijska referenca.
	 */
	public ElementVariable(String name) {
		this.name = name;
	}

	/**
	 * Za ispis metoda.
	 */
	@Override
	public String asText() {
		return name;
	}

	/**
	 * Getter za člansku variablu
	 * 
	 * @return tu variablu.
	 */
	public String getName() {
		return name;
	}

}
