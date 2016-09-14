package hr.fer.zemris.java.tecaj.hw5.db;

/**
 * Razred koi služi za dohvat atributa ime.
 * 
 * @author Jure Šiljeg
 * @version 1.0.
 */
public class FirstNameFieldValueGetter implements IFieldValueGetter {

	/**
	 * {@inheritDoc} Dohvaća se ime studenta.
	 */
	@Override
	public String get(StudentRecord record) {
		return record.getFirstName();
	}

}
