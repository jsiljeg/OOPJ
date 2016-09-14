package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.functions.DecFmtFunImpl;
import hr.fer.zemris.java.custom.scripting.functions.DupFunImpl;
import hr.fer.zemris.java.custom.scripting.functions.IFunction;
import hr.fer.zemris.java.custom.scripting.functions.MimeTypeFunImpl;
import hr.fer.zemris.java.custom.scripting.functions.PParamDel;
import hr.fer.zemris.java.custom.scripting.functions.PParamGetImpl;
import hr.fer.zemris.java.custom.scripting.functions.PParamSet;
import hr.fer.zemris.java.custom.scripting.functions.ParamGetImpl;
import hr.fer.zemris.java.custom.scripting.functions.SinFunImpl;
import hr.fer.zemris.java.custom.scripting.functions.SwapFunImpl;
import hr.fer.zemris.java.custom.scripting.functions.TParamDel;
import hr.fer.zemris.java.custom.scripting.functions.TParamGet;
import hr.fer.zemris.java.custom.scripting.functions.TParamSet;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * Razred čija je zadaća da izvrši doslovno isparsirani dokument.
 * 
 * @author Jure Šiljeg
 *
 */
public class SmartScriptEngine {
	/** Polje dopustivih operacija. */
	private static final List<String> OPERATIONS = Arrays.asList("+", "-", "*", "/");
	/**
	 * Mapa koja čuva zadana imena kao ključ i strategiju za izvršavanje
	 * funkcije kao vrijednosni argument.
	 */
	private static final Map<String, IFunction> MAPPING_FUNCTIONS;
	static {
		Map<String, IFunction> aMap = new HashMap<>();
		aMap.put("sin", new SinFunImpl());
		aMap.put("decfmt", new DecFmtFunImpl());
		aMap.put("dup", new DupFunImpl());
		aMap.put("swap", new SwapFunImpl());

		aMap.put("setMimeType", new MimeTypeFunImpl());
		aMap.put("paramGet", new ParamGetImpl());
		aMap.put("pparamGet", new PParamGetImpl());
		aMap.put("pparamSet", new PParamSet());
		aMap.put("pparamDel", new PParamDel());
		aMap.put("tparamGet", new TParamGet());
		aMap.put("tparamSet", new TParamSet());
		aMap.put("tparamDel", new TParamDel());
		MAPPING_FUNCTIONS = Collections.unmodifiableMap(aMap);
	}

	/** Glavni čvor dokumenta. */
	private DocumentNode documentNode;
	/** Kontekst zahtjeva. */
	private RequestContext requestContext;
	/** Multistog. */
	private ObjectMultistack multistack = new ObjectMultistack();
	/** Posjetitelj. */
	private INodeVisitor visitor = new INodeVisitor() {

		@Override
		public void visitDocumentNode(DocumentNode node) {
			int childrenCount = node.numberOfChildren();
			for (int i = 0; i < childrenCount; ++i) {
				Node n = node.getChild(i);
				n.accept(this);
			}
		}

		@Override
		public void visitTextNode(TextNode node) {
			try {
				requestContext.write(node.toString());
			} catch (IOException e) {
				System.out.println("Nemoguće pisati metodom write!");
				System.exit(-1);
			}
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			String variable = node.getVariable().asText();
			ValueWrapper startExpr = new ValueWrapper(node.getStartExpression().asText());
			ValueWrapper endExpr = new ValueWrapper(node.getEndExpression().asText());
			ValueWrapper stepExpr = new ValueWrapper(node.getStepExpression().asText());
			multistack.push(variable, startExpr);

			while (multistack.peek(variable).numCompare(endExpr.getValue()) <= 0) {
				int childrenCount = node.numberOfChildren();
				for (int i = 0; i < childrenCount; i++) {
					Node child = node.getChild(i);

					child.accept(this);
				}
				ValueWrapper currValue = multistack.pop(variable);
				currValue.increment(stepExpr.getValue());
				multistack.push(variable, currValue);
			}
			multistack.pop(variable);
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			ObjectStack tmpStack = new ObjectStack();
			Element[] elems = node.getElements();
			StackRequestContext src = new StackRequestContext(tmpStack, requestContext);

			for (int i = 0; i < elems.length; ++i) {
				// ako je operator u pitanju
				if (elems[i] instanceof ElementOperator) {
					ValueWrapper upper = (ValueWrapper) tmpStack.pop();
					Object lower = ((ValueWrapper) tmpStack.pop()).getValue();

					if (elems[i].asText().equals("+")) {
						tmpStack.push(upper.increment(lower));
					} else if (elems[i].asText().equals("-")) {
						tmpStack.push(upper.decrement(lower));
					} else if (elems[i].asText().equals("*")) {
						tmpStack.push(upper.multiply(lower));
					} else if (elems[i].asText().equals("/")) {
						tmpStack.push(upper.divide(lower));
					} else {
						System.out.println("Imate nepodržanu operaciju: " + elems[i].asText() + ", a podržane su samo: "
								+ OPERATIONS.toString());
					}
				}
				// ako je varijabla u pitanju
				else if (elems[i] instanceof ElementVariable) {
					tmpStack.push(new ValueWrapper(multistack.peek(elems[i].asText()).getValue()));
				}
				// funkcija
				else if (elems[i] instanceof ElementFunction) {
					if (MAPPING_FUNCTIONS.get(elems[i].asText()) != null) {
						src = MAPPING_FUNCTIONS.get(elems[i].asText()).execute(tmpStack, requestContext);
						tmpStack = src.getTmpStack();
						requestContext = src.getRequestContext();
					} else {
						System.out.println("Nepodržano ime operacije imate negdje! Unijeli ste: " + elems[i].asText()
								+ ", a jedino su podržane sljedeće naredbe: " + MAPPING_FUNCTIONS.keySet().toString());
						System.exit(-1);
					}
				}
				// neka konstanta
				else if (elems[i] instanceof ElementConstantDouble) {
					tmpStack.push(new ValueWrapper(Double.parseDouble(elems[i].asText())));
				} else if (elems[i] instanceof ElementConstantInteger) {
					tmpStack.push(new ValueWrapper(Integer.parseInt(elems[i].asText())));
				} else if (elems[i] instanceof ElementString) {
					tmpStack.push(new ValueWrapper(elems[i].asText()));
				}
			}

			// preokrenemo stog
			tmpStack = revert(tmpStack);
			// ispisujemo dio po dio
			while (!tmpStack.isEmpty()) {
				try {
					Object obj = ((ValueWrapper) tmpStack.pop()).getValue();
					requestContext.write(String.valueOf(obj));
				} catch (IOException e) {
					System.out.println("Pogreška prilikom upisa i korištenja metode write!");
					System.exit(-1);
				}
			}
		}

		private ObjectStack revert(ObjectStack tmpStack) {
			ObjectStack tmp = new ObjectStack();
			while (!tmpStack.isEmpty()) {
				tmp.push(tmpStack.pop());
			}
			return tmp;
		}
	};

	/**
	 * Konstruktor za izvršitelja skripte.
	 * 
	 * @param documentNode
	 *            glavni čvor dokumenta kojeg želimo isparsirati.
	 * @param requestContext
	 *            kontekst zahtjeva.
	 */
	public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
		this.documentNode = documentNode;
		this.requestContext = requestContext;
	}

	/**
	 * Metoda za izvršavanje posjete dokumentu.
	 */
	public void execute() {
		documentNode.accept(visitor);
	}

	/**
	 * Glavna metoda kojom demonstriramo rad pametnog parsera skripte.
	 * 
	 * @param args
	 *            dobiva stazu do datoteke koju ćemo parsirati preko komandne
	 *            linije.
	 */
	public static void main(String[] args) {

		String documentBody = readFromDisk(args[0]);
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		// create engine and execute it
		new SmartScriptEngine(new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters, persistentParameters, cookies)).execute();
	}

	/**
	 * Ućitava zapis iz željene datoteke u string.
	 * 
	 * @param filePath
	 *            staza do željene datoteke za parsiranje.
	 * @return string koji sadrži sve znakove iz skripte u očuvanom poretku.
	 */
	private static String readFromDisk(String filePath) {
		String docBody = null;

		try {
			docBody = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
		} catch (IOException ex) {
			System.err.println("Nemoguće otvoriti datoteku!");
			System.exit(-1);
		}
		return docBody;
	}

}
