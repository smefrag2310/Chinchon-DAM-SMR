package chinchon.dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

	private final List<Card> cards;
	private int decks;

	public Deck(int decks) {
		cards = new ArrayList<>();
		this.decks=decks;
		createCards(decks);
	}

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

	public void shuffle() {
		Collections.shuffle(cards);
	}
	
	public String peekLastCard() {
		if(cards.isEmpty()) {
			throw new IllegalStateException("No quedan cartas en el mazo");
		}
		return cards.get(cards.size()-1).toString();
	}

	public Card removeCard() {
		if (cards.isEmpty()) {
            throw new IllegalStateException("No quedan cartas en el mazo");
        }
		return cards.remove(cards.size() - 1);
	}

	public void resetDeck() {
		cards.clear();
		createCards(decks);
	}
	
	public List<Card> obtainAllCards(){
		
		List<Card> list= new ArrayList<>();
		
		for (int i = 1; i <= 1; i++) {
			for (Suit suit : Suit.values()) {
				for (Value value : Value.values()) {
					list.add(new Card(suit,value));
				}
			}
		}
		return list;
	}
	
	public List<Card> getCards(){
		return cards;
	}
	
}
