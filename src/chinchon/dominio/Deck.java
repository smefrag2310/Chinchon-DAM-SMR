package chinchon.dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents one or more Spanish decks of cards used in the Chinchon game.
 * <p>
 * A deck contains all 40 cards of the Spanish deck (or 80 if two decks are used).
 * Cards are created, shuffled, drawn, and reset through this class.
 * <p>
 * The deck behaves as a stack: cards are drawn from the end of the list.
 */

public class Deck {

	private final List<Card> cards;
	private int decks;

	 /**
     * Creates a new deck containing the specified number of Spanish decks.
     *
     * @param decks the number of decks to include (typically 1 or 2)
     */
	
	public Deck(int decks) {
		cards = new ArrayList<>();
		this.decks=decks;
		createCards(decks);
	}
	
	/**
     * Generates all cards for the given number of decks and shuffles them.
     *
     * @param decks the number of decks to generate
     */

	private void createCards(int decks) {

		for (int i = 1; i <= decks; i++) {
			for (Suit suit : Suit.values()) {
				for (Value value : Value.values()) {
					cards.add(new Card(suit, value));
				}
			}
		}
		shuffle();
	}
	
	/**
     * Randomly shuffles the deck.
     */

	public void shuffle() {
		Collections.shuffle(cards);
	}
	
	/**
     * Returns the last card in the deck without removing it.
     *
     * @return a string representation of the last card
     * @throws IllegalStateException if the deck is empty
     */
	
	public String peekLastCard() {
		if(cards.isEmpty()) {
			throw new IllegalStateException("No quedan cartas en el mazo");
		}
		return cards.get(cards.size()-1).toString();
	}
	
	/**
     * Removes and returns the last card from the deck.
     *
     * @return the drawn card
     * @throws IllegalStateException if the deck is empty
     */

	public Card removeCard() {
		if (cards.isEmpty()) {
            throw new IllegalStateException("No quedan cartas en el mazo");
        }
		return cards.remove(cards.size() - 1);
	}
	
	/**
     * Resets the deck to its original state by clearing all cards,
     * recreating them, and shuffling again.
     */

	public void resetDeck() {
		cards.clear();
		createCards(decks);
	}
	
	public List<Card> getCards(){
		return cards;
	}
	
}
