package chinchon.dominio;

import java.util.ArrayList;
import java.util.List;

import chinchon.app.Round;

public abstract class Player {

	protected String nickname;
	protected Hand hand;
	protected int points;
	protected CombinationAnalyzer analyzer;

	public Player(String nickname) {
		this.nickname = nickname;
		this.hand = new Hand();
		points = 0;
		analyzer = CombinationAnalyzer.getInstance();
	}

	public void addCardToHand(Card card) {
		hand.getCards().add(card);
	}

	public String showCards() {
		return hand.toString();
	}
	
	public String combinatedCardsToString() {
		List<Combination> combinations = new ArrayList<>(analyzer.obtainCombinations(hand.getCards()));
		StringBuilder sb= new StringBuilder();
		
		for(CombinationType type: CombinationType.values()) {
			
		}
		
	}
	
	public String nonCombinatedCardsToString() {
		
	}

	public abstract void decisionMaking(Round round);
	
	public void addPoints(int punctuation) {
		points += punctuation;
	}

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
