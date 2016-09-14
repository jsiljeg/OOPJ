package hr.fer.zemris.java.tecaj.hw5.db;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw5.collections.SimpleHashtable;

/**
 * Razred kojim modeliramo bazu studenata.
 * 
 * @author Jure Šiljeg
 * @version 1.0.
 */
public class StudentDatabase {

	/** Lista studenata. */
	private List<StudentRecord> studentRecords;
	/**
	 * Mapa studenata koja služi za brži dohvat. Ključ po kojem pretražujemo je
	 * jmbag.
	 */
	private SimpleHashtable<String, StudentRecord> index;

	/**
	 * Konstruktor koji iz liste redaka tablice iz tekstualne datoteke o
	 * studenatima pravi listu kojoj su elementi instance razreda koji
	 * predstavlja studenta sa svojim specifičnim podacima. Također se kreira
	 * hash tablica koja vremenski olakšava dohvat studenta. I tablica i lista
	 * se inicijaliziraju ovim pozivom.
	 * 
	 * @param listOfStudents
	 *            lista redaka tekstualne tablice.
	 */
	public StudentDatabase(List<String> listOfStudents) {
		super();
		createStudentRecords(listOfStudents);
		createIndex();
	}

	/**
	 * Metoda kreira hash tablicu iz liste studenata.
	 */
	private void createIndex() {
		index = new SimpleHashtable<String, StudentRecord>(studentRecords.size());
		for (int i = 0; i < studentRecords.size(); ++i) {
			index.put(studentRecords.get(i).getJmbag(), studentRecords.get(i));
		}
	}

	/**
	 * Metoda zadužena za stvaranje instance jednog studenta ekstrakcijom
	 * sirovih podataka iz tekstualne datoteke.
	 * 
	 * @param student
	 *            redak sirove i neobrađene tablice.
	 * @return kreiranog studenta.
	 */
	private StudentRecord parse(String student) {
		String[] field = student.trim().split("\\t+");
		return new StudentRecord(field[0].trim(), field[1].trim(), field[2].trim(), Integer.parseInt(field[3].trim()));
	}

	/**
	 * Pravi listu studenata iz liste redaka tekstualne datoteke.
	 * 
	 * @param listOfStudents
	 *            retci tekstualne datoteke.
	 */
	private void createStudentRecords(List<String> listOfStudents) {
		studentRecords = new ArrayList<>(listOfStudents.size());
		for (int i = 0; i < listOfStudents.size(); ++i) {
			studentRecords.add(parse(listOfStudents.get(i)));
		}
	}

	/**
	 * Metoda koja u O(1) pronalazi studenta iz hashirane tablise.
	 * 
	 * @param jmbag
	 *            vrijednost ključa po kojem tražimo.
	 * @return pronađenog studenta.
	 */
	public StudentRecord forJMBAG(String jmbag) {
		return index.get(jmbag);
	}

	/**
	 * Metoda koja traži filtriranu listu studenata (oni koji zadovoljavaju
	 * određeni kriterij).
	 * 
	 * @param filter
	 *            popis filtera.
	 * @return novogeneriranu listu u skladu s filterom.
	 */
	public List<StudentRecord> filter(IFilter filter) {
		List<StudentRecord> tempList = new ArrayList<>();
		for (int i = 0; i < studentRecords.size(); ++i) {
			if (filter.accepts(studentRecords.get(i))) {
				tempList.add(studentRecords.get(i));
			}
		}
		return tempList;
	}

	/**
	 * Metoda za standardizirani ispis dohvaćenih podataka o studentima.
	 * Izgledom podsjeća na bazu podataka.
	 * 
	 * @param records
	 *            kolekcija studenata koji se moraju naći u ispisu.
	 */
	public void specialOutput(List<StudentRecord> records) {
		int firstNameLen = 0, lastNameLen = 0;
		String fixPart = "+============+";

		for (int i = 0; i < records.size(); ++i) { // pronađi u listi najdulje
													// ime i prezime
			if (records.get(i).getFirstName().length() > firstNameLen) {
				firstNameLen = records.get(i).getFirstName().length();
			}
			if (records.get(i).getLastName().length() > lastNameLen) {
				lastNameLen = records.get(i).getLastName().length();
			}
		} // postavi gornji i donji obrub po specifikaciji
		for (int i = 0; i < lastNameLen + 2; ++i) {
			fixPart += "=";
		}
		fixPart += "+";
		for (int i = 0; i < firstNameLen + 2; ++i) {
			fixPart += "=";
		}
		fixPart += "+===+"; // ovdje sam gotov s izgledom najgornje i najdonje
							// linije ispisa

		System.out.println(fixPart);
		// unutarnji dio tablice
		for (int i = 0; i < records.size(); ++i) {
			String nonFixPart = "| ";
			nonFixPart += records.get(i).getJmbag() + " | " + records.get(i).getLastName();
			for (int j = 0; j < lastNameLen - records.get(i).getLastName().length(); ++j) {
				nonFixPart += " ";
			}
			nonFixPart += " | " + records.get(i).getFirstName();
			for (int j = 0; j < firstNameLen - records.get(i).getFirstName().length(); ++j) {
				nonFixPart += " ";
			}
			nonFixPart += " | " + records.get(i).getGrade() + " |";
			System.out.println(nonFixPart);
		}

		System.out.println(fixPart);

		System.out.println("Records selected: " + records.size());
	}

}
