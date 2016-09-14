package hr.fer.zemris.java.tecaj.hw5.db;

/**
 * Razred za filtriranje podataka o studentima u skladu s nekim kriterijima.
 * 
 * @author Jure Šiljeg
 * @version 1.0.
 */
public interface IFilter {
	/**
	 * Metoda koja odlučuje nalazi li se odgovarajući student u bazi po zadanim
	 * kriterijima.
	 * 
	 * @param record
	 *            student o čijoj sudbini se odlučuje.
	 * @return istinu ako se nalazi, inače laž.
	 */
	public boolean accepts(StudentRecord record);
}
