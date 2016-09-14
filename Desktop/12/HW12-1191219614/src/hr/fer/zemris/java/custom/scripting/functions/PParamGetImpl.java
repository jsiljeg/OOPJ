package hr.fer.zemris.java.custom.scripting.functions;

import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.exec.StackRequestContext;
import hr.fer.zemris.java.custom.scripting.exec.ValueWrapper;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Odgovara funkciji: pparamGet(name, defValue). Radi isto što i
 * {@link ParamGetImpl}, samo to radi nad persistentParameter mapi.
 * 
 * @author Jure Šiljeg
 *
 */
public class PParamGetImpl implements IFunction {

	/**
	 * @see hr.fer.zemris.java.custom.scripting.functions.IFunction#execute(hr.fer.zemris.java.custom.collections.ObjectStack,
	 *      hr.fer.zemris.java.webserver.RequestContext)
	 */
	@Override
	public StackRequestContext execute(ObjectStack tmpStack, RequestContext requestContext) {
		Object defaultValue = ((ValueWrapper) tmpStack.pop()).getValue();
		Object name = ((ValueWrapper) tmpStack.pop()).getValue();
		String value = requestContext.getPersistentParameter(String.valueOf(name));

		tmpStack.push(value == null ? new ValueWrapper(defaultValue) : new ValueWrapper(value));

		return new StackRequestContext(tmpStack, requestContext);
	}

}
