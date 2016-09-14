package hr.fer.zemris.java.raytracer;

import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Razred za simulaciju neparaleliziranog renderiranja scene s objektima i
 * izvorima svjetlosti.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class RayCaster {
	/**
	 * Glavna metoda koja simulira paralelizirani rad..
	 * 
	 * @param args
	 *            argumenti komande linije (nema ih).
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), new Point3D(10, 0, 0), new Point3D(0, 0, 0),
				new Point3D(0, 0, 10), 20, 20);
	}

	/**
	 * Getter za produkciju tragača zrake.
	 * 
	 * @return Objekt tipa IRayTracerProducer.
	 */
	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {
			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp, double horizontal, double vertical,
					int width, int height, long requestNo, IRayTracerResultObserver observer) {
				long startingTime = System.nanoTime();

				System.out.println("Započinjem izračune...");

				short[] red = new short[width * height];
				short[] green = new short[width * height];
				short[] blue = new short[width * height];

				Point3D yAxis = initializeYAxis(eye, view, viewUp);
				Point3D xAxis = view.sub(eye).normalize().vectorProduct(yAxis).normalize();
				@SuppressWarnings("unused")
				Point3D zAxis = xAxis.vectorProduct(yAxis);

				Point3D screenCorner = view.sub(xAxis.scalarMultiply(horizontal / 2.))
						.add(yAxis.scalarMultiply(vertical / 2.));
				Scene scene = RayTracerViewer.createPredefinedScene();

				short[] rgb = new short[3];
				int offset = 0;

				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						Point3D screenPoint = screenCorner
								.add(xAxis.scalarMultiply(((double) x / (double) (width - 1) * horizontal)))
								.sub(yAxis.scalarMultiply(((double) y / ((double) height - 1) * vertical)));
						Ray ray = Ray.fromPoints(eye, screenPoint);

						rgb = tracer(scene, ray, rgb);

						red[offset] = rgb[0] > 255 ? 255 : rgb[0];
						green[offset] = rgb[1] > 255 ? 255 : rgb[1];
						blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
						offset++;
					}
				}
				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
				double estimatedTime = (System.nanoTime() - startingTime) / 1_000_000;
				System.out.println("Vrijeme u neparaleliziranom načinu rada: " + estimatedTime + "ms");
			}

			/**
			 * Metoda koja služi za inicijalizaciju y-osi.
			 * 
			 * @param eye
			 *            Očište.
			 * @param view
			 *            Gledište.
			 * @param viewUp
			 *            vektor koji pomaže za fiksiranje y-osi. Ne smije biti
			 *            kolinearan s vektorom OG i ne mora ležati u ravnini
			 *            xy. Njegova projekcija pomaže pri određivanju y-osi.
			 * @return Smjer koji označava "prostiranje" y-osi.
			 */
			private Point3D initializeYAxis(Point3D eye, Point3D view, Point3D viewUp) {
				// OG vektor normaliziran
				Point3D eyeView = view.sub(eye).normalize();
				// VUV normaliziran
				viewUp.modifyNormalize();

				// OG * VUV skalarno
				double scalarOGVUV = eyeView.scalarProduct(viewUp);
				// j'=VUV−OG(OG * VUV )
				// j = j' / ||j'||
				return viewUp.sub(eyeView.scalarMultiply(scalarOGVUV)).normalize();
			}

			/**
			 * Metoda zadužena za pronalaženja boje piksela u bilo kojem
			 * slučaju. Implementiran je algoritam iz pseudokoda:
			 * 
			 * <pre>
			 *   
			 *   
			 *   odredi najbliže presjecište S između zrake ray i bilo kojeg objekta na sceni
			 *   ako presjecište ne postoji
			 *   	| oboji piksel (x, y) u rgb(0, 0, 0)
			 *   inače
			 *   	| pozovi metodu determineColor(S) i odredi boju (x, y)
			 *   kraj uvjeta
			 *   vrati boju piksela (x, y)
			 * </pre>
			 * 
			 * @param scene
			 *            Scena.
			 * @param ray
			 *            Zraka.
			 * @param rgb
			 *            Polje koje sadrži intezitete tonova boja. Njega ćemo
			 *            napuniti i vratiti.
			 * @return Obojani piksel.
			 */
			private short[] tracer(Scene scene, Ray ray, short[] rgb) {
				Arrays.fill(rgb, (short) 0);
				List<GraphicalObject> objects = scene.getObjects();
				RayIntersection closestIntersection = findClosestIntersection(objects, ray);
				if (closestIntersection == null) {
					return rgb;
				} else {
					return determineColor(closestIntersection, scene, ray, rgb);
				}
			}

			/**
			 * Metoda pronalazi najbliže presjecište zrake koja ide iz očišta i
			 * predmeta na sceni.
			 * 
			 * @param objects
			 *            Predmeti na sceni.
			 * @param ray
			 *            Zraka iz očišta.
			 * @return Najbliže presjecište koje se vidi iz očišta.
			 */
			private RayIntersection findClosestIntersection(List<GraphicalObject> objects, Ray ray) {
				double minDistance = Double.MAX_VALUE;
				RayIntersection closestIntersection = null;
				for (GraphicalObject obj : objects) {
					RayIntersection tempClosestIntersection = obj.findClosestRayIntersection(ray);
					if (tempClosestIntersection != null) {
						double tempDistance = tempClosestIntersection.getDistance();
						if (tempDistance < minDistance) {
							minDistance = tempDistance;
							closestIntersection = tempClosestIntersection;
						}
					}
				}
				return closestIntersection;
			}

			/**
			 * Metoda koja određuje boju piksela ako objekt nije sakriven. To
			 * radimo po sljedećem algoritmu:
			 * 
			 * <pre>
			 *   postavi boju piksela (x, y) na rgb(0, 0, 0)
			 *   za svaki izvor svjetla
			 *   | definiraj zraku r' od izvora do presjecišta S
			 *   | pronađi najbliže presjecište S' od zrake r' do bilo kojeg obekta na sceni 
			 *   | ako S' postoji i bliže je izvoru nego S
			 *   |	| preskoči ovaj izvor
			 *   | inače
			 *   | 	| boja piksela += difuzna komponenta + zrcalna komponenta
			 *   kraj petlje
			 * </pre>
			 * 
			 * @param closestIntersection
			 *            Presjecište zrake iz očišta i predmeta na sceni.
			 * @param scene
			 *            Scena.
			 * @param ray
			 *            Zraka iz očišta.
			 * @param rgb
			 *            Polje koje sadrži intezitete tonova boja. Njega ćemo
			 *            napuniti i vratiti.
			 * @return Obojani piksel.
			 */
			private short[] determineColor(RayIntersection closestIntersection, Scene scene, Ray ray, short[] rgb) {
				Arrays.fill(rgb, (short) 15);
				List<LightSource> lights = scene.getLights();
				List<GraphicalObject> objects = scene.getObjects();

				short[] diffuseRgb = new short[3];
				short[] reflectiveRgb = new short[3];

				for (LightSource ls : lights) {
					Ray r = Ray.fromPoints(ls.getPoint(), closestIntersection.getPoint());
					RayIntersection closestIntersectionWithLightSource = findClosestIntersection(objects, r);

					double lightIntersectionDistance = 0;
					double intersectionDistance = 0;

					if (closestIntersectionWithLightSource != null) {
						lightIntersectionDistance = closestIntersectionWithLightSource.getDistance();
						intersectionDistance = closestIntersection.getPoint().sub(ls.getPoint()).norm();
					}

					if (closestIntersectionWithLightSource != null
							&& lightIntersectionDistance + 0.01 < intersectionDistance) {

						continue;
					} else {

						Point3D normal = closestIntersection.getNormal().normalize();
						Point3D toLight = ls.getPoint().sub(closestIntersection.getPoint()).normalize();
						Point3D toEye = ray.start.sub(closestIntersection.getPoint()).normalize();
						Point3D reflected = normal.scalarMultiply(toLight.scalarProduct(normal) * 2).modifySub(toLight)
								.normalize();

						double cosineDiffuse = limittedCosineDiffuse(normal, toLight);
						double cosineReflected = limittedCosineReflected(toEye, reflected, closestIntersection);

						// difuzna komponenta
						diffuseRgb[0] = (short) (closestIntersection.getKdr() * ls.getR() * cosineDiffuse);
						diffuseRgb[1] = (short) (closestIntersection.getKdg() * ls.getG() * cosineDiffuse);
						diffuseRgb[2] = (short) (closestIntersection.getKdb() * ls.getB() * cosineDiffuse);

						// zrcalna komponenta
						reflectiveRgb[0] = (short) (closestIntersection.getKrr() * ls.getR() * cosineReflected);
						reflectiveRgb[1] = (short) (closestIntersection.getKrg() * ls.getG() * cosineReflected);
						reflectiveRgb[2] = (short) (closestIntersection.getKrb() * ls.getB() * cosineReflected);

						// ambijentna + difuzna + zrcalna
						rgb[0] += (diffuseRgb[0] + reflectiveRgb[0]);
						rgb[1] += (diffuseRgb[1] + reflectiveRgb[1]);
						rgb[2] += (diffuseRgb[2] + reflectiveRgb[2]);
					}
				}
				return rgb;
			}

			/**
			 * Metoda računa kosinus kuta između reflektirane zrake i zrake
			 * prema promatraču.
			 * 
			 * @param toEye
			 *            zraka prema promatraču (normirana).
			 * @param reflected
			 *            reflektirana zraka (normirana).
			 * @param closestIntersection
			 *            točka presjecišta iz koje pucamo zrake.
			 * @return kosinus kuta koji mora biti nenegativan.
			 */
			private double limittedCosineReflected(Point3D toEye, Point3D reflected,
					RayIntersection closestIntersection) {
				double maximize = Math.max(reflected.scalarProduct(toEye), 0);
				return Math.pow(maximize, closestIntersection.getKrn());
			}

			/**
			 * Metoda računa kosinus kuta između normale na presjecište i zrake
			 * koja ide prema izvoru svjetla.
			 * 
			 * @param normal
			 *            normala iz presjecišta.
			 * @param toLight
			 *            zraka koja ide prema svjetlosnom izvoru.
			 * @return kosinus kuta koji mora biti nenegativan.
			 */
			private double limittedCosineDiffuse(Point3D normal, Point3D toLight) {
				return Math.max(toLight.scalarProduct(normal), 0);
			}

		};
	}

}
