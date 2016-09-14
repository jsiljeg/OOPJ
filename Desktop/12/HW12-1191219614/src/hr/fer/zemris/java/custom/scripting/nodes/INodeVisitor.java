package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Sučelje pomoću kojeg modeliramo posjetitelja pojedinom čvoru.
 * 
 * @author Jure Šiljeg
 *
 */
public interface INodeVisitor {
	/**
	 * Obavlja se posjeta tekstualnom čvoru.
	 * 
	 * @param node
	 *            taj čvor.
	 */
	public void visitTextNode(TextNode node);

	/**
	 * Obavlja se posjeta čvoru koji predstavlja for petlju.
	 * 
	 * @param node
	 *            taj čvor.
	 */
	public void visitForLoopNode(ForLoopNode node);

	/**
	 * Obavlja se posjeta čvoru koji predstavlja echo čvor.
	 * 
	 * @param node
	 *            taj čvor.
	 */
	public void visitEchoNode(EchoNode node);

	/**
	 * Obavlja se posjeta glanom čvoru (korijenu). Sadrži se sebi stablasto
	 * razapetu svu djecu.
	 * 
	 * @param node
	 *            taj čvor.
	 */
	public void visitDocumentNode(DocumentNode node);
}
