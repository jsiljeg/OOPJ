package hr.fer.zemris.java.simplecomp;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import hr.fer.zemris.java.simplecomp.impl.ComputerImpl;
import hr.fer.zemris.java.simplecomp.impl.ExecutionUnitImpl;
import hr.fer.zemris.java.simplecomp.models.Computer;
import hr.fer.zemris.java.simplecomp.models.ExecutionUnit;
import hr.fer.zemris.java.simplecomp.models.InstructionCreator;
import hr.fer.zemris.java.simplecomp.parser.InstructionCreatorImpl;
import hr.fer.zemris.java.simplecomp.parser.ProgramParser;

/**
 * Razred koji služi za simulaciju računala i pojedinih primjera.
 * 
 * @author Jure Šiljeg
 * @version 1.0
 */
public class Simulator {
	/**
	 * Glavna metoda koja simulira rad računala.
	 * 
	 * @param args
	 *            argumenti komandne linije. Treba postaviti stazu do željne
	 *            datoteke iz examples foldera.
	 * @throws Exception
	 *             u slučaju nemogućnosti učitavanja iz konzole.
	 */
	public static void main(String[] args) throws Exception {
		String path = "";
		if (args.length == 1) {
			path = args[0];
		}
		if (args.length != 1) {
			System.out.println("Unesite stazu do datoteke!");
			System.out.print("> ");
			BufferedReader buffReader = new BufferedReader(new InputStreamReader(System.in));
			path = buffReader.readLine();
		}
		File f = new File(path);
		if (!f.exists()) {
			System.out.println("Unijeli ste nepostojeću stazu!");
			return;
		}
		// Stvori računalo s 256 memorijskih lokacija i 16 registara
		try {
			Computer comp = new ComputerImpl(256, 16);
			// Stvori objekt koji zna stvarati primjerke instrukcija
			InstructionCreator creator = new InstructionCreatorImpl("hr.fer.zemris.java.simplecomp.impl.instructions");
			// Napuni memoriju računala programom iz datoteke; instrukcije
			// stvaraj
			// uporabom predanog objekta za stvaranje instrukcija
			ProgramParser.parse(path, comp, creator);
			ExecutionUnit exec = new ExecutionUnitImpl();
			// Izvedi program
			exec.go(comp);
		} catch (Exception e) {
			System.out.println("Pogrješka pri radu s datotekom: " + path);
			e.printStackTrace();
			return;
		}
	}
}
