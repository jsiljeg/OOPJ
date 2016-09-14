package hr.fer.zemris.java.custom.scripting.functions;

import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.exec.StackRequestContext;
import hr.fer.zemris.java.custom.scripting.exec.ValueWrapper;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Uzima string x i poziva requestContext.setMimeType(x).
 * 
 * @author Jure Šiljeg
 *
 */
public class MimeTypeFunImpl implements IFunction {

	/**
	 * @see hr.fer.zemris.java.custom.scripting.functions.IFunction#execute(hr.fer.zemris.java.custom.collections.ObjectStack,
	 *      hr.fer.zemris.java.webserver.RequestContext)
	 */
	@Override
	public StackRequestContext execute(ObjectStack tmpStack, RequestContext requestContext) {
		String mimeType = String.valueOf(((ValueWrapper) tmpStack.pop()).getValue());
		requestContext.setMimeType(mimeType);

		return new StackRequestContext(tmpStack, requestContext);
	}

}
