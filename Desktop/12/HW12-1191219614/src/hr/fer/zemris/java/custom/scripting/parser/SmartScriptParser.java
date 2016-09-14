package hr.fer.zemris.java.custom.scripting.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.lexer.Lexer;
import hr.fer.zemris.java.custom.scripting.lexer.LexerState;
import hr.fer.zemris.java.custom.scripting.lexer.Token;
import hr.fer.zemris.java.custom.scripting.lexer.TokenType;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;

/**
 * Ovo treba parsirati i izgraditi stablo. Parser je zadužen za stvaranje
 * nodeova i expressiona temeljem informacija koje dobiva od lexera.
 * 
 * @author Jure Šiljeg
 * @version 1.0.
 */
public class SmartScriptParser {

	/** Lexer koji koristimo za tokeniziranje. */
	private final Lexer lexer;
	/** Korijenski čvor dokumenta. */
	private final DocumentNode docNode;
	/**
	 * Stog koji koristimo za kreiranje stablaste strukture dokumenta i čvorova.
	 */
	private final ObjectStack stack;
	/** Lista podržanih operatora. */
	private static final List<Character> SYMBOLS = Arrays.asList('+', '-', '*', '/', '^');

	/**
	 * Konstruktor za pametni parser.
	 * 
	 * @param docBody
	 *            dokument koi treba parsirati.
	 */
	public SmartScriptParser(String docBody) {
		if (docBody == null) {
			throw new IllegalArgumentException("Dokument ne smije biti null!");
		}
		lexer = new Lexer(docBody.toCharArray());
		stack = new ObjectStack();
		docNode = new DocumentNode();
		stack.push(docNode);
		workOnParser();
	}

	/** Metoda s kojom počinje naše parsiranje. */
	private void workOnParser() {
		try {
			lexer.setState(LexerState.TEXT);

			while (lexer.nextToken().getType() != TokenType.EOF) {

				Token token = lexer.getToken();
				// ako smo u text modu
				if (token.getType().equals(TokenType.TEXT)) {
					((Node) stack.peek()).addChildNode(new TextNode((String) token.getValue()));
				}
				// ako ulazimo u tag mod nakon pročitanog znaka
				else if (token.getType().equals(TokenType.SYMBOL)) {
					if ((char) token.getValue() == '{') {
						parseTag();
					}
				}
			}
		} catch (SmartScriptParserException e) {
			throw new SmartScriptParserException(e.getMessage());
		}
	}

	/**
	 * Metoda za parsiranje taga.
	 * 
	 * @throws SmartScriptParserException
	 *             if an error occurs while parsing.
	 */
	private void parseTag() {
		lexer.setState(LexerState.TAG);

		Token token = lexer.nextToken();
		if (!(String.valueOf(token.getValue()).equals("$"))) {
			throw new SmartScriptParserException(
					"Otvorili ste tag znakom: " + String.valueOf(token.getValue()) + " umjesto znakom $");
		}
		// tag
		token = lexer.nextToken();
		// for tag
		if (token.getType() == TokenType.VARIABLE && String.valueOf(token.getValue()).equalsIgnoreCase("FOR")) {
			parseForLoopTag();
		}
		// end tag
		else if (token.getType() == TokenType.VARIABLE && String.valueOf(token.getValue()).equalsIgnoreCase("END")) {
			parseEndTag();
		}
		// echo tag
		else if (token.getType() == TokenType.VARIABLE || String.valueOf(token.getValue()).equals("=")) {
			parseEchoTag(token);
		} else {
			throw new SmartScriptParserException("Nedefiniran tip tag-a se dogodio!");
		}
		lexer.setState(LexerState.TEXT);
	}

	/**
	 * Metoda zadužena za parsiranje {@link ForLoopNode}. Baca se
	 * SmartScriptParserException u slučaju pogreške pri parsiranju.
	 */
	private void parseForLoopTag() {
		ElementVariable variable;
		try {
			variable = (ElementVariable) parseForLoopElement(lexer.nextToken());
		} catch (ClassCastException e) {
			throw new SmartScriptParserException("Nije valjana forma for tag-a!");

		}
		// starting Expression
		Element startingExpression = parseForLoopElement(lexer.nextToken());

		// ending Expression
		Element endingExpression = parseForLoopElement(lexer.nextToken());

		// step Expression
		Token token = lexer.nextToken();
		ForLoopNode node;
		// ne postoji step Eexpression
		if (String.valueOf(token.getValue()).equals("$")) {
			node = new ForLoopNode(variable, startingExpression, endingExpression);
		}
		// postoji
		else {
			node = new ForLoopNode(variable, startingExpression, endingExpression, parseForLoopElement(token));

			token = lexer.nextToken();
		}
		((Node) stack.peek()).addChildNode(node);
		stack.push(node);
		// prvi dio kraja tag-a
		if (!String.valueOf(token.getValue()).equals("$")) {
			throw new SmartScriptParserException("Nije valjana forma for tag-a!");
		}
		token = lexer.nextToken();
		// drugi dio kraja tag-a
		if (String.valueOf(token.getValue()).equals("}")) {
			return;
		}
		// nije bio dobar završetak
		throw new SmartScriptParserException("Nije valjana forma for tag-a!");
	}

	/**
	 * Metoda za parsiranje elemenata unutar for tag-a.
	 * 
	 * @param token
	 *            žeton kojeg želimo parsirati i izvući element iz taga za for
	 *            petlju ({@link ForLoopNode}).
	 * @return parsirani element. Baca se SmartScriptParserException u slučaju
	 *         pogreške prilikom parsiranja, a odnosi se na nepodržane tipove
	 *         i/ili njihov format unutar taga. Dozvoljeni su
	 *         {@link ElementString}, {@link ElementVariable} i brojevi (
	 *         {@link ElementConstantInteger} i {@link ElementConstantDouble}).
	 */
	private Element parseForLoopElement(Token token) {
		Element returningElement;

		switch (token.getType()) {

		case STRING:
			returningElement = new ElementString((String) token.getValue());
			break;

		case INTEGER:
			returningElement = new ElementConstantInteger((Integer) token.getValue());
			break;

		case DOUBLE:
			returningElement = new ElementConstantDouble((Double) token.getValue());
			break;

		case VARIABLE:
			returningElement = new ElementVariable((String) token.getValue());
			break;

		default:
			throw new SmartScriptParserException("Nije valjana forma for tag-a!");
		}

		return returningElement;
	}

	/**
	 * Metoda koja parsira end tag. Baca SmartScriptParserException u slučaju
	 * loše zatvorenog tag-a ili previše end tagova.
	 */
	private void parseEndTag() {
		Token firstToken = lexer.nextToken();
		Token secondToken = lexer.nextToken();
		if (!(firstToken.getValue().toString().equals("$") && secondToken.getValue().toString().equals("}"))) {
			throw new SmartScriptParserException("Nije dobro zatvoren end tag");
		}
		stack.pop();
		if (stack.size() == 0) {
			throw new SmartScriptParserException("Stog je prazan!");
		}
	}

	/**
	 * Metoda zadužena za parsiranje {@link EchoNode}. Promatra se prvi žeton i
	 * određuje se je li on bio {@link ElementOperator} ili
	 * {@link ElementVariable} budući da u echo tagu ime taga može biti znak =
	 * ili ime varijable. Potom se ide u parsiranje ostalih dopustivih
	 * kontrukata unutar echo taga. Baca se SmartScriptParserException u slučaju
	 * krivo zatvorenog taga ili neke druge pogreške pri parsiranju echo taga.
	 * 
	 * @param token
	 *            žeton koji se tokenizira.
	 * 
	 */
	private void parseEchoTag(Token token) {

		List<Element> elements = new ArrayList<>();
		// ako je znak = bio prvi, onda je on simbol
		if (token.getType().equals(TokenType.SYMBOL)) {
			// elements.add(new ElementOperator(token.getValue().toString()));
		}
		// inače je bila varijabla
		else {
			elements.add(new ElementVariable(String.valueOf(token.getValue())));
		}

		token = lexer.nextToken();
		// ubaci sve iz taga u listu
		while (!String.valueOf(token.getValue()).equals("$")) {
			elements.add(parseEchoElement(token));
			token = lexer.nextToken();
		}
		token = lexer.nextToken();
		// provjeri je li legalno sve bilo do samog kraja
		if (token.getType() != TokenType.SYMBOL || (char) token.getValue() != '}') {
			throw new SmartScriptParserException("Krivo zatvaranje echo tag-a!");
		}
		// pretvori listu u niz jer EchoNode prima niz Element
		Element[] elementArray = new Element[elements.size()];
		int i = 0;
		for (Element e : elements) {
			elementArray[i++] = e;
		}
		((Node) stack.peek()).addChildNode(new EchoNode(elementArray));
	}

	/**
	 * Metoda zadužena za dohvaćanje elementa iz echo tag-a.
	 * 
	 * @param token
	 *            žeton koji se parsira u tag elemente. Baca
	 *            SmartScriptParserException u slučaju nepodržanog simbola ili
	 *            lošeg zatvaranja taga, pa situacije kad smo dosegnuli eof
	 *            žeton gdje nismo smjeli.
	 * 
	 * @return traženi element.
	 */
	private Element parseEchoElement(Token token) {

		switch (token.getType()) {
		case VARIABLE:
			return new ElementVariable(String.valueOf(token.getValue()));

		case STRING:
			return new ElementString((String) token.getValue());

		case SYMBOL:
			if (checkIfItIsOperator((char) token.getValue())) {
				return new ElementOperator(token.getValue().toString());
			} else {
				throw new SmartScriptParserException(
						"Simbol kojeg ste unijeli nije podržan u skupu simbola. Unijeli ste: "
								+ String.valueOf(token.getValue()) + "; podržani su samo: " + SYMBOLS.toString());
			}

		case FUNCTION:
			return new ElementFunction(String.valueOf(token.getValue()));

		case DOUBLE:
			return new ElementConstantDouble(Double.parseDouble(String.valueOf(token.getValue())));

		case INTEGER:
			return new ElementConstantInteger(Integer.parseInt(String.valueOf(token.getValue())));

		default:
			throw new SmartScriptParserException("EOF smo dosegli, a nismo trebali!");
		}
	}

	/**
	 * Metoda za provjeru je li pojedini char operator.
	 * 
	 * @param symbol
	 *            char koji provjeravamo.
	 * @return <code> true </code> ako je operator, inače <code> false </code>.
	 */
	private boolean checkIfItIsOperator(char symbol) {
		return SYMBOLS.contains(symbol);
	}

	/** @return Document čvor. */
	public DocumentNode getDocumentNode() {
		return docNode;
	}

}
