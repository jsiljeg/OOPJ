import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Ignore;
import org.junit.Test;

import hr.fer.zemris.java.tecaj.hw5.collections.SimpleHashtable;

@SuppressWarnings("javadoc")
public class SHMTest {

	@Test
	public void outputEmptyTable() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		assertEquals("[]", examMarks.toString());
	}

	@Test
	public void simplePut() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		String s = "[Ante=2, Ivana=2, Jasna=2]";
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		assertEquals(s, examMarks.toString());
	}

	@Test
	public void putWithSameValue() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		String s = "[Ante=2, Ivana=5, Jasna=2]";
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Ivana", 5);
		assertEquals(s, examMarks.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void putNullKey() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		examMarks.put(null, 2);
	}

	@Test
	public void putNullValue() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		String s = "[Kosjenka=null]";
		examMarks.put("Kosjenka", null);
		assertEquals(s, examMarks.toString());
	}

	@Test
	public void testSimpleGet() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Ivana", 5);
		assertEquals((int) 2, (int) examMarks.get((Object) "Jasna"));
	}

	@Test
	public void testNullKeyGet() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Ivana", 5);
		assertEquals(null, examMarks.get((Object) null));
	}

	@Test
	public void testNoOccuranceOfElementInGet() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Ivana", 5);
		assertEquals(null, examMarks.get((Object) "Kavgadžija"));
	}

	@Test
	public void testSizeSimple() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		assertEquals(3, examMarks.size());
	}

	@Test
	public void testSizeAfterAddingSameValue() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Ivana", 5);
		assertEquals(3, examMarks.size());
	}

	@Test
	public void testSizeOfEmptyTable() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		assertEquals(0, examMarks.size());
	}

	@Test
	public void testSimpleContainsKey() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		assertEquals(true, examMarks.containsKey((Object) "Jasna"));
	}

	@Test
	public void testContainsKeyForNullKey() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		assertEquals(false, examMarks.containsKey((Object) null));
	}

	@Test
	public void testContainsKeyForNoKeyInTable() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		assertEquals(false, examMarks.containsKey((Object) "Hnjonjonjo"));
	}

	public void testSimpleContainsValue() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 3);
		examMarks.put("Jasna", 5);
		assertEquals(true, examMarks.containsValue(3));
	}

	@Test
	public void testContainsValueForNullValue() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 4);
		examMarks.put("Jasna", null);

		assertEquals(true, examMarks.containsValue(null));
	}

	@Test
	public void testContainsValueForNoKeyInTable() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		examMarks.put("Ivana", 211);
		examMarks.put("Ante", 21);
		examMarks.put("Jasna", 22);
		assertEquals(false, examMarks.containsValue(65));
	}

	@Test
	public void testSimpleRemoveFromTable() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		String s = "[Markica=211, Slavica Čukteraš=21, Perkica=21, Ante=21, Jozica=211, Jasna=22]";
		examMarks.put("Ivana", 211);
		examMarks.put("Ante", 21);
		examMarks.put("Jasna", 22);
		examMarks.put("Markica", 211);
		examMarks.put("Perkica", 21);
		examMarks.put("Jozica", 211);
		examMarks.put("Slavica Čukteraš", 21);
		// [Markica=211, Slavica Čukteraš=21, Ivana=211, Perkica=21,
		// Kosjenka=null, Ante=2, Jozica=211, Jasna=2]
		examMarks.remove((Object) "Ivana");
		// [Markica=211, Slavica Čukteraš=21, Perkica=21, Kosjenka=null, Ante=2,
		// Jozica=211, Jasna=2]
		assertEquals(s, examMarks.toString());
	}

	@Test
	public void testRemoveFromBeginningOfTable() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		String s = "[Slavica Čukteraš=21, Ivana=211, Perkica=21, Ante=21, Jozica=211, Jasna=22]";
		examMarks.put("Ivana", 211);
		examMarks.put("Ante", 21);
		examMarks.put("Jasna", 22);
		examMarks.put("Markica", 211);
		examMarks.put("Perkica", 21);
		examMarks.put("Jozica", 211);
		examMarks.put("Slavica Čukteraš", 21);
		// [Markica=211, Slavica Čukteraš=21, Ivana=211, Perkica=21,
		// Kosjenka=null, Ante=2, Jozica=211, Jasna=2]
		examMarks.remove((Object) "Markica");
		// [Slavica Čukteraš=21, Ivana=211, Perkica=21, Kosjenka=null, Ante=2,
		// Jozica=211, Jasna=2]
		assertEquals(s, examMarks.toString());
	}

	@Test
	public void testRemoveFromMiddleOfTable() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		String s = "[Markica=211, Slavica Čukteraš=21, Ivana=211, Ante=21, Jozica=211, Jasna=22]";
		examMarks.put("Ivana", 211);
		examMarks.put("Ante", 21);
		examMarks.put("Jasna", 22);
		examMarks.put("Markica", 211);
		examMarks.put("Perkica", 21);
		examMarks.put("Jozica", 211);
		examMarks.put("Slavica Čukteraš", 21);
		examMarks.remove((Object) "Perkica");
		assertEquals(s, examMarks.toString());
	}

	@Test
	public void testRemoveFromEndOfTable() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		String s = "[Markica=211, Slavica Čukteraš=21, Ivana=211, Perkica=21, Ante=21, Jozica=211]";
		examMarks.put("Ivana", 211);
		examMarks.put("Ante", 21);
		examMarks.put("Jasna", 22);
		examMarks.put("Markica", 211);
		examMarks.put("Perkica", 21);
		examMarks.put("Jozica", 211);
		examMarks.put("Slavica Čukteraš", 21);
		examMarks.remove((Object) "Jasna");
		assertEquals(s, examMarks.toString());
	}

	@Test
	public void testRemoveWithElementNotInTable() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		String s = "[Perkica=21, Ante=21, Markica=211, Jozica=211, Slavica Čukteraš=21, Ivana=211]";
		examMarks.put("Ivana", 211);
		examMarks.put("Ante", 21);
		examMarks.put("Markica", 211);
		examMarks.put("Perkica", 21);
		examMarks.put("Jozica", 211);
		examMarks.put("Slavica Čukteraš", 21);
		examMarks.remove((Object) "Tihana");
		assertEquals(s, examMarks.toString());
	}

	@Test
	public void isEmpty() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		assertEquals(true, examMarks.isEmpty());
	}

	@Test
	public void isNotEmpty() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		examMarks.put("Ivana", 211);
		assertEquals(false, examMarks.isEmpty());
	}

	@Test
	public void testClear() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		examMarks.put("Ivana", 211);
		examMarks.put("Ante", 21);
		examMarks.put("Markica", 211);
		examMarks.put("Perkica", 21);
		examMarks.put("Jozica", 211);
		examMarks.put("Slavica Čukteraš", 21);
		assertEquals(6, examMarks.size());
		examMarks.clear();
		assertEquals(0, examMarks.size());
	}

	@Test
	public void hasNextOnEmptyCollection() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		assertEquals(false, iter.hasNext());
	}

	@Test(expected = NoSuchElementException.class)
	public void testNextOnEmptyCollection() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		iter.next();
	}

	@Test
	public void hasNextOnNonEmptyCollection() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		examMarks.put("Sinan Sakić", 211);
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		assertEquals(true, iter.hasNext());
		assertEquals(true, iter.hasNext());
		assertEquals(true, iter.hasNext());
	}

	@Test
	public void hasNextAndNextOnNonEmptyCollection() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		examMarks.put("Pišem ovo u 3ipo ujtru i moram predat za par sati. Ranit ću se.", 211);
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		assertEquals(true, iter.hasNext());
		assertEquals("Pišem ovo u 3ipo ujtru i moram predat za par sati. Ranit ću se.", iter.next().getKey());
		assertEquals(false, iter.hasNext());
		assertEquals(false, iter.hasNext());
	}

	@Test
	public void testRemoveOnNonEmptyCollection() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		examMarks.put("Pišem ovo u 3ipo ujtru i moram predat za par sati. Ranit ću se.", 211);
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		assertEquals(true, iter.hasNext());
		iter.next();
		iter.remove();
		assertEquals(0, examMarks.size());
	}

	@Test(expected = IllegalStateException.class)
	public void testRemoveRemoveOnNonEmptyCollection() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		examMarks.put("Pišem ovo u 3ipo ujtru i moram predat za par sati. Ranit ću se.", 211);
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		assertEquals(true, iter.hasNext());
		iter.next();
		iter.remove();
		assertEquals(0, examMarks.size());
		iter.remove();
	}

	@Test
	public void testThroughItemsOnNonEmptyCollection() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);

		String finalSyso = "";
		int controlSyso = "Potražit ću oči nešto zelenije, potražit ću srce nešto iskrenije. Potražit ću ženu koja umije moju mladooost da razumijeeee!!"
				.length();
		examMarks.put("Potražit ću oči nešto zelenije, ", 21);
		examMarks.put("potražit ću srce nešto iskrenije. ", 3);
		examMarks.put("Potražit ću ženu koja umije ", 23);
		examMarks.put("moju mladooost ", 11);
		examMarks.put("da razumijeeee!!", null);

		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		while (iter.hasNext()) {
			SimpleHashtable.TableEntry<String, Integer> pair = iter.next();
			finalSyso += pair.getKey().toString();
		}
		assertEquals(finalSyso.length(), controlSyso);
	}

	@Ignore
	@Test
	public void removeAllWithIterator() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);

		examMarks.put("Potražit ću oči nešto zelenije, ", 21);
		examMarks.put("potražit ću srce nešto iskrenije. ", 3);
		examMarks.put("Potražit ću ženu koja umije ", 23);
		examMarks.put("moju mladooost ", 11);
		examMarks.put("da razumijeeee!!", null);

		for (Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator(); iter.hasNext();) {
			iter.next();
			iter.remove();
		}

		assertEquals(0, examMarks.size());
	}

}
