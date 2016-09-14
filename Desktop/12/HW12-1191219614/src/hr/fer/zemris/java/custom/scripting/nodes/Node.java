package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Bazna klasa za sve čvorove grafa.
 * 
 * @author Jure Šiljeg
 * @version 1.0.
 */
public abstract class Node {
	/** Interna lista čvorova. */
	private List<Node> col;
	/** Zastavica koja signalizira trebamo li inicijalizirati ponovno listu. */
	private boolean canAdd = false;

	/**
	 * Dodaje dijete u internu kolekciju.
	 * 
	 * @param child
	 *            dijete koje dodaje u privatnu kolekciju.
	 */
	public void addChildNode(Node child) {

		if (!canAdd) {
			col = new ArrayList<>();
			canAdd = true;
			col.add(child);
		} else {
			col.add(child);
		}

	}

	/**
	 * Gleda broj djece.
	 * 
	 * @return taj broj.
	 */
	public int numberOfChildren() {
		if (!canAdd)
			return 0;
		else
			return col.size();
	}

	/**
	 * Vraća dijete koje tražimo.
	 * 
	 * @param index
	 *            pozicija djeteta.
	 * @return to dijete.
	 */
	public Node getChild(int index) {
		return (Node) col.get(index);
	}

	/**
	 * Metoda koja će pozivati konkretnu posjetitelj-metodu nad samim sobom kao
	 * argumentom.
	 * 
	 * @param visitor
	 *            pojetitelj.
	 */
	public abstract void accept(INodeVisitor visitor);

	/**
	 * @see java.lang.Object#toString()
	 */
	public abstract String toString();
}
