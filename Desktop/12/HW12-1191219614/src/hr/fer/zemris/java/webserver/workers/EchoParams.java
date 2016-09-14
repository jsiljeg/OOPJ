package hr.fer.zemris.java.webserver.workers;

import java.io.IOException;
import java.util.Set;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Razred koji implementira {@link IWebWorker} sučelje, zadužen je za ispivanje
 * parametara u tablicu koju implicitno zadamo kroz URL. Zadaje se ime i
 * vrijednost i oboje se ispisuje u tablicu.
 * 
 * @author Jure Šiljeg
 *
 */
public class EchoParams implements IWebWorker {

	/**
	 * @see hr.fer.zemris.java.webserver.IWebWorker#processRequest(hr.fer.zemris.java.webserver.RequestContext)
	 */
	@Override
	public void processRequest(RequestContext context) {
		context.setMimeType("text/html");
		try {
			context.write("<html><body>");
			context.write("<h2>Tablica: </h2>");
			context.write("<table border=4 style=\"width:60%\">");
			context.write("<tr><th>Ime</th>");
			Set<String> paramNames = context.getParameterNames();
			for (String param : paramNames) {
				context.write("<td>" + param + "</td>");
			}
			context.write("</tr>");
			context.write("<tr><th>Value</th>");
			for (String paramName : paramNames) {
				context.write("<td>" + context.getParameter(paramName) + "</td>");
			}
			context.write("</tr>");
			context.write("</table>");
			context.write("</body></html>");
		} catch (IOException ex) {
			// Log exception to servers log...
			ex.printStackTrace();
		}

	}

}
