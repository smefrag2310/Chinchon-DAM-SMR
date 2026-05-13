package chinchon.dominio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CombinationAnalyzer {

	private static CombinationAnalyzer analyzer;

	private CombinationAnalyzer() {

	}

	public static CombinationAnalyzer getInstance() {
		if (analyzer == null) {
			analyzer = new CombinationAnalyzer();
		}
		return analyzer;
	}

	public List<Combination> obtainCombinations(List<Card> hand) {

		List<Card> remaining = new ArrayList<>(hand);
		List<Combination> combinations = new ArrayList<>();
		List<Combination> ladder = new ArrayList<>();
		List<Combination> triple = new ArrayList<>();

		ladder = findLadderAndChinchon(remaining);
		
		adjustLaddersForAlmostTriples(ladder, remaining);
		
		for (Combination c : ladder) {
			combinations.add(c);
			remaining.removeAll(c.getCards());
		}

		triple = findEqualNumber(remaining);
		for (Combination c : triple) {
			combinations.add(c);
			remaining.removeAll(c.getCards());
		}

		return combinations;
	}

	public boolean checkCombinations(List<Card> hand) {

		List<Card> combinationsCards;

		combinationsCards = obtainCombinations(hand).stream()
				.flatMap(c -> c.getCards().stream())
				.toList();

		return combinationsCards.size() >= 6;

	}

	public List<Combination> findLadderAndChinchon(List<Card> remaining) {

		List<Card> list = new ArrayList<>();
		List<Card> actual = new ArrayList<>();
		List<Combination> sequence = new ArrayList<>();
		Card last;

		for (Suit suit : Suit.values()) {

			list = remaining.stream()
					.filter(c -> c.getSuit() == suit)
					.sorted(Comparator.comparing(c -> c.getValue()))
					.toList();

			actual.clear();

			for (Card c : list) {
				if (actual.isEmpty()) {
					actual.add(c);
				} else {
					last = actual.get(actual.size() - 1);

					if (c.getValue().getOrder() == last.getValue().getOrder() + 1) {
						actual.add(c);
					} else {

						if (actual.size() >= 3) {
							sequence.add(createLadderOrChinchon(new ArrayList<>(actual)));
						}
						actual.clear();
						actual.add(c);
					}
				}
			}
			if (actual.size() >= 3) {
				sequence.add(createLadderOrChinchon(new ArrayList<>(actual)));
			}
		}
		return sequence;
	}

	public List<Combination> findEqualNumber(List<Card> remaining) {

		List<Card> list = new ArrayList<>();
		List<Combination> sequence = new ArrayList<>();

		for (Value value : Value.values()) {

			list = remaining.stream()
					.filter(c -> c.getValue() == value)
					.toList();

			if (list.size() >= 3) {
				sequence.add(createTriples(list));
			}
		}
		return sequence;
	}

	public Combination createLadderOrChinchon(List<Card> list) {
		if (list.size() == 7) {
			return new Combination(list, CombinationType.CHINCHON);
		}
		return new Combination(list, CombinationType.LADDER);
	}

	public Combination createTriples(List<Card> list) {
		return new Combination(list, CombinationType.TRIPLE);
	}

	public int calculatePoints(List<Combination> combinations, List<Card> hand) {

		List<Card> remainingCards = new ArrayList<>();
		Set<Card> combinationCards;
		int punctuation = 0;

		combinationCards = combinations.stream().flatMap(c -> c.getCards().stream()).collect(Collectors.toSet());

		remainingCards = hand.stream()
				.filter(c -> !combinationCards.contains(c))
				.toList();

		if (remainingCards.isEmpty()) {
			punctuation -= 10;
		} else {
			punctuation = remainingCards.stream()
					.mapToInt(c -> c.getValue().getValue())
					.sum();
		}

		return punctuation;

	}

	public int currentPoints(List<Combination> combinations, List<Card> hand, int points) {

		List<Card> remainingCards = new ArrayList<>();
		Set<Card> combinationCards;
		int punctuation;

		combinationCards = combinations.stream()
				.flatMap(c -> c.getCards().stream())
				.collect(Collectors.toSet());

		remainingCards = hand.stream()
				.filter(c -> !combinationCards.contains(c))
				.toList();

		if (remainingCards.isEmpty()) {
			punctuation = -10;
		} else {
			punctuation = remainingCards.stream().mapToInt(c -> c.getValue().getValue()).sum();
		}

		return points + punctuation;

	}

	public List<Card> protectedCards(List<Card> hand) {

		List<Combination> combinations = new ArrayList<>(analyzer.obtainCombinations(hand));
		List<Card> combinatedCards = new ArrayList<>();
		List<Card> protectedCards = new ArrayList<>();

		combinatedCards = combinations.stream()
				.flatMap(c -> c.getCards().stream())
				.toList();

		protectedCards.addAll(combinatedCards);

		protectedCards.addAll(findAlmostLadders(hand));

		protectedCards.addAll(findAlmostTriples(hand));

		return protectedCards;

	}

	public List<Card> cardsForSearch(List<Card> hand) {

		List<Card> missing = new ArrayList<>();
		Card c1, c2;
		int v1, v2, min, max;

		for (int i = 0; i < hand.size(); i++) {
			for (int j = i + 1; j < hand.size(); j++) {

				c1 = hand.get(i);
				c2 = hand.get(j);

				if (c1.getSuit() == c2.getSuit()) {

					v1 = c1.getValue().getOrder();
					v2 = c2.getValue().getOrder();

					min = Math.min(v1, v2);
					max = Math.max(v1, v2);

					if (max - min == 2) {
						missing.add(new Card(c1.getSuit(), Value.fromOrder(min + 1)));
					}

					if (max - min == 1) {
						if (min > 1) {
							missing.add(new Card(c1.getSuit(), Value.fromOrder(min - 1)));
						}
						if (max < 10) {
							missing.add(new Card(c1.getSuit(), Value.fromOrder(max + 1)));
						}
					}

				}

				if (c1.getValue() == c2.getValue()) {

					for (Suit s : Suit.values()) {
						missing.add(new Card(s, c1.getValue()));
					}

				}

			}
		}

		return missing;

	}

	public List<Card> findAlmostLadders(List<Card> cards) {

		List<Card> list = new ArrayList<>();
		List<Card> sequence = new ArrayList<>();
		Card c1, c2;
		int diff;

		for (Suit suit : Suit.values()) {

			list = cards.stream()
					.filter(c -> c.getSuit() == suit)
					.sorted(Comparator.comparing(c -> c.getValue()))
					.toList();

			if (list.size() >= 2) {

				for (int i = 0; i < list.size() - 1; i++) {

					c1 = list.get(i);
					c2 = list.get(i + 1);

					diff = c2.getValue().getOrder() - c1.getValue().getOrder();

					if (diff == 1 || diff == 2) {

						sequence.add(c1);
						sequence.add(c2);
					}

				}
			}
		}
		return sequence;

	}

	public List<Card> findAlmostTriples(List<Card> cards) {

		List<Card> list = new ArrayList<>();
		List<Card> sequence = new ArrayList<>();

		for (Value value : Value.values()) {

			list = cards.stream().filter(c -> c.getValue() == value).toList();

			if (list.size() == 2) {
				sequence.addAll(list);
			}
		}
		return sequence;

	}

	private void adjustLaddersForAlmostTriples(List<Combination> ladders, List<Card> remaining) {

		List<Card> almostTripleCards = findAlmostTriples(remaining);
		Set<Value> almostTripleValues = new HashSet<>();
		List<Card> cards = new ArrayList<>();
		Card c1, c2;

		if (almostTripleCards.isEmpty()) {
			return;
		}

		almostTripleValues = almostTripleCards.stream()
				.map(Card::getValue)
				.collect(Collectors.toSet());

		for (Combination ladder : ladders) {

			cards = ladder.getCards();

			if (cards.size() >= 4) {

				c1 = cards.get(0);
				c2 = cards.get(cards.size() - 1);

				if (almostTripleValues.contains(c1.getValue())) {
					cards.remove(c1);
					remaining.add(c1);
					return;
				}

				if (almostTripleValues.contains(c2.getValue())) {
					cards.remove(c2);
					remaining.add(c2);
					return;
				}
			}
		}
	}

	public String combinatedCardsToString(List<Card> hand) {
		List<Combination> combinations = new ArrayList<>(obtainCombinations(hand));
		StringBuilder sb = new StringBuilder();

		for (Combination comb : combinations) {

			sb.append(String.format("%s -> ", comb.getType().getName()));
			
			sb.append(comb.getCards().stream().map(c -> c.toString()).collect(Collectors.joining(" ")));
		}

		return sb.toString();

	}

	public String nonCombinatedCardsToString(List<Card> hand) {

		List<Combination> combinations = new ArrayList<>(analyzer.obtainCombinations(hand));
		StringBuilder sb = new StringBuilder();
		List<Card> remaining = new ArrayList<>(hand);
		List<Card> combinationsCards = new ArrayList<>();
		List<Card> cards = new ArrayList<>();
		Card first, last;
		int index;

		combinationsCards = combinations.stream()
				.flatMap(c -> c.getCards().stream()).toList();

		remaining.removeAll(combinationsCards);

		if (!remaining.isEmpty()) {
			sb.append(String.format("===== Cartas sin combinar (para descarte) =====\n"));

			for (int i = 0; i < remaining.size(); i++) {
				sb.append(String.format("%d) %s\n", i + 1, remaining.get(i)));
			}

		} else {
			sb.append("===== Cartas para descarte =====\n");
			sb.append("Todas tus cartas actuales hacen combinaciones...\n");
			sb.append("Puedes romper una combinación larga (≥4) para descartar una carta:\n\n");

			index = 1;

			for (Combination comb : combinations) {

				if (comb.getCards().size() >= 4) {

					cards = comb.getCards();

					if (comb.getType() == CombinationType.LADDER) {

						sb.append(String.format(">> Escalera de tamaño %d:\n", cards.size()));

						first = cards.get(0);
						last = cards.get(cards.size() - 1);

						sb.append(String.format("\t%d) %s (primer carta)\n", index++, first));
						sb.append(String.format("\t%d) %s (última carta)\n", index++, last));

						sb.append("\n");

					} else if (comb.getType() == CombinationType.TRIPLE) {

						sb.append(String.format(">> Trío de tamaño %d:\n", cards.size()));

						for (Card c : cards) {
							sb.append(String.format("\t%d) %s\n", index++, c));
						}

						sb.append("\n");
					}
				}
			}
		}

		return sb.toString();
	}

}
