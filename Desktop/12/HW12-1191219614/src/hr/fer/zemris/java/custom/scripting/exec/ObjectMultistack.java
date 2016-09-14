package hr.fer.zemris.java.custom.scripting.exec;

import java.util.EmptyStackException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Razred u kojem implementiramo jednostavne funkcionalnosti multistoga koje i
 * sam stog ima.
 * 
 * @author Jure Šiljeg
 * @version 1.0.
 */
public class ObjectMultistack {

	/**
	 * Mapa koja kao ključ ima string, a kao vrijednost čitav stog raznih
	 * objekata
	 */
	private Map<String, MultistackEntry> map;

	/**
	 * Konstruktor za inicijalizaciju mape.
	 */
	public ObjectMultistack() {
		map = new LinkedHashMap<String, MultistackEntry>();
	}

	/**
	 * Metoda za dodavanje elementa na vrh multistoga.
	 * 
	 * @param name
	 *            ključ po kojem identificiramo multistog.
	 * @param valueWrapper
	 *            vrijednost koja se dodaje na sam vrh.
	 */
	public void push(String name, ValueWrapper valueWrapper) {
		if (!map.containsKey(name)) {
			map.put(name, new MultistackEntry(valueWrapper));
		} else { // dodamo na početak liste
			MultistackEntry newNode = new MultistackEntry(valueWrapper);
			newNode.next = map.get(name);
			map.put(name, newNode);
		}

	}

	/**
	 * Metoda za uklanjanje elementa s vrha multistoga.
	 * 
	 * @param name
	 *            ključ po kojem identificiramo multistog. Mapa mora sadržavati
	 *            ime, inače se baca iznimka o praznom multistogu.
	 * @return vrijednost koja se uklanja.
	 */
	public ValueWrapper pop(String name) {
		if (!map.containsKey(name)) {
			throw new EmptyStackException();
		}
		MultistackEntry newNode = map.get(name);
		if (newNode.next == null) {
			map.remove(name);
		} else {
			map.put(name, newNode.next);
		}
		return newNode.getVw();
	}

	/**
	 * Metoda koja vraća vršni element stoga.
	 * 
	 * @param name
	 *            ključ po kojem identificiramo multistog. Mapa mora sadržavati
	 *            ime, inače se baca iznimka o praznom multistogu.
	 * @return vrijednost koja se uklanja.
	 */
	public ValueWrapper peek(String name) {
		if (!map.containsKey(name)) {
			throw new EmptyStackException();
		}
		return map.get(name).getVw();
	}

	/**
	 * Metoda provjerava postoje li elementi na multistogu.
	 * 
	 * @param name
	 *            ključ po kojem identificiramo multistog.
	 * @return istinu ako ne postoje elementi (prazan multistog), inače laž.
	 */
	public boolean isEmpty(String name) {
		return !map.containsKey(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((map == null) ? 0 : map.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObjectMultistack other = (ObjectMultistack) obj;
		if (map == null) {
			if (other.map != null)
				return false;
		} else if (!map.equals(other.map))
			return false;
		return true;
	}

	/**
	 * Razred koji reprezentira čvor vezane liste pomoću koje je izveden stog.
	 * 
	 * @author Jure Šiljeg
	 * @version 1.0.
	 */
	public static class MultistackEntry {
		/** Vrijednost koja se ubacuje u listu (naš multistog) */
		private ValueWrapper vw;
		/** Referenca na sljedeći čvor liste */
		private MultistackEntry next;

		/**
		 * Kontruktor za vrijednost koju nosimo u čvoru.
		 * 
		 * @param vw
		 *            vrijednost kojom punimo čvor.
		 */
		public MultistackEntry(ValueWrapper vw) {
			this.vw = vw;
		}

		/**
		 * Getter za vrijednost iz čvora.
		 * 
		 * @return tu vrijednost.
		 */
		public ValueWrapper getVw() {
			return vw;
		}

		/**
		 * Setter za vrijednost čvora.
		 * 
		 * @param vw
		 *            vrijednost na koju postavljamo.
		 */
		public void setVw(ValueWrapper vw) {
			this.vw = vw;
		}

	}
}
