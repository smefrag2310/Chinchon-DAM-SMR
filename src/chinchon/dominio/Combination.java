package chinchon.dominio;

import java.util.List;

public class Combination {

	private CombinationType type;
	private List<Card> cards;

	public Combination(List<Card> cards, CombinationType type) {
		this.cards = cards;
		this.type = type;
	}
	
	public CombinationType getType() {
		return type; 
	}
	
	public List<Card> getCards(){
		return cards;
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb= new StringBuilder();
		
		for(Card c: cards) {
			sb.append(String.format("%s ", c.toString()));
		}
		
		return String.format("%s -> %s",sb.toString(),type);
	}
}
