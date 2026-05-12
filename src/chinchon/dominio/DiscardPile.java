package chinchon.dominio;

import java.util.ArrayList;
import java.util.List;

public class DiscardPile {

	private List<Card> cards;
	
	public DiscardPile() {
		cards= new ArrayList<>();
	}

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}
	
	public void addCard(Card card) {
		cards.add(card);
	}
	
	public Card removeCard() {
		if(cards.isEmpty()) {
			throw new IllegalStateException("La pila de descarte está vacía");
		}
		return cards.remove(cards.size()-1);
	}
	
	public void clear() {
		cards.clear();
	}
	
	public List<Card> clearAndReturn(){
		List<Card> discardCards = new ArrayList<>(cards);
		cards.clear();
		return discardCards;
	}
	
	public Card getLastCard() {
		if (cards.isEmpty()) {
			throw new IllegalStateException("La pila de descate está vacía");
		}
	    return cards.getLast();
	}
	
}
