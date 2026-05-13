package chinchon.dominio;

import java.util.ArrayList;
import java.util.List;

import chinchon.app.Round;

public abstract class Player {

	protected String nickname;
	protected Hand hand;
	protected int points;
	protected CombinationAnalyzer analyzer;

	public Player(String nickname) {
		this.nickname = nickname;
		this.hand = new Hand();
		points = 0;
		analyzer = CombinationAnalyzer.getInstance();
	}

	public void addCardToHand(Card card) {
		hand.getCards().add(card);
	}

	public String showCards() {
		return hand.toString();
	}

	public abstract void decisionMaking(Round round);
	
	public void addPoints(int punctuation) {
		points += punctuation;
	}
	

	public List<Card> getDiscardableCards() {

		List<Combination> combinations = analyzer.obtainCombinations(hand.getCards());
		List<Card> remaining = new ArrayList<>(hand.getCards());
		List<Card> combinationsCards = new ArrayList<>();
		List<Card> discardable = new ArrayList<>();

		combinationsCards = combinations.stream()
				.flatMap(c -> c.getCards().stream())
				.toList();

		remaining.removeAll(combinationsCards);

		if (!remaining.isEmpty()) {
			return remaining;
		}

		for (Combination comb : combinations) {

			if (comb.getCards().size() >= 4) {

				List<Card> cards = comb.getCards();

				if (comb.getType() == CombinationType.LADDER) {
					
					discardable.add(cards.get(0));
					discardable.add(cards.get(cards.size() - 1));
				} else {
					
					discardable.addAll(cards);
				}
			}
		}

		return discardable;
	}

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
