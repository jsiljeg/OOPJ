package hr.fer.zemris.java.custom.scripting.functions;

import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.exec.StackRequestContext;
import hr.fer.zemris.java.custom.scripting.exec.ValueWrapper;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Odgovara funkciji: pparamSet(value, name). Konceptualno, radi se o sljedećem:
 * name = pop(), value = pop(), requestContext.setPerParam(name, value).
 * 
 * @author Jure Šiljeg
 *
 */
public class PParamSet implements IFunction {

	/**
	 * @see hr.fer.zemris.java.custom.scripting.functions.IFunction#execute(hr.fer.zemris.java.custom.collections.ObjectStack,
	 *      hr.fer.zemris.java.webserver.RequestContext)
	 */
	@Override
	public StackRequestContext execute(ObjectStack tmpStack, RequestContext requestContext) {
		Object name = ((ValueWrapper) tmpStack.pop()).getValue();
		Object value = ((ValueWrapper) tmpStack.pop()).getValue();
		requestContext.setPersistentParameter(String.valueOf(name), String.valueOf(value));
		return new StackRequestContext(tmpStack, requestContext);
	}

}
