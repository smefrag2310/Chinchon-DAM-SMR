package chinchon.dominio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import chinchon.app.Round;

public abstract class Player {

	protected String nickname;
	protected Hand hand;
	protected int points;

	public Player(String nickname) {
		this.nickname = nickname;
		this.hand = new Hand();
		points = 0;
	}

	public void addCardToHand(Card card) {
		hand.getCards().add(card);
	}

	public List<Combination> obtainCombinations() {

		List<Card> remaining = new ArrayList<>(hand.getCards());
		List<Combination> combinations = new ArrayList<>();
		List<Combination> ladder = new ArrayList<>();
		List<Combination> triple = new ArrayList<>();

		ladder = findLadderAndChinchon(remaining);
		for (Combination c : ladder) {
			combinations.add(c);
			remaining.removeAll(c.getCards());
		}

		// 2. Buscar tríos
		triple = findEqualNumber(remaining);
		for (Combination c : triple) {
			combinations.add(c);
			remaining.removeAll(c.getCards());
		}

		return combinations;
	}

	public boolean checkCombinations() {
		return obtainCombinations().size() >= 6;
	}

	public List<Combination> findLadderAndChinchon(List<Card> remaining) {

		List<Card> list = new ArrayList<>();
		List<Card> actual = new ArrayList<>();
		List<Combination> sequence = new ArrayList<>();
		Card last;

		for (Suit suit : Suit.values()) {

			list = remaining.stream().filter(c -> c.getSuit() == suit).sorted(Comparator.comparing(c -> c.getValue()))
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
							sequence.add(createLadderOrChinchon(actual));
						}
						actual.clear();
						actual.add(c);
					}
				}
			}
			if (actual.size() >= 3) {
				sequence.add(createLadderOrChinchon(actual));
			}
		}
		return sequence;
	}

	public List<Combination> findEqualNumber(List<Card> remaining) {

		List<Card> list = new ArrayList<>();
		List<Combination> sequence = new ArrayList<>();

		for (Value value : Value.values()) {

			list = remaining.stream().filter(c -> c.getValue() == value).toList();

			if (list.size() >= 3) {
				sequence.add(createTriples(list));
			}
		}
		return sequence;
	}
	
	public Combination createLadderOrChinchon(List<Card> list){
		if(list.size() == 7) {
			return new Combination(list,CombinationType.CHINCHON);
		}
		return new Combination(list,CombinationType.LADDER);
	}
	
	public Combination createTriples(List<Card> list) {
		return new Combination(list,CombinationType.TRIPLE);
	}

	public int calculatePoints(List<Combination> combinations) {

		List<Card> remainingCards = new ArrayList<>();
		Set<Card> combinationCards;
		int punctuation=0;
		
		combinationCards = combinations.stream()
							.flatMap(c -> c.getCards().stream())
							.collect(Collectors.toSet());
		
		remainingCards = hand.getCards().stream()
						.filter(c -> !combinationCards.contains(c))
						.toList();
		
		if(remainingCards.isEmpty()) {
			punctuation-=10;
		}else {
			punctuation = remainingCards.stream()
						 .mapToInt(c -> c.getValue().getValue())
						 .sum();
		}
		
		points+=punctuation;
		
		return points;
		
	}

	public int currentPoints(List<Combination> combinations) {

		List<Card> remainingCards = new ArrayList<>();
		Set<Card> combinationCards;
		int punctuation,current;
		
		current=points;
		punctuation=0;
		
		combinationCards = combinations.stream()
							.flatMap(c -> c.getCards().stream())
							.collect(Collectors.toSet());
		
		remainingCards = hand.getCards().stream()
						.filter(c -> !combinationCards.contains(c))
						.toList();
		
		if(remainingCards.isEmpty()) {
			punctuation-=10;
		}else {
			punctuation = remainingCards.stream()
						 .mapToInt(c -> c.getValue().getValue())
						 .sum();
		}
		
		
		return current + punctuation;
		
	}

	public String showCards() {
		return hand.toString();
	}

	public abstract void decisionMaking(Round round);

	public Hand getHand() {
		return hand;
	}

	public int getPoints() {
		return points;
	}

	public String getNickname() {
		return nickname;
	}

	@Override
	public String toString() {
		return String.format("%s: %s, Puntuación actual: %d", nickname, points);
	}
}
