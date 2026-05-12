package chinchon.dominio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Hand {

	private List<Card> cards;

	public Hand() {
		this.cards = new ArrayList<>();
	}

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	public void removeCard(Card card) {
		cards.remove(card);
	}

	public void resetHand() {
		cards.clear();
	}
	
	public void organizeHand() {
		cards.sort(Comparator.comparing(Card::getSuit)
				.thenComparing(Card::getValue));
	}

	public String enumeratedCards() {
		StringBuilder sb = new StringBuilder();
		
		organizeHand();

		for (int i = 0; i < cards.size(); i++) {
			sb.append(String.format("%d) %s\n", i + 1, cards.get(i)));
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		organizeHand();

		for (Card card : cards) {
			sb.append(String.format("%s ", card.toString()));
		}
		return sb.toString();
	}
}
