package hr.fer.zemris.java.webserver.workers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Razred koji implementira {@link IWebWorker} sučelje, zadužen je za ispisvanje
 * trenutnog vremena, poruku dobrodošlice te duljinu imena koje je proslijeđeno
 * (ako je proslijeđeno), inače upozorava korisnika na to.
 * 
 * @author Jure Šiljeg
 *
 */
public class HelloWorker implements IWebWorker {

	/**
	 * @see hr.fer.zemris.java.webserver.IWebWorker#processRequest(hr.fer.zemris.java
	 *      .webserver.RequestContext)
	 */
	@Override
	public void processRequest(RequestContext context) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		context.setMimeType("text/html");
		String name = context.getParameter("name");
		try {
			context.write("<html><body>");
			context.write("<h1>Hello!!!</h1>");
			context.write("<p>Now is: " + sdf.format(now) + "</p>");
			if (name == null || name.trim().isEmpty()) {
				context.write("<p>You did not send me your name!</p>");
			} else {
				context.write("<p>Your name has " + name.trim().length() + " letters.</p>");
			}
			context.write("</body></html>");
		} catch (IOException ex) {
			// Log exception to servers log...
			ex.printStackTrace();
		}

	}

}
