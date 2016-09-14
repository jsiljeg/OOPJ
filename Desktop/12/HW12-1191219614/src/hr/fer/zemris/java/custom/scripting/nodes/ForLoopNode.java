package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

/**
 * Razred koji reprezentira čvor koji predstavlja pojedinu for petlju.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class ForLoopNode extends Node {
	/** varijabla. */
	private ElementVariable variable;
	/** početni izraz ufor petlji. */
	private Element startExpression;
	/** završni dio for petlje. */
	private Element endExpression;
	/** korak for petlje. */
	private Element stepExpression;

	/**
	 * Konstruktor.
	 * 
	 * @param variable
	 *            prvi argument za inicijalizaciju koji mora biti varijabla
	 * @param startExpression
	 *            drugi arg za inicijaliziranje koji može biti string, broj ili
	 *            varijabla
	 * @param endExpression
	 *            treći arg za inicijaliziranje koji može biti string, broj ili
	 *            varijabla
	 * @param stepExpression
	 *            četvrti arg za inicijaliziranje koji može biti string, broj,
	 *            varijabla ili null
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression,
			Element stepExpression) {
		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
	}

	/**
	 * Konsturktor bez step-a. Delegira izvršavanje osnovnom konstruktoru.
	 * 
	 * @param variable
	 *            prvi argument za inicijalizaciju koji mora biti varijabla
	 * @param startExpression
	 *            drugi arg za inicijaliziranje koji može biti string, broj ili
	 *            varijabla
	 * @param endExpression
	 *            treći arg za inicijaliziranje koji može biti string, broj ili
	 *            varijabla
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression) {
		this(variable, startExpression, endExpression, null);
	}

	/**
	 * Getter za prvu var.
	 * 
	 * @return tu var.
	 */
	public ElementVariable getVariable() {
		return variable;
	}

	/**
	 * Getter za drugu var.
	 * 
	 * @return tu var.
	 */
	public Element getStartExpression() {
		return startExpression;
	}

	/**
	 * Getter za treću var.
	 * 
	 * @return tu var.
	 */
	public Element getEndExpression() {
		return endExpression;
	}

	/**
	 * Getter za četvrtu var.
	 * 
	 * @return tu var.
	 */
	public Element getStepExpression() {
		return stepExpression;
	}

	/**
	 * @see hr.fer.zemris.java.custom.scripting.nodes.Node#accept(hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor)
	 */
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitForLoopNode(this);
	}

	/**
	 * @see hr.fer.zemris.java.custom.scripting.nodes.Node#toString()
	 */
	@Override
	public String toString() {
		String str = "{$ FOR";
		str += " " + getVariable().asText();
		str += " " + getStartExpression().asText();
		str += " " + getEndExpression().asText();
		if (getStepExpression() == null) {
			str += " ";
		} else {
			String step = " " + getStepExpression().asText() + " ";
			str += step;
		}
		str += "$}";
		return str;
	}

}
