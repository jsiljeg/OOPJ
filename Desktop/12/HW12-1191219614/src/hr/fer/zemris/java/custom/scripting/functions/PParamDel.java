package hr.fer.zemris.java.custom.scripting.functions;

import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.exec.StackRequestContext;
import hr.fer.zemris.java.custom.scripting.exec.ValueWrapper;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Radi se o sljedećoj akciji: pparamDel(name). Uklanja vrijednost određenu
 * varijablom name iz mape persistentParameters konteksta zahtjeva.
 * 
 * @author Jure Šiljeg
 *
 */
public class PParamDel implements IFunction {

	/**
	 * @see hr.fer.zemris.java.custom.scripting.functions.IFunction#execute(hr.fer.zemris.java.custom.collections.ObjectStack,
	 *      hr.fer.zemris.java.webserver.RequestContext)
	 */
	@Override
	public StackRequestContext execute(ObjectStack tmpStack, RequestContext requestContext) {
		Object name = ((ValueWrapper) tmpStack.pop()).getValue();
		requestContext.removePersistentParameter(String.valueOf(name));

		return new StackRequestContext(tmpStack, requestContext);
	}

}
