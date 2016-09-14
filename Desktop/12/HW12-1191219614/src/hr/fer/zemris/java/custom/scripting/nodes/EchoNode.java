package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;

/**
 * Razred koji predstavlja čvor koji predstavlja pak naredbu koja dinamički
 * generira neki tekstualni izlaz.
 * 
 * @author Jure Šiljeg
 * @version 1.0.
 */
public class EchoNode extends Node {
	/** Polje elemenata koje sadržava echo čvor. */
	private Element[] elements;

	/**
	 * Konstruktor.
	 * 
	 * @param elements
	 *            parametri za inicijalizaciju.
	 */
	public EchoNode(Element[] elements) {
		this.elements = elements;
	}

	/**
	 * Getter.
	 * 
	 * @return elemente iz privatne kolekcije.
	 */
	public Element[] getElements() {
		return elements;
	}

	/**
	 * @see hr.fer.zemris.java.custom.scripting.nodes.Node#accept(hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor)
	 */
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitEchoNode(this);
	}

	/**
	 * @see hr.fer.zemris.java.custom.scripting.nodes.Node#toString()
	 */
	@Override
	public String toString() {
		String str = "{$= ";
		for (Element e : elements) {
			str += e.asText() + " ";
		}
		return str + "$}";
	}
}
