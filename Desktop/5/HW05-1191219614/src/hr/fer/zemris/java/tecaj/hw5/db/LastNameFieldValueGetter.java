package hr.fer.zemris.java.tecaj.hw5.db;

/**
 * Razred koi služi za dohvat atributa prezime.
 * 
 * @author Jure Šiljeg
 * @version 1.0.
 */
public class LastNameFieldValueGetter implements IFieldValueGetter {

	/**
	 * {@inheritDoc} Dohvaća se prezime studenta.
	 */
	@Override
	public String get(StudentRecord record) {
		return record.getLastName();
	}

}
