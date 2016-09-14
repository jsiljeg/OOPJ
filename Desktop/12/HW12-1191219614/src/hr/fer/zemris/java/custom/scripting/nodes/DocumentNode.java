package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Razred koji predstavlja čitav dokument.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class DocumentNode extends Node {

	/**
	 * @see hr.fer.zemris.java.custom.scripting.nodes.Node#accept(hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor)
	 */
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitDocumentNode(this);
	}

	/**
	 * @see hr.fer.zemris.java.custom.scripting.nodes.Node#toString()
	 */
	@Override
	public String toString() {
		return "";
	}

}
