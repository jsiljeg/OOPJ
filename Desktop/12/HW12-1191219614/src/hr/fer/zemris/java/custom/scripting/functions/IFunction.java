package hr.fer.zemris.java.custom.scripting.functions;

import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.exec.StackRequestContext;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Sučelje kojim ćemo modelirati strategiju za izvršavanje pojedine funkcije.
 * 
 * @author Jure Šiljeg
 *
 */
public interface IFunction {

	/**
	 * Metoda za izvršavanje funkcije. Opis akcije i izvršavanja se nalazi u
	 * opisu razreda koji implementira sučelje {@link IFunction}
	 * 
	 * @param tmpStack
	 *            stog s vrijednostima varijabli u sebi.
	 * @param requestContext
	 *            kontekst zahtjeva.
	 * @return StackRequestContext koji sadrži prerađene i sačuvane argumente u
	 *         skladu s funkcijom.
	 */
	public StackRequestContext execute(ObjectStack tmpStack, RequestContext requestContext);

}
