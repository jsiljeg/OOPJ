package hr.fer.zemris.java.tecaj.hw5.db;

import java.util.ArrayList;
import java.util.List;

/**
 * Razred zadužen za filtriranje upita (parsiranje). Na osnovu upita se
 * postavljaju kriteriji za prihvaćanje pojedinog studenta ili ne, a sve u
 * skladu sa specifikacijom iz upita.
 * 
 * @author Jure Šiljeg
 * @version 1.0.
 */
public class QueryFilter implements IFilter {
	/** Varijabla u kojoj čuvamo tekstualni upit u originalu. */
	private String query;
	/** Niz uvjeta koji moraju biti zadovoljeni za prihvaćanje studenta. */
	private List<ConditionalExpression> condExpress;

	/**
	 * Konstruktor koji služi za čuvanje tekstualnog upita i koji pokreće
	 * parsiranje.
	 * 
	 * @param query
	 *            tekstualni unos upita iz konzole.
	 */
	public QueryFilter(String query) {
		super();
		this.query = query;
		parseQuery();
	}

	/**
	 * Parser koji generira uvjet iz dobivenog komada teksta.
	 * 
	 * @param exp
	 *            tekst koji treba analizirati.
	 * @return generirani uvjet.
	 */
	private ConditionalExpression processExpr(String exp) {
		IFieldValueGetter atr = null;
		IComparisonOperator op = null;
		String lit = "";

		// atribut s kontrolom da je na početku
		if (exp.contains("jmbag") && exp.startsWith("jmbag")) {
			atr = new JmbagFieldValueGetter();
		} else if (exp.contains("firstName") && exp.startsWith("firstName")) {
			atr = new FirstNameFieldValueGetter();
		} else if (exp.contains("lastName") && exp.startsWith("lastName")) {
			atr = new LastNameFieldValueGetter();
		} else {
			System.out.println("Nepodržano ime atributa ste unijeli!");
			System.exit(-1);
		}
		// operator
		if (exp.contains("<")) {
			op = new LessThanComparison();
		} else if (exp.contains("<=")) {
			op = new LessEqualsComparison();
		} else if (exp.contains(">")) {
			op = new GreaterComparison();
		} else if (exp.contains(">=")) {
			op = new GreaterEqualsComparison();
		} else if (exp.contains("=")) {
			op = new EqualsComparison();
		} else if (exp.contains("!=")) {
			op = new NonEqualComparison();
		} else if (exp.contains("LIKE")) {
			op = new LikeComparison();
		} else {
			System.out.println("Nepodržan operator ste unijeli!");
			System.exit(-1);
		}
		// literal s kontrolom kraja... sve je trim-ano!
		if (exp.contains("\"") && exp.endsWith("\"")) {
			String[] literalpart = exp.split("\"");
			if (literalpart.length != 2) {
				System.out.println("Kriv način unosa literala!");
				System.exit(-1);
			}
			lit = literalpart[1].trim();
		} else {
			System.out.println("Literali moraju biti s navodnicima!");
			System.exit(-1);
		}

		return new ConditionalExpression(atr, lit, op);
	}

	/**
	 * Metoda generira uvjete prihvaćanja studenta (filtere) iz svakog uvjetnog
	 * izraza upita.
	 */
	private void parseQuery() {

		String[] expressions = query.split("[aA][nN][dD]");
		condExpress = new ArrayList<>(expressions.length);
		for (String exp : expressions) {
			exp = exp.trim();

			condExpress.add(processExpr(exp));
		}
	}

	/**
	 * Metoda odlučuje prihvaćamo li nekog studenta u skladu s postavljenim
	 * uvjetima (filterima).
	 * 
	 * @param record
	 *            student o kojem odlučujemo.
	 * @return istinu ako prolazi sve filtere, inače laž.
	 */
	@Override
	public boolean accepts(StudentRecord record) {
		for (int i = 0; i < condExpress.size(); ++i) {
			ConditionalExpression expr = condExpress.get(i);
			try {
				if (!expr.getOperation().satisfied(expr.getField().get(record), expr.getLiteral())) {
					return false;
				}
			} catch (Exception e) {
				System.out.println("Neregularan broj * u literalu!");
				System.exit(-1);
			}
		}
		return true;
	}

}
