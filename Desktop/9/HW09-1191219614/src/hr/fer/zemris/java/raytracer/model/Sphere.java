package hr.fer.zemris.java.raytracer.model;

/**
 * Razred koji predstavlja sferu.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class Sphere extends GraphicalObject {

	/** Točka predstavlja središte sfere. */
	private Point3D center;
	/** Radijus sfere. */
	private double radius;
	/** Koeficjent za difuznu komponentu crvenog svjetla. */
	private double kdr;
	/** Koeficjent za difuznu komponentu zelenog svjetla. */
	private double kdg;
	/** Koeficjent za difuznu komponentu plavog svjetla. */
	private double kdb;
	/** Koeficjent za zrcalnu komponentu crvenog svjetla. */
	private double krr;
	/** Koeficjent za zrcalnu komponentu zelenog svjetla. */
	private double krg;
	/** Koeficjent za zrcalnu komponentu plavog svjetla. */
	private double krb;
	/** Koeficjent n zrcalnu komponentu. */
	private double krn;

	/**
	 * Konstruktor sfere. Inicializirau se sve bitne točke.
	 * 
	 * @param center
	 *            Točka predstavlja središte sfere.
	 * @param radius
	 *            Radijus sfere.
	 * @param kdr
	 *            Koeficjent za difuznu komponentu crvenog svjetla.
	 * @param kdg
	 *            Koeficjent za difuznu komponentu zelenog svjetla.
	 * @param kdb
	 *            Koeficjent za difuznu komponentu plavog svjetla.
	 * @param krr
	 *            Koeficjent za zrcalnu komponentu crvenog svjetla.
	 * @param krg
	 *            Koeficjent za zrcalnu komponentu zelenog svjetla.
	 * @param krb
	 *            Koeficjent za zrcalnu komponentu plavog svjetla.
	 * @param krn
	 *            Koeficjent n zrcalnu komponentu.
	 */
	public Sphere(Point3D center, double radius, double kdr, double kdg, double kdb, double krr, double krg, double krb,
			double krn) {
		super();
		this.center = center;
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}

	/**
	 * @see hr.fer.zemris.java.raytracer.model.GraphicalObject#findClosestRayIntersection(hr.fer.zemris.java.raytracer.model.Ray)
	 */
	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {
		// ograničenje zadatka je da je promatrač izvana, pa će bliža točka
		// presjecišta uvijek nastati kada dolazimo objektu "s vanjske strane"
		boolean outer = true;
		double distance;

		// konstruktor ne normalizira smjer, pa moramo ručno
		ray.direction.modifyNormalize();

		double xd = ray.direction.x;
		double yd = ray.direction.y;
		double zd = ray.direction.z;

		double x0 = ray.start.x;
		double y0 = ray.start.y;
		double z0 = ray.start.z;

		double[] parametersT = getParametersForSomePoint(this, ray, xd, yd, zd, x0, y0, z0);
		// diskriminanta < 0, pa nema presjeka
		if (parametersT == null) {
			return null;
		}

		// Na zraci start je na (x0, y0, z0), normalizirani vektor smjera je
		// (xd, yd, zd) i parametar t za računanje točaka. Točka presjeka ima
		// koordinate (X, Y, Z).
		// X = x0 + xd*t
		// Y = y0 + yd*t
		// Z = z0 + zd*t

		Point3D intersectionPoint;
		Point3D intersectionPoint1 = new Point3D(x0 + xd * parametersT[0], y0 + yd * parametersT[0],
				z0 + zd * parametersT[0]);
		Point3D intersectionPoint2 = new Point3D(x0 + xd * parametersT[1], y0 + yd * parametersT[1],
				z0 + zd * parametersT[1]);

		// zraka je tangenta
		if (intersectionPoint1.equals(intersectionPoint2)) {
			distance = (ray.start.sub(intersectionPoint1.x, intersectionPoint1.y, intersectionPoint1.z)).norm();
			intersectionPoint = intersectionPoint1;
		} else { // 2 puta siječe sferu
			double distance1 = (ray.start.sub(intersectionPoint1.x, intersectionPoint1.y, intersectionPoint1.z)).norm();
			double distance2 = (ray.start.sub(intersectionPoint2.x, intersectionPoint2.y, intersectionPoint2.z)).norm();
			distance = distance1 < distance2 ? distance1 : distance2;
			intersectionPoint = distance1 < distance2 ? intersectionPoint1 : intersectionPoint2;
		}
		RayIntersection intersection = new RayIntersection(intersectionPoint, distance, outer) {

			@Override
			public Point3D getNormal() {
				return intersectionPoint.sub(center);
			}

			@Override
			public double getKrr() {
				return krr;
			}

			@Override
			public double getKrn() {
				return krn;
			}

			@Override
			public double getKrg() {
				return krg;
			}

			@Override
			public double getKrb() {
				return krb;
			}

			@Override
			public double getKdr() {
				return kdr;
			}

			@Override
			public double getKdg() {
				return kdg;
			}

			@Override
			public double getKdb() {
				return kdb;
			}
		};
		return intersection;
	}

	/**
	 * Metoda koja radi algoritam za pronalaženje presjecišta sfere i zrake
	 * pomoću parametra t po uputama odavde:
	 * http://stackoverflow.com/questions/1986378/how-to-set-up-quadratic-
	 * equation-for-a-ray-sphere-intersection
	 * 
	 * @param sphere
	 *            Sfera.
	 * @param ray
	 *            Zraka iz očišta.
	 * @param xd
	 *            x-komponenta vektora smjera zrake ray.
	 * @param yd
	 *            y-komponenta vektora smjera zrake ray.
	 * @param zd
	 *            z-komponenta vektora smjera zrake ray.
	 * @param x0
	 *            x-koordinata točke iz koje počinje zraka ray.
	 * @param y0
	 *            y-koordinata točke iz koje počinje zraka ray.
	 * @param z0
	 *            z-koordinata točke iz koje počinje zraka ray.
	 * @return polje koje sadrži parametre t.
	 */
	private double[] getParametersForSomePoint(Sphere sphere, Ray ray, double xd, double yd, double zd, double x0,
			double y0, double z0) {

		double a = sphere.center.x;
		double b = sphere.center.y;
		double c = sphere.center.z;
		double r = sphere.radius;

		double A = xd * xd + yd * yd + zd * zd;
		double B = (2 * (xd * (x0 - a) + yd * (y0 - b) + zd * (z0 - c)));
		double C = ((x0 - a) * (x0 - a) + (y0 - b) * (y0 - b) + (z0 - c) * (z0 - c) - r * r);

		double discriminant = B * B - 4 * A * C;
		if (discriminant < 0) {
			return null;
		} else {
			double[] field = new double[2];
			field[0] = (-B + Math.sqrt(discriminant)) / (2 * A);
			field[1] = (-B - Math.sqrt(discriminant)) / (2 * A);
			return field;
		}
	}
}
