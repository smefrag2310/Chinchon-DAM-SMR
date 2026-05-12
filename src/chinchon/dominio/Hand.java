package chinchon.dominio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

	public String enumeratedCards() {
		StringBuilder sb = new StringBuilder();
		List<Card> list = new ArrayList<>();

		list = cards.stream()
				.sorted(Comparator.comparing(Card::getSuit)
				.thenComparing(Card::getValue))
				.toList();

		for (int i = 0; i < list.size(); i++) {
			sb.append(String.format("%d) %s\n", i + 1, list.get(i)));
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		List<Card> list = new ArrayList<>();

		list = cards.stream()
				.sorted(Comparator.comparing(Card::getSuit)
				.thenComparing(Card::getValue))
				.toList();

		for (Card card : list) {
			sb.append(String.format("%s ", card.toString()));
		}
		return sb.toString();
	}
}
