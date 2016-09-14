package hr.fer.zemris.java.custom.scripting.exec;

/**
 * Razred koji predstavlja omotač za vrijednost. Bit će nam dozvoljene null
 * vrijednost, String, Integer i Double primjerci.
 * 
 * @author Jure Šiljeg
 * @version 1.0.
 */
public class ValueWrapper {
	/** Vrijednost koju želimo "omotati" */
	private Object value;

	/**
	 * Konstruktor za vrijednost.
	 * 
	 * @param value
	 *            zadana vrijednost.
	 */
	public ValueWrapper(Object value) {
		super();
		this.value = value;
	}

	/**
	 * Getter za vrijednost.
	 * 
	 * @return tu vrijednost.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Setter na vrijednost.
	 * 
	 * @param value
	 *            zadana vrijednost koju postavljamo.
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Metoda koja vrši detekciju tipa objekta kojem pripadaju argumenti, te ih
	 * potom posprema s odgovarajućim tipom u uređeni par.
	 * 
	 * @param firstVal
	 *            prvi argument kojem detektiramo tip.
	 * @param second
	 *            drugi argument kojem detektiramo tip.
	 * @throws RuntimeException
	 *             ako nije moguće parsirati u Integer ili Double.
	 * @return uređeni par s pronađenim vrijednostima.
	 */
	private Pair checkInstances(ValueWrapper firstVal, Object second) {
		Pair p = new Pair();
		Object first = (Object) firstVal.getValue();

		if (first == null) {
			p.setFirst(Integer.valueOf(0));
		}
		if (second == null) {
			p.setSecond(Integer.valueOf(0));
		}

		if (first instanceof String) {
			try {
				p.setFirst(Integer.valueOf(Integer.parseInt((String) first)));
			} catch (NumberFormatException e) {
				try {
					p.setFirst(Double.valueOf(Double.parseDouble((String) first)));

				} catch (NumberFormatException e1) {
					throw new RuntimeException("Nije moguće pretvoriti prvi String ni u Double niti u Integer!");
				}
			}
		}
		if (second instanceof String) {
			try {
				p.setSecond(Integer.valueOf(Integer.parseInt((String) second)));
			} catch (NumberFormatException e) {
				try {
					p.setSecond(Double.valueOf(Double.parseDouble((String) second)));

				} catch (NumberFormatException e1) {
					throw new RuntimeException("Nije moguće pretvoriti drugi String ni u Double niti u Integer!");
				}
			}
		}
		if (first instanceof Double) {
			p.setFirst((Double) first);
		}
		if (second instanceof Double) {
			p.setSecond((Double) second);
		}
		if (first instanceof Integer) {
			p.setFirst((Integer) first);
		}
		if (second instanceof Integer) {
			p.setSecond((Integer) second);
		}
		if (p.getFirst() == null || p.getSecond() == null) {
			throw new RuntimeException("Uneseni tipovi nisu odgovarajući");
		} else {
			return p;
		}
	}

	/**
	 * Metoda obavlja inkrementiranje elementa koji poziva metodu elementom iz
	 * argumenta.
	 * 
	 * @param incValue
	 *            vrijednost inkrementa.
	 * @return Modificirani objekt nad kojim smo pozvali operaciju
	 */
	public ValueWrapper increment(Object incValue) {

		Pair p = checkInstances(this, incValue);
		// oba Integeri
		if (p.getFirst() instanceof Integer && p.getSecond() instanceof Integer) {
			this.setValue((Integer) p.getFirst() + (Integer) p.getSecond());
		} else { // kombinacija Double vs. Integer ili Double vs Double
			if (p.getFirst() instanceof Double && p.getSecond() instanceof Double) {
				this.setValue((Double) p.getFirst() + (Double) p.getSecond());
			} else if (p.getFirst() instanceof Double && p.getSecond() instanceof Integer) {
				this.setValue((Double) p.getFirst() + (Integer) p.getSecond());
			} else {
				this.setValue((Integer) p.getFirst() + (Double) p.getSecond());
			}
		}
		return this;
	}

	/**
	 * Metoda obavlja dekrementiranje elementa koji poziva metodu elementom iz
	 * argumenta.
	 * 
	 * @param decValue
	 *            vrijednost dekrementa.
	 * @return Modificirani objekt nad kojim smo pozvali operaciju
	 */
	public ValueWrapper decrement(Object decValue) {
		Pair p = checkInstances(this, decValue);
		if (p.getFirst() instanceof Integer && p.getSecond() instanceof Integer) {
			this.setValue((Integer) p.getFirst() - (Integer) p.getSecond());
		} else {// kombinacija Double vs. Integer ili Double vs Double
			if (p.getFirst() instanceof Double && p.getSecond() instanceof Double) {
				this.setValue((Double) p.getFirst() - (Double) p.getSecond());
			} else if (p.getFirst() instanceof Double && p.getSecond() instanceof Integer) {
				this.setValue((Double) p.getFirst() - (Integer) p.getSecond());
			} else {
				this.setValue((Integer) p.getFirst() - (Double) p.getSecond());
			}
		}
		return this;
	}

	/**
	 * Metoda obavlja množenje elementa koji poziva metodu elementom iz
	 * argumenta.
	 * 
	 * @param mulValue
	 *            vrijednost faktora kojim množimo.
	 * @return Modificirani objekt nad kojim smo pozvali operaciju
	 */
	public ValueWrapper multiply(Object mulValue) {
		Pair p = checkInstances(this, mulValue);
		if (p.getFirst() instanceof Integer && p.getSecond() instanceof Integer) {
			this.setValue((Integer) p.getFirst() * (Integer) p.getSecond());
		} else { // kombinacija Double vs. Integer ili Double vs Double
			if (p.getFirst() instanceof Double && p.getSecond() instanceof Double) {
				this.setValue((Double) p.getFirst() * (Double) p.getSecond());
			} else if (p.getFirst() instanceof Double && p.getSecond() instanceof Integer) {
				this.setValue((Double) p.getFirst() * (Integer) p.getSecond());
			} else {
				this.setValue((Integer) p.getFirst() * (Double) p.getSecond());
			}
		}
		return this;
	}

	/**
	 * Metoda obavlja dijeljenje elementa koji poziva metodu elementom iz
	 * argumenta.
	 * 
	 * @param divValue
	 *            vrijednost faktora kojim dijelimo.
	 * @return Modificirani objekt nad kojim smo pozvali operaciju
	 */
	public ValueWrapper divide(Object divValue) {
		Pair p = checkInstances(this, divValue);
		if (p.getSecond() instanceof Integer && (Integer) p.getSecond() == 0
				|| p.getSecond() instanceof Double && (Double) p.getSecond() == 0.0) {
			throw new ArithmeticException("Nedozvoljeno dijeljenje s nulom!");
		}
		if (p.getFirst() instanceof Integer && p.getSecond() instanceof Integer) {
			this.setValue((Integer) p.getFirst() / (Integer) p.getSecond());
		} else { // kombinacija Double vs. Integer ili Double vs Double
			if (p.getFirst() instanceof Double && p.getSecond() instanceof Double) {
				this.setValue((Double) p.getFirst() / (Double) p.getSecond());
			} else if (p.getFirst() instanceof Double && p.getSecond() instanceof Integer) {
				this.setValue((Double) p.getFirst() / (Integer) p.getSecond());
			} else {
				this.setValue((Integer) p.getFirst() / (Double) p.getSecond());
			}
		}
		return this;

	}

	/**
	 * Metoda obavlja usporedbu elementa koji poziva metodu elementom iz
	 * argumenta.
	 * 
	 * @param withValue
	 *            vrijednost s kojom uspoređujemo.
	 * @return broj manji od nula ako je broj nad kojim pozivamo manji, veći od
	 *         nula ako je nad kojim pozivamo veći, inače vrati nulu.
	 */
	public int numCompare(Object withValue) {
		ValueWrapper first = new ValueWrapper(this.getValue());
		// koristimo se trikom i promatramo kolika je razlika
		first.decrement(withValue);
		Object firstObj = (Object) first.getValue();
		if (firstObj instanceof Integer) {
			if ((Integer) firstObj < 0) {
				return -1;
			} else if ((Integer) firstObj > 0) {
				return 1;
			} else {
				return 0;
			}
		} else {
			if ((Double) firstObj < 0) {
				return -1;
			} else if ((Double) firstObj > 0) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	/**
	 * Statički razred kojim modeliramo uređeni par.
	 * 
	 * @author jure Šiljeg
	 * @version 1.0.
	 */
	private static class Pair {
		/** Prvi element uređenog para */
		Object first;
		/** Drugi element uređenog para */
		Object second;

		/**
		 * Defaultni konstruktor koji nema nikakvu funkcionalnost.
		 */
		public Pair() {
		}

		/**
		 * Konstruktor za inicijalizaciju uređenog para.
		 * 
		 * @param first
		 *            prvi element para.
		 * @param second
		 *            drugi element para.
		 */
		@SuppressWarnings("unused")
		public Pair(Object first, Object second) {
			this.first = first;
			this.second = second;
		}

		/**
		 * Getter za prvi element.
		 * 
		 * @return taj element.
		 */
		public Object getFirst() {
			return first;
		}

		/**
		 * Getter za drugi element.
		 * 
		 * @return taj element.
		 */
		public Object getSecond() {
			return second;
		}

		/**
		 * Setter za prvi element.
		 * 
		 * @param first
		 *            vrijednost na koju postavljamo.
		 */
		public void setFirst(Object first) {
			this.first = first;
		}

		/**
		 * Setter za drugi element.
		 * 
		 * @param second
		 *            vrijednost na koju postavljamo.
		 */
		public void setSecond(Object second) {
			this.second = second;
		}

	}

}
