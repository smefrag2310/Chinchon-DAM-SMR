package chinchon.dominio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a player's hand in the Chinchon game.
 * <p>
 * A hand stores the cards held by a player and provides utility methods for
 * organizing, displaying, and modifying those cards. The hand does not enforce
 * game rules directly; instead, it serves as a container used by players,
 * the {@link CombinationAnalyzer}, and the game flow.
 */

public class Hand {

	private List<Card> cards;
	
	/**
     * Creates an empty hand.
     */

	public Hand() {
		this.cards = new ArrayList<>();
	}
	
	 /**
     * Removes the specified card from the hand.
     * <p>
     * If the card is not present, nothing happens.
     *
     * @param card the card to remove
     */

	public void removeCard(Card card) {
		cards.remove(card);
	}
	
	/**
     * Clears all cards from the hand.
     */

	public void resetHand() {
		cards.clear();
	}
	
	/**
     * Sorts the hand by suit first, then by value.
     * <p>
     * This method is used to display the hand in a clean and predictable order.
     */
	
	public void organizeHand() {
		cards.sort(Comparator.comparing(Card::getSuit)
				.thenComparing(Card::getValue));
	}
	
	 /**
     * Returns a formatted string where each card is enumerated with an index.
     * <p>
     * Example:
     * <pre>
     * 1) 3🪙
     * 2) 7🍷
     * 3) 12⚔️
     * </pre>
     *
     * @return a numbered list of cards in the hand
     */

	public String enumeratedCards() {
		StringBuilder sb = new StringBuilder();
		
		organizeHand();

		for (int i = 0; i < cards.size(); i++) {
			sb.append(String.format("%d) %s\n", i + 1, cards.get(i)));
		}
		return sb.toString();
	}
	
	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	 /**
     * Returns a string representation of the hand, with cards sorted and
     * displayed in a single line.
     *
     * @return a formatted string of all cards in the hand
     */

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
