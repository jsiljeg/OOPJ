package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Čvor koji reprezentira komad tekstualnih podataka.
 * 
 * @author Jure Šiljeg
 * @version 1.0.
 */
public class TextNode extends Node {
	/** Sadržaj tekstualni. */
	private String text;

	/**
	 * Konstruktor.
	 * 
	 * @param text
	 *            inicijalizacijski element.
	 */
	public TextNode(String text) {
		this.text = text;
	}

	/**
	 * Getter.
	 * 
	 * @return taj privatni element.
	 */
	public String getText() {
		return text;
	}

	/**
	 * @see hr.fer.zemris.java.custom.scripting.nodes.Node#toString()
	 */
	@Override
	public String toString() {
		return text;
	}

	/**
	 * @see hr.fer.zemris.java.custom.scripting.nodes.Node#accept(hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor)
	 */
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitTextNode(this);
	}
}
