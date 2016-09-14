package hr.fer.zemris.java.tecaj.hw5.db;

/**
 * Razred koji modelira jednog studenta.
 * 
 * @author Jure Šiljeg
 * @version 1.0.
 */
public class StudentRecord {
	/** Ime studenta. */
	private String firstName;
	/** Prezime studenta. */
	private String lastName;
	/** Studentov jmbag. */
	private String jmbag;
	/** Studentova ocjena. */
	private int grade;

	/**
	 * Konstruktor za stvaranje studenta s pojedinim osobinama.
	 * 
	 * @param jmbag
	 *            odnosi se na studentov jmbag.
	 * @param lastName
	 *            predstavlja prezime studenta.
	 * @param firstName
	 *            predstavlja ime studenta.
	 * @param grade
	 *            studentova ocjena.
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, int grade) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.jmbag = jmbag;
		this.grade = grade;
	}

	/**
	 * Getter za ime studentovo.
	 * 
	 * @return to ime.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Setter za studentovo ime.
	 * 
	 * @param firstName
	 *            ime na koje postavljamo.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Getter za prezime studentovo.
	 * 
	 * @return to prezime.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Setter za studentovo prezime.
	 * 
	 * @param lastName
	 *            prezime na koje postavljamo.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Getter za jmbag studentov.
	 * 
	 * @return taj jmbag.
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Setter za studentov jmbag.
	 * 
	 * @param jmbag
	 *            jmbag na koji postavljamo.
	 */
	public void setJmbag(String jmbag) {
		this.jmbag = jmbag;
	}

	/**
	 * Getter za studentovu ocjenu.
	 * 
	 * @return tu ocjenu.
	 */
	public int getGrade() {
		return grade;
	}

	/**
	 * Setter za studentovu ocjenu.
	 * 
	 * @param grade
	 *            ta ocjena koju postavljamo.
	 */
	public void setGrade(int grade) {
		this.grade = grade;
	}

}
