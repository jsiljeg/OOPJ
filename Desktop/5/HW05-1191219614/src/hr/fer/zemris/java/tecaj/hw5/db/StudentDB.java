package hr.fer.zemris.java.tecaj.hw5.db;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Razred za testiranje funkcionalnosti pojednostavnjene baze podataka s dvije
 * komande.
 * 
 * @author Jure Šiljeg
 * @version 1.0.
 */
public class StudentDB {

	/**
	 * Osnovna metoda za pokretanje emulatora baze.
	 * 
	 * @param args
	 *            argumenti komandne linije.
	 * @throws FileNotFoundException
	 *             iznimka u slučaju nemogućnosti otvaranja filea.
	 * @throws IOException
	 *             u slučaju nemogućnosti očitavanja retka datoteke.
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {

		// učitavanje liste redaka iz tekst. datoteke
		String filepath = "database.txt";
		List<String> database = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
			String line;
			while ((line = br.readLine()) != null) {
				database.add(line);
			}
		}
		// inicijaliziramo bazu
		StudentDatabase stdDb = new StudentDatabase(database);

		BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(System.in)));
		while (true) {
			System.out.print("> ");
			String input = reader.readLine().trim();
			if (input.toLowerCase().equals("exit")) {
				System.out.println("Goodbye!");
				return;
			} else { // unos komandi

				List<StudentRecord> studentOutputRecords = new ArrayList<StudentRecord>();
				StudentRecord student;

				if (input.contains("indexquery")) {
					// instanca pomoćne klase radi urednosti i lakšeg pisanja
					// JUnit testova kasnije
					IndexQueryFilter indexQ = new IndexQueryFilter(input);
					// dohvaćanje studenta u O(1)
					student = stdDb.forJMBAG(indexQ.getJmbag());

					// ako student nije pronađen
					if (student == null) {
						System.out.println("Nepostojeći student!");
						System.exit(-1);
					}

					// ako jest, ispiši ga
					studentOutputRecords.add(student);
					stdDb.specialOutput(studentOutputRecords);
				} else if (input.contains("query")) {
					// od 5.indeksa da uzme sve van query
					stdDb.specialOutput(stdDb.filter(new QueryFilter(input.substring(5).trim())));
				} else {
					System.out.println("Unijeli ste neispravnu komandu! ");
					return;
				}
			}
		}

	}

}
