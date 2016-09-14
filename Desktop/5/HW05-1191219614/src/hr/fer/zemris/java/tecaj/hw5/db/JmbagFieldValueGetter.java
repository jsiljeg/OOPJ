package hr.fer.zemris.java.tecaj.hw5.db;

/**
 * Razred koi služi za dohvat atributa jmbag.
 * 
 * @author Jure Šiljeg
 * @version 1.0.
 */
public class JmbagFieldValueGetter implements IFieldValueGetter {

	/**
	 * {@inheritDoc} Dohvaća se jmbag studenta.
	 */
	@Override
	public String get(StudentRecord record) {
		return record.getJmbag();
	}

}
