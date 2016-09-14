package hr.fer.zemris.java.custom.scripting.functions;

import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.exec.StackRequestContext;
import hr.fer.zemris.java.custom.scripting.exec.ValueWrapper;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Radi se o funkciji zamjene elemenata s vrha stoga: swap(). Konceptualno, to
 * izgleda ovako: a = pop(), b = pop(), push(a), push(b).
 * 
 * @author Jure Šiljeg
 *
 */
public class SwapFunImpl implements IFunction {

	/**
	 * @see hr.fer.zemris.java.custom.scripting.functions.IFunction#execute(hr.fer.zemris.java.custom.collections.ObjectStack,
	 *      hr.fer.zemris.java.webserver.RequestContext)
	 */
	@Override
	public StackRequestContext execute(ObjectStack tmpStack, RequestContext requestContext) {
		Object a = ((ValueWrapper) tmpStack.pop()).getValue();
		Object b = ((ValueWrapper) tmpStack.pop()).getValue();
		tmpStack.push(new ValueWrapper(a));
		tmpStack.push(new ValueWrapper(b));
		return new StackRequestContext(tmpStack, requestContext);
	}

}
