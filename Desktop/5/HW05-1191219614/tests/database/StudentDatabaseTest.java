import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

import hr.fer.zemris.java.tecaj.hw5.db.IndexQueryFilter;
import hr.fer.zemris.java.tecaj.hw5.db.QueryFilter;
import hr.fer.zemris.java.tecaj.hw5.db.StudentDatabase;
import hr.fer.zemris.java.tecaj.hw5.db.StudentRecord;

@SuppressWarnings("javadoc")
public class StudentDatabaseTest {

	@Test
	public void testSimpleIndexQuery() throws IOException {
		List<String> database = Files.readAllLines(Paths.get("database.txt"), StandardCharsets.UTF_8);

		String input = "indexquery jmbag=\"0000000034\"";
		StudentDatabase stdDb = new StudentDatabase(database);
		IndexQueryFilter indexQ = new IndexQueryFilter(input);

		StudentRecord student = stdDb.forJMBAG(indexQ.getJmbag());

		assertEquals(student.getLastName(), "Majić");
	}

	@Test
	public void testSimpleQuery() throws IOException {
		List<String> database = Files.readAllLines(Paths.get("database.txt"), StandardCharsets.UTF_8);

		String input = "query firstName>\"A\"";
		StudentDatabase stdDb = new StudentDatabase(database);

		List<StudentRecord> list = stdDb.filter(new QueryFilter(input.substring(5).trim()));

		assertEquals(list.get(9).getLastName(), "Dokleja");
	}

	@Test
	public void testMoreComplexQuery() throws IOException {
		List<String> database = Files.readAllLines(Paths.get("database.txt"), StandardCharsets.UTF_8);

		String input = "query firstName>\"A\" AnD jmbag=\"0000000021\"";
		StudentDatabase stdDb = new StudentDatabase(database);

		List<StudentRecord> list = stdDb.filter(new QueryFilter(input.substring(5).trim()));

		assertEquals(list.get(0).getLastName(), "Jakobušić");
	}

	@Test
	public void testLittleMoreComplexQuery() throws IOException {
		List<String> database = Files.readAllLines(Paths.get("database.txt"), StandardCharsets.UTF_8);

		String input = "query firstName>\"A\" AnD jmbag>\"0000000021\" aND lastName LIKE \"*ić\"";
		StudentDatabase stdDb = new StudentDatabase(database);

		List<StudentRecord> list = stdDb.filter(new QueryFilter(input.substring(5).trim()));

		assertEquals(list.get(3).getFirstName(), "Nenad");
	}

	@Test
	public void testLikeStarFromBegin() throws IOException {
		List<String> database = Files.readAllLines(Paths.get("database.txt"), StandardCharsets.UTF_8);

		String input = "query lastName LIKE \"*ić\" aNd firstName LIKE \"*a\"";
		StudentDatabase stdDb = new StudentDatabase(database);

		List<StudentRecord> list = stdDb.filter(new QueryFilter(input.substring(5).trim()));

		assertEquals(list.get(2).getFirstName(), "Jura");
	}

	@Test
	public void testLikeStarFromBehind() throws IOException {
		List<String> database = Files.readAllLines(Paths.get("database.txt"), StandardCharsets.UTF_8);

		String input = "query lastName LIKE \"Ko*\" aNd firstName LIKE \"N*\"";
		StudentDatabase stdDb = new StudentDatabase(database);

		List<StudentRecord> list = stdDb.filter(new QueryFilter(input.substring(5).trim()));

		assertEquals(list.get(0).getLastName(), "Kosanović");
	}

	@Test
	public void testLikeStarMixed() throws IOException {
		List<String> database = Files.readAllLines(Paths.get("database.txt"), StandardCharsets.UTF_8);

		String input = "query lastName LIKE \"K*\" aNd firstName LIKE \"Bo*\" anD firstName LIKE \"*s\" ";
		StudentDatabase stdDb = new StudentDatabase(database);

		List<StudentRecord> list = stdDb.filter(new QueryFilter(input.substring(5).trim()));

		assertEquals(list.get(0).getFirstName(), "Boris");
	}

	@Test
	public void testLikeStarMiddle() throws IOException {
		List<String> database = Files.readAllLines(Paths.get("database.txt"), StandardCharsets.UTF_8);

		String input = "query  lastName LIKE \"K*ć\" aNd firstName LIKE \"N*\"";
		StudentDatabase stdDb = new StudentDatabase(database);

		List<StudentRecord> list = stdDb.filter(new QueryFilter(input.substring(5).trim()));

		assertEquals(list.get(0).getFirstName(), "Nenad");
	}

	@Test
	public void testSomeComplexQuery() throws IOException {
		List<String> database = Files.readAllLines(Paths.get("database.txt"), StandardCharsets.UTF_8);

		String input = "query firstName>\"A\" and lastName LIKE \"B*ć\" aNd jmbag >= \"0000000003\" ANd jmbag <= \"0000000045\" AND lastName LIKE \"Bre*\"";
		StudentDatabase stdDb = new StudentDatabase(database);

		List<StudentRecord> list = stdDb.filter(new QueryFilter(input.substring(5).trim()));

		assertEquals(list.get(0).getFirstName(), "Jusufadis");
	}

}
