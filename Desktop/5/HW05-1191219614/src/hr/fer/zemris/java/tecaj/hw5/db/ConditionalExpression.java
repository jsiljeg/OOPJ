package hr.fer.zemris.java.tecaj.hw5.db;

/**
 * Razred pomoću kojeg ćemo modelirati uvjetne izraze.
 * 
 * @author Jure Šiljeg
 *
 */
public class ConditionalExpression {
	/**
	 * Varijabla pomoću koje ćemo identificirati o kojoj vrsti atributa se radi.
	 */
	private IFieldValueGetter field;
	/** Varijabla u koju spremamo literal. */
	private String literal;
	/**
	 * Varijabla pomaže za identificiranje o kojem operatoru uspoređivanja je
	 * riječ.
	 */
	private IComparisonOperator operation;

	/**
	 * Konstruktor pomoću kojeg inicijaliziramo članske varijable.
	 * 
	 * @param field
	 *            odgovara vrsti atributa
	 * @param literal
	 *            odgovara literalu s desne strane operatora uspoređivanja.
	 * @param operation
	 *            se odnosi na operator uspoređivanja.
	 */
	public ConditionalExpression(IFieldValueGetter field, String literal, IComparisonOperator operation) {
		super();
		this.field = field;
		this.literal = literal;
		this.operation = operation;
	}

	/**
	 * Getter za atribut.
	 * 
	 * @return taj atribut.
	 */
	public IFieldValueGetter getField() {
		return field;
	}

	/**
	 * Getter za literal.
	 * 
	 * @return taj literal.
	 */
	public String getLiteral() {
		return literal;
	}

	/**
	 * Getter za operator uspoređivanja.
	 * 
	 * @return taj operator.
	 */
	public IComparisonOperator getOperation() {
		return operation;
	}
}
