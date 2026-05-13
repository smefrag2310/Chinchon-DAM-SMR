package chinchon.dominio;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the discard pile used in the Chinchon game.
 * <p>
 * The discard pile stores cards that players have discarded during the round.
 * Cards are added to the end of the list, and the pile behaves like a stack:
 * the last card added is the first one available to be drawn.
 * <p>
 * This class also provides utility methods for clearing the pile, returning
 * its contents, and retrieving the top card.
 */

public class DiscardPile {

	private List<Card> cards;
	
	/**
     * Creates an empty discard pile.
     */
	
	public DiscardPile() {
		cards= new ArrayList<>();
	}
	
	/**
     * Adds a card to the top of the discard pile.
     *
     * @param card the card to add
     */
	
	public void addCard(Card card) {
		cards.add(card);
	}
	
	/**
     * Removes and returns the top card from the discard pile.
     *
     * @return the removed card
     * @throws IllegalStateException if the discard pile is empty
     */
	
	public Card removeCard() {
		if(cards.isEmpty()) {
			throw new IllegalStateException("La pila de descarte está vacía");
		}
		return cards.remove(cards.size()-1);
	}
	
	 /**
     * Removes all cards from the discard pile.
     */
	
	public void clear() {
		cards.clear();
	}
	
	/**
     * Removes all cards from the discard pile and returns them.
     * <p>
     * This is typically used when the deck needs to be refilled.
     *
     * @return a list containing all cards previously in the discard pile
     */
	
	public List<Card> clearAndReturn(){
		List<Card> discardCards = new ArrayList<>(cards);
		cards.clear();
		return discardCards;
	}
	
	/**
     * Returns the top card of the discard pile without removing it.
     *
     * @return the last card in the discard pile
     * @throws IllegalStateException if the discard pile is empty
     */
	
	public Card getLastCard() {
		if (cards.isEmpty()) {
			throw new IllegalStateException("La pila de descate está vacía");
		}
	    return cards.getLast();
	}
	
	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}
	
}
