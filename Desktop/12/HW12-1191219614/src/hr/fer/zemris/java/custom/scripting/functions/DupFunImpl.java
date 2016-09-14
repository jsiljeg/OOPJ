package hr.fer.zemris.java.custom.scripting.functions;

import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.exec.StackRequestContext;
import hr.fer.zemris.java.custom.scripting.exec.ValueWrapper;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Duplicira trenutnu vrijednost s vrha stoga. Konceptualno se događa sljedeće:
 * x = pop(), push(x), push(x).
 * 
 * @author Jure Šiljeg
 *
 */
public class DupFunImpl implements IFunction {

	/**
	 * @see hr.fer.zemris.java.custom.scripting.functions.IFunction#execute(hr.fer.zemris.java.custom.collections.ObjectStack,
	 *      hr.fer.zemris.java.webserver.RequestContext)
	 */
	@Override
	public StackRequestContext execute(ObjectStack tmpStack, RequestContext requestContext) {
		Object val = ((ValueWrapper) tmpStack.pop()).getValue();
		tmpStack.push(new ValueWrapper(val));
		tmpStack.push(new ValueWrapper(val));
		return new StackRequestContext(tmpStack, requestContext);
	}

}
