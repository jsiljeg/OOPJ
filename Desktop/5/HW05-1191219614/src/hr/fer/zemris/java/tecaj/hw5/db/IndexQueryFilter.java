package hr.fer.zemris.java.tecaj.hw5.db;

/**
 * Pomoćni razred za dohvaćanje indexqueryja. Tu je isključivo radi
 * jednostavnosti testiranja JUnit testova kasnije.
 * 
 * @author Jure Šiljeg
 * @version 1.0.
 */
public class IndexQueryFilter {

	/** Varijabla u kojoj se čuva naš jmbag. */
	private String jmbag;

	/**
	 * Konstruktor koji poziva parser za uvjetni izraz kod indexqueryja i
	 * inicijalizira jmbag adekvatno.
	 * 
	 * @param query
	 *            uvjetni izraz koji trebamo obraditi.
	 */
	public IndexQueryFilter(String query) {
		super();
		this.jmbag = processIndexQuery(query);
	}

	/**
	 * Getter za jmbag.
	 * 
	 * @return taj jmbag.
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Metoda ekstrahira jmbag u slučaju da je zadan indexquery i vrši provjere
	 * valjanosti za isti i format inputa.
	 * 
	 * @param input
	 *            tekstualni zapis jedinog uvjetnog izraza.
	 * @return vraća pronađeni jmbag.
	 */
	private static String processIndexQuery(String input) {
		input = input.substring(10).trim(); // izdvojim dio bez indexquery
		String[] indexQuery = input.split("=");
		if (indexQuery.length != 2) {
			System.out.println("Greška u parsiranju. Unijeli ste krivi format indexquery komande!");
			System.exit(-1);
		}
		String jmbag = indexQuery[0].trim();
		if (!jmbag.toLowerCase().equals("jmbag")) {
			System.out.println("Morali ste za ime atributa isključivo upisati riječ jmbag");
			System.exit(-1);
		}
		String number = indexQuery[1].trim();
		String[] numberArray = number.split("\"");
		if (numberArray.length != 2) {
			System.out.println("Krivo ste unijeli literal pod znakovima navodnika!");
			System.exit(-1);
		}
		number = numberArray[1].trim(); // čisti broj
		if (number.length() != 10) {
			System.out.println("Broj mora imati 10 znamenaka točno!");
			System.exit(-1);
		}
		return number;
	}

}
