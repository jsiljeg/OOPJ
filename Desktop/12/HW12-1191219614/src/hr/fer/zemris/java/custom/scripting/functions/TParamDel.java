package hr.fer.zemris.java.custom.scripting.functions;

import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.exec.StackRequestContext;
import hr.fer.zemris.java.custom.scripting.exec.ValueWrapper;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Funkcija izgleda ovako: tparamDel(name). Radi isto što i {@link PParamDel},
 * samo to čini nad mapom temporaryParameters.
 * 
 * @author Jure Šiljeg
 *
 */
public class TParamDel implements IFunction {

	/**
	 * @see hr.fer.zemris.java.custom.scripting.functions.IFunction#execute(hr.fer.zemris.java.custom.collections.ObjectStack,
	 *      hr.fer.zemris.java.webserver.RequestContext)
	 */
	@Override
	public StackRequestContext execute(ObjectStack tmpStack, RequestContext requestContext) {
		Object name = ((ValueWrapper) tmpStack.pop()).getValue();
		requestContext.removeTemporaryParameter(String.valueOf(name));

		return new StackRequestContext(tmpStack, requestContext);
	}

}
