package chinchon.dominio;

import java.util.Objects;

/**
 * Represents a single playing card used in the Chinchón game.
 * <p>
 * A card is defined by a {@link Suit} and a {@link Value}. Cards are immutable
 * and provide comparison, equality, and formatted string representation
 * functionalities. They are used throughout the game for hand management,
 * combination detection, and scoring.
 */

public class Card {

	private final Suit suit;
	private final Value value;
	
	 /**
     * Creates a new card with the specified suit and value.
     *
     * @param suit  the suit of the card (e.g., Coins, Swords)
     * @param value the value of the card (e.g., 1–12)
     */
	
	public Card(Suit suit,Value value) {
		this.suit=suit;
		this.value=value;
	}

	public Suit getSuit() {
		return suit;
	}

	public Value getValue() {
		return value;
	}
	
	 /**
     * Returns a compact string representation of the card.
     * <p>
     * Format example: {@code "5🪙"} or {@code "12🍷"}.
     *
     * @return a formatted string representing the card
     */

	@Override
	public String toString() {
		return String.format("%d%s",value.getValue(),suit.getSymbol());
	}
	
	/**
     * Computes the hash code for this card based on its suit and value.
     *
     * @return the hash code of this card
     */

	@Override
	public int hashCode() {
		return Objects.hash(suit, value);
	}
	
	 /**
     * Compares this card to another object for equality.
     * <p>
     * Two cards are considered equal if they have the same suit and value.
     *
     * @param obj the object to compare with
     * @return {@code true} if both cards are equal, otherwise {@code false}
     */

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		return suit == other.suit && value == other.value;
	}
	
	/**
     * Compares this card to another card for ordering.
     * <p>
     * Cards are first compared by suit (using the natural order of {@link Suit}),
     * and if the suits are equal, they are compared by their numeric value.
     *
     * @param o the card to compare with
     * @return a negative number, zero, or a positive number depending on the ordering
     */
	
	public int compareTo(Card o) {
		int suitCompare= this.suit.compareTo(o.suit);
		if(suitCompare != 0) {
			return suitCompare;
		}
		return Integer.compare(this.value.getValue(),o.value.getValue());
	}
	
}
