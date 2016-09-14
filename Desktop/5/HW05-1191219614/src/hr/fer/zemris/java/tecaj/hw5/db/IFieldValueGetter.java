package hr.fer.zemris.java.tecaj.hw5.db;

/**
 * Sučelje za dohvat atributa.
 * 
 * @author Jure Šiljeg
 * @version 1.0.
 */
public interface IFieldValueGetter {

	/**
	 * Metoda kojom dohvaćamo zadani atribut.
	 * 
	 * @param record
	 *            složeni podatak iz kojeg trebamo izvući specifični jedan.
	 * @return taj podatak u obliku Stringa.
	 */
	public String get(StudentRecord record);
}
