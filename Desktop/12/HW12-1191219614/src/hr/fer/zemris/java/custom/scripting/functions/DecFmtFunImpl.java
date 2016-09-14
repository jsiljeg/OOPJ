package hr.fer.zemris.java.custom.scripting.functions;

import java.text.DecimalFormat;

import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.exec.StackRequestContext;
import hr.fer.zemris.java.custom.scripting.exec.ValueWrapper;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Razred kojim modeliramo funkciju decfmt(x,f). Konceptualno radi sljedeće:
 * Formatira decimalni broj koristeći zadani format f koji je kompatibilan s
 * DecimalFormat. Proizvodi string. X može biti integer, double ili string
 * reprezentacija broja. Događa se sljedeće: f = pop(), x = pop(), r =
 * decfmt(x,f), push(r).
 * 
 * @author Jure Šiljeg
 *
 */
public class DecFmtFunImpl implements IFunction {

	/**
	 * @see hr.fer.zemris.java.custom.scripting.functions.IFunction#execute(hr.fer.zemris.java.custom.collections.ObjectStack,
	 *      hr.fer.zemris.java.webserver.RequestContext)
	 */
	@Override
	public StackRequestContext execute(ObjectStack tmpStack, RequestContext requestContext) {
		String f = String.valueOf(((ValueWrapper) tmpStack.pop()).getValue());
		String x = String.valueOf(((ValueWrapper) tmpStack.pop()).getValue());

		DecimalFormat decimalFormat = new DecimalFormat(f);

		String format = decimalFormat.format(Double.parseDouble(x));
		tmpStack.push(new ValueWrapper(format));
		return new StackRequestContext(tmpStack, requestContext);
	}

}
