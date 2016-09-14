package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Obavlja se razdvajanje u leksičke cjeline čitavog teksta. Iste su definirane
 * obzirom na lexer-mod (text ili tag iz {@link LexerState}) i obzirom na ono
 * što se nalazi unutar taga i propisano je s {@link TokenType}.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class Lexer {
	/**
	 * Predstavlja ulazni tekst.
	 */
	private final char[] data;
	/**
	 * Indeks prvog neobrađenog znaka.
	 */
	private int currentIndex = 0;
	/**
	 * Odnosi se na trenutnu cjelinu (žeton) koji pregledavamo.
	 */
	private Token token;
	/**
	 * Stanje u kojem se nalazimo za pregled (opcije).
	 */
	private LexerState state = LexerState.TEXT;

	/**
	 * Konstruktor prima ulazni tekst koji se tokenizira.
	 * 
	 * @param data
	 *            tekstualni unos koji ćemo razlagati. Ne smije biti null.
	 */
	public Lexer(char[] data) {
		if (data == null) {
			throw new LexerException("Podaci ne mogu biti null!");
		}
		this.data = data;
	}

	/**
	 * Služi za postavljanje stanja (opcije) kojom pregledavamo dio ulaza.
	 * 
	 * @param state
	 *            oznaka za stanje koje želimo postaviti. Ne smije stanje biti
	 *            null.
	 */
	public void setState(LexerState state) {
		if (state == null) {
			throw new LexerException("Stanje ne može biti null!");
		}
		this.state = state;
	}

	/**
	 * Detektira sljedeću cjelinu u odnosu na prethodno pozvanu i vraćenu (ako
	 * takva postoji). Može se pozivati više puta. Ne pokreće generiranje
	 * sljedećeg tokena.
	 * 
	 * @return zadnje generirani token. Baca iznimku LexerException ako token ne
	 *         postoji!
	 */
	public Token getToken() {
		if (token == null) {
			throw new LexerException("Nije pronađen žeton (token)!");
		}
		return token;
	}

	/**
	 * Generira sljedeću cjelinu (token).
	 * 
	 * @return vraća pronađenu cjelinu (token).
	 */
	public Token nextToken() {

		if (token != null && token.getType() == TokenType.EOF) {
			throw new LexerException("No more tokens to find, EOF was already reached.");
		}
		// TEXT stanje
		if (state == LexerState.TEXT) {
			token = evaluateText();
			return token;

			// TAG stanje
		} else if (state == LexerState.TAG) {
			try {
				ignoreWhitespaces();
			} catch (IndexOutOfBoundsException e) {
				token = new Token(TokenType.EOF, null);
			}

			token = evaluateTag();

		}
		return token;
	}

	/**
	 * Vraća se sljedeći žeton u skladu s pravilima unutar texta.
	 * 
	 * @return taj sljedeći žeton. Baca LexerException u slučaju pogreške
	 *         prilikom leksičke analize, a pojedine greške su riješene u
	 *         precizno u skladu s pravilima za pogrešnu escape sekvencu ili
	 *         dosezanje kraja dokumenta bez da smo odradili parsiranje u skladu
	 *         s propozicijama iz zadatka.
	 */
	private Token evaluateText() {
		Token returningToken;
		if (currentIndex >= data.length) {
			returningToken = new Token(TokenType.EOF, null);
			return returningToken;
		}
		if (data[currentIndex] == '{') {
			returningToken = new Token(TokenType.SYMBOL, '{');
			currentIndex++;
		} else {
			String text = "";
			while (currentIndex < data.length && data[currentIndex] != '{') {
				// pravila za escape sekvence unutar texta
				if (data[currentIndex] == '\\') {
					try {
						currentIndex++;
						if (data[currentIndex] != '\\' && data[currentIndex] != '{') {
							throw new LexerException(
									"Unijeli ste nepodržanu escape sekvencu: \\" + data[currentIndex + 1]);
						}
					} catch (IndexOutOfBoundsException e) {
						throw new LexerException("Došli ste do kraja dokumenta i imate nepravilnu escape sekvencu!");
					}
				}
				text += (data[currentIndex]);
				currentIndex++;
			}
			returningToken = new Token(TokenType.TEXT, text.toString());
		}
		return returningToken;
	}

	/**
	 * Vraća žeton u skladu s tag-pravilima.
	 * 
	 * @return idući žeton. Baca LexerException u slučaju pogreške prilikom
	 *         leksičke analize, a pojedine greške su riješene u pod-metodama
	 *         precizno.
	 * 
	 */
	private Token evaluateTag() {
		try {
			ignoreWhitespaces();
		} catch (IndexOutOfBoundsException e) {
			return new Token(TokenType.EOF, null);
		}

		// Varijabla
		if (Character.isLetter(data[currentIndex])) {
			return lexVariable();

			// Funkcija
		} else if (data[currentIndex] == '@') {
			currentIndex++;
			if (currentIndex >= data.length) {
				throw new LexerException("Dosegnuli smo kraj datoteke, a nismo ispravno deklarirali ime funkcije!");
			}
			try {
				return new Token(TokenType.FUNCTION, lexVariable().getValue());
			} catch (LexerException e) {
				throw e;
			}

			// Broj
		} else if (Character.isDigit(data[currentIndex]) || data[currentIndex] == '-') {
			return lexNumber();

			// String
		} else if (data[currentIndex] == '"') {
			return lexString();

			// Simbol
		} else {
			return new Token(TokenType.SYMBOL, data[currentIndex++]);
		}
	}

	/**
	 * Detektira i parsira varijablu počevši od trenutnog indeksa pa nadalje.
	 * 
	 * @return tip varijable. Baca iznimku u slučaju pogreške prilikom leksičke
	 *         analize imena varijable i nepoštivanja pravila da ime treba
	 *         započeti slovom.
	 */
	private Token lexVariable() {
		// istinski potrebno samo u slučaju funkcije
		if (!Character.isLetter(data[currentIndex])) {
			throw new LexerException("Varijabla mora početi slovom, a ne s: " + data[currentIndex]);
		}
		String variable = "";
		variable += data[currentIndex];
		currentIndex++;

		while (currentIndex < data.length && (Character.isAlphabetic(data[currentIndex])
				|| Character.isDigit(data[currentIndex]) || data[currentIndex] == '_')) {

			variable += data[currentIndex];
			currentIndex++;
		}

		return new Token(TokenType.VARIABLE, variable);
	}

	/**
	 * Parser za broj.
	 * 
	 * @return Integer ili Double. Baca LexerException u slučaju pogrešnog
	 *         formata broja (pogreška pri parsiranju).
	 */
	private Token lexNumber() {

		boolean isNegative = (currentIndex > 0 && data[currentIndex] == '-') ? true : false;
		if (isNegative) {
			++currentIndex;
		}
		boolean isDouble = false;
		String number = isNegative ? "-" : "";

		while (currentIndex < data.length && (Character.isDigit(data[currentIndex]) || data[currentIndex] == '.')) {
			if (data[currentIndex] == '.') {
				isDouble = true;
			}
			number += data[currentIndex];
			currentIndex++;
		}

		Token returningToken;
		try {
			if (isDouble) {
				returningToken = new Token(TokenType.DOUBLE, Double.parseDouble(number));

			} else {
				returningToken = new Token(TokenType.INTEGER, Integer.parseInt(number));

			}
		} catch (NumberFormatException e) {
			throw new LexerException("Dogodila se pogreška u parsiranju \"" + number + "\" u broj!");
		}

		return returningToken;
	}

	/**
	 * Parsira string žeton počevši od neke pozicije..
	 * 
	 * @return tip string. Baca iznimku u slučaju dolaska do kraja datoteke, a
	 *         da nismo dobro zatvorili navodne znakove ili u slučaju nepodržane
	 *         escape sekvence.
	 */
	private Token lexString() {

		String str = "";
		currentIndex++;

		while (data[currentIndex] != '"') {
			char nextChar;

			if (data[currentIndex] == '\\') {
				currentIndex++;
				if (currentIndex >= data.length) {
					throw new LexerException("Dosegnuli smo kraj datoteke uz neispravno zatvorene escape sekvence!");
				}
				switch (data[currentIndex]) {

				case '\\':
					nextChar = data[currentIndex];
					break;
				case '"':
					nextChar = data[currentIndex];
					break;

				case 'n':
					nextChar = '\n';
					break;

				case 'r':
					nextChar = '\r';
					break;

				case 't':
					nextChar = '\t';
					break;

				default:
					throw new LexerException("Unijeli ste nepodržanu escape sekvencu: \\" + data[currentIndex]);
				}
			} else {
				nextChar = data[currentIndex];
			}
			str += nextChar;
			currentIndex++;
		}
		currentIndex++;

		return new Token(TokenType.STRING, str);
	}

	/**
	 * Određuje je li znak prazan prostor u skladu s Javom i preskače dio do
	 * prvog nepraznog znaka. Baca se iznimka ako smo stigli do kraja datoteke
	 * prije nego do nekog nepraznog znaka.
	 */
	private void ignoreWhitespaces() {
		while (currentIndex < data.length && Character.isWhitespace(data[currentIndex])) {
			currentIndex++;
		}
		if (currentIndex >= data.length) {
			throw new IndexOutOfBoundsException("Došli smo do kraa datoteke - EOF");
		}
	}
}
