package hr.fer.zemris.java.webserver.workers;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Razred koji implementira {@link IWebWorker} sučelje, a iscrtava mali krug
 * lociran na pravokutniku 200x200px u sredinu.
 * 
 * @author Jure Šiljeg
 *
 */
public class CircleWorker implements IWebWorker {

	/**
	 * @see hr.fer.zemris.java.webserver.IWebWorker#processRequest(hr.fer.zemris.java.webserver.RequestContext)
	 */
	@Override
	public void processRequest(RequestContext context) {
		BufferedImage bim = new BufferedImage(200, 200, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = bim.createGraphics();
		// do drawing...
		Ellipse2D.Double circle = new Ellipse2D.Double(90, 90, 15, 15);
		g2d.fill(circle);
		// ha - ha end drawing
		g2d.dispose();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bim, "png", bos);
			context.setMimeType("image/png");
			context.write(bos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
