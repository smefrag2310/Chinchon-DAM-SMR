package chinchon.dominio;

import java.util.Objects;

public class Card {

	private final Suit suit;
	private final Value value;
	
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

	@Override
	public String toString() {
		return String.format("%d%s",value.getValue(),suit.getSymbol());
	}

	@Override
	public int hashCode() {
		return Objects.hash(suit, value);
	}

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
	
	public int compareTo(Card o) {
		int suitCompare= this.suit.compareTo(o.suit);
		if(suitCompare != 0) {
			return suitCompare;
		}
		return Integer.compare(this.value.getValue(),o.value.getValue());
	}
	
}
