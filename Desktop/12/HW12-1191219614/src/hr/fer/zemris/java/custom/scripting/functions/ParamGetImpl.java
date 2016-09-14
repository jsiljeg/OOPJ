package hr.fer.zemris.java.custom.scripting.functions;

import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.exec.StackRequestContext;
import hr.fer.zemris.java.custom.scripting.exec.ValueWrapper;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Konceptualno radi sljedeće: dv = pop(), name = pop(),
 * value=reqCtx.getParam(name). *
 * 
 * @author Jure Šiljeg
 *
 */
public class ParamGetImpl implements IFunction {

	/**
	 * @see hr.fer.zemris.java.custom.scripting.functions.IFunction#execute(hr.fer.zemris.java.custom.collections.ObjectStack,
	 *      hr.fer.zemris.java.webserver.RequestContext)
	 */
	@Override
	public StackRequestContext execute(ObjectStack tmpStack, RequestContext requestContext) {
		Object defaultValue = ((ValueWrapper) tmpStack.pop()).getValue();
		Object name = ((ValueWrapper) tmpStack.pop()).getValue();
		Object value = requestContext.getParameter(String.valueOf(name));

		tmpStack.push(value == null ? new ValueWrapper(defaultValue) : new ValueWrapper(value));

		return new StackRequestContext(tmpStack, requestContext);
	}

}
