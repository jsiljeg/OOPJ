package hr.fer.zemris.java.custom.scripting.functions;

import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.exec.StackRequestContext;
import hr.fer.zemris.java.custom.scripting.exec.ValueWrapper;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Radi se o funkciji sinus: sin(x). Konceptualno, radi se sljedeće: x = pop(),
 * r = sin(x), push(r).
 * 
 * @author Jure Šiljeg
 *
 */
public class SinFunImpl implements IFunction {

	/**
	 * @see hr.fer.zemris.java.custom.scripting.functions.IFunction#execute(hr.fer.zemris.java.custom.collections.ObjectStack,
	 *      hr.fer.zemris.java.webserver.RequestContext)
	 */
	@Override
	public StackRequestContext execute(ObjectStack tmpStack, RequestContext requestContext) {
		double val = 0;
		try {
			double num = Double.parseDouble(String.valueOf(((ValueWrapper) tmpStack.pop()).getValue()));
			// traži se kao da nam je u stupnjevima zadano, a Math.sin kao
			// argument prima kut u radijanima
			val = Math.sin(Math.toRadians(num));
		} catch (NumberFormatException e) {
			System.out.println("Nepodržan format argumenta u izvršavanju metode sinus!");
			System.exit(-1);
		}
		tmpStack.push(new ValueWrapper(val));
		return new StackRequestContext(tmpStack, requestContext);
	}

}
