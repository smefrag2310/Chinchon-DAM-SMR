package chinchon.dominio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import chinchon.app.Round;

public abstract class Player {

	protected String nickname;
	protected Hand hand;
	protected int points;

	public Player(String nickname) {
		this.nickname=nickname;
		this.hand = new Hand();
		points=0;
	}
	
	public void addCardToHand(Card card) {
		hand.getCards().add(card);
	}
	
	public List<Card> obtainCombinations() {
		
		List<Card> remaining= new ArrayList<>(hand.getCards());
		List<Card> combinations= new ArrayList<>();
		List<Card> ladder= new ArrayList<>();
		List<Card> triple= new ArrayList<>();
		
		ladder= findLadderAndChinchon(remaining);
		combinations.addAll(ladder);
		remaining.removeAll(ladder);
		
		triple= findEqualNumber(remaining);
		combinations.addAll(triple);
		remaining.removeAll(triple);
		
		return combinations;
	}

	public boolean checkCombinations() {
		return obtainCombinations().size() >= 6;
	}

	public List<Card> findLadderAndChinchon(List<Card> remaining) {
		
		List<Card> list = new ArrayList<>();
		List<Card> actual = new ArrayList<>();
		List<Card> sequence = new ArrayList<>();
		Card last;
		
		for (Suit suit : Suit.values()) {

			list = remaining.stream()
					.filter(c -> c.getSuit() == suit)
					.sorted(Comparator.comparing(c -> c.getValue()))
					.toList();
			
			actual.clear();
			
		for (Card c : list) {
			if (actual.isEmpty()) {
				actual.add(c);
			} else {
				last = actual.get(actual.size() - 1);

				if (c.getValue().getOrder() == last.getValue().getOrder() + 1) {
					actual.add(c);
				} else {

					if (actual.size() >= 3) {
						sequence.addAll(actual);
					}
					actual.clear();
					actual.add(c);
				}
			}
		}
		if (actual.size() >= 3) {
			sequence.addAll(actual);
		}
		}
		return sequence;
	}
	
	public List<Card> findEqualNumber(List<Card> remaining){
		
		List<Card> list= new ArrayList<>();
		List<Card> sequence = new ArrayList<>();
		
		for(Value value: Value.values()) {
			
		list= remaining.stream()
			 .filter(c -> c.getValue()==value)
			 .toList();
		
		 if (list.size() >= 3) {
	            sequence.addAll(list);
	        }
		}
		return sequence;
	}
	
	public int calculatePoints() {
		
		List<Card> remainingCards= new ArrayList<>(hand.getCards());
		int punctuation=0;
		
		remainingCards.removeAll(obtainCombinations());
		
		if(remainingCards.isEmpty()) {
			punctuation-=10;
		}else {
			for(Card card: remainingCards) {
				punctuation+=card.getValue().getValue();
			}
		}
		points+=punctuation;
		return punctuation;
	}
	
	public int currentPoints() {
		
		List<Card> remainingCards= new ArrayList<>(hand.getCards());
		int currentPoints = points;
		
		remainingCards.removeAll(obtainCombinations());
		
		if(remainingCards.isEmpty()) {
			currentPoints-=10;
		}else {
			for(Card card: remainingCards) {
				currentPoints+=card.getValue().getValue();
			}
		}
		return currentPoints;
	}
		

	public String showCards() {
		return hand.toString();
	}
	
	public abstract void decisionMaking(Round round);

	public Hand getHand() {
		return hand;
	}

	public int getPoints() {
		return points;
	}

	public String getNickname() {
		return nickname;
	}
	
	@Override
	public String toString() {
		return String.format("%s: %s, Puntuación actual: %d", nickname, points);
	}
}
