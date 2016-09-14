package hr.fer.zemris.java.custom.scripting.exec;

import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Razred kojim modeliramo objekt koji sadrži kontekst zahtjeva i sam privremeni
 * stog. Potreban je kao povratni tip strategiji jer nekad radimo nad stogom, a
 * nekad nad kontekstom zahtjeva, a ovakavobjekt će upravo čuvati sve promjene
 * koje nas uopće i zanimaju.
 * 
 * @author Jure Šiljeg
 *
 */
public class StackRequestContext {
	/** Stog s vrijednostima. */
	private ObjectStack tmpStack;
	/** Kontekst zahtjeva. */
	private RequestContext requestContext;

	/**
	 * Konsturktor za inicjalizaciju članskih varijabli objekta.
	 * 
	 * @param tmpStack
	 *            Stog s vrijednostima.
	 * @param requestContext
	 *            Kontekst zahtjeva.
	 */
	public StackRequestContext(ObjectStack tmpStack, RequestContext requestContext) {
		super();
		this.tmpStack = tmpStack;
		this.requestContext = requestContext;
	}

	/**
	 * Getter za stog.
	 * 
	 * @return traženi stog.
	 */
	public ObjectStack getTmpStack() {
		return tmpStack;
	}

	/**
	 * Getter za kontekst zahtjeva.
	 * 
	 * @return traženi zahtjev.
	 */
	public RequestContext getRequestContext() {
		return requestContext;
	}
}
