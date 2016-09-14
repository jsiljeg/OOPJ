package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * Razred koji služi za demonstraciju oblikovnog obrasca posjetitelj nad
 * dokumentom koji se parsira i na standardni izlaz ispisuje približnu formu tog
 * dokumenta.
 * 
 * @author Jure Šiljeg
 *
 */
public class TreeWriter {
	/**
	 * Glavna metoda pomoću koje pokrećemo SmartScriptParser i generiramo
	 * traženi stablasti oblik i ispisujemo ga.
	 * 
	 * @param args
	 *            staza do datotekekoju parsiramo.
	 */
	public static void main(String[] args) {
		String docBody = null;

		try {
			docBody = new String(Files.readAllBytes(Paths.get(args[0])), StandardCharsets.UTF_8);
		} catch (IOException ex) {
			System.err.println("Nemoguće otvoriti datoteku!");
			System.exit(-1);
		}

		SmartScriptParser parser = null;
		try {
			parser = new SmartScriptParser(docBody);

		} catch (SmartScriptParserException ex) {
			System.out.println("Nije moguće parsirati dokument :");
			System.out.println(ex.getMessage());
			System.exit(-1);

		} catch (Exception ex) {
			System.out.println("Ako se ova linija ikada izvrši, pali ste vještinu!");
			System.exit(-1);
		}
		WriterVisitor visitor = new WriterVisitor();
		parser.getDocumentNode().accept(visitor);
	}

	/**
	 * Razred pomoću kojeg obavljamo demonstraciju Posjetitelja.
	 * 
	 * @author Jure Šiljeg
	 *
	 */
	public static class WriterVisitor implements INodeVisitor {

		/**
		 * @see hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor#visitTextNode(hr.fer.zemris.java.custom.scripting.nodes.TextNode)
		 */
		@Override
		public void visitTextNode(TextNode node) {
			System.out.print(node);
		}

		/**
		 * @see hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor#visitForLoopNode(hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode)
		 */
		@Override
		public void visitForLoopNode(ForLoopNode node) {
			System.out.print(node);
			int childrenCount = node.numberOfChildren();
			for (int i = 0; i < childrenCount; i++) {
				Node child = node.getChild(i);
				child.accept(this);
			}
			System.out.print("{$END$}");
		}

		/**
		 * @see hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor#visitEchoNode(hr.fer.zemris.java.custom.scripting.nodes.EchoNode)
		 */
		@Override
		public void visitEchoNode(EchoNode node) {
			System.out.print(node);
		}

		/**
		 * @see hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor#visitDocumentNode(hr.fer.zemris.java.custom.scripting.nodes.DocumentNode)
		 */
		@Override
		public void visitDocumentNode(DocumentNode node) {
			System.out.println("Tražili ste sljedeći ispis: \n \n");
			int childrenCount = node.numberOfChildren();
			for (int i = 0; i < childrenCount; ++i) {
				Node n = node.getChild(i);
				n.accept(this);
			}

		}

	}

}
