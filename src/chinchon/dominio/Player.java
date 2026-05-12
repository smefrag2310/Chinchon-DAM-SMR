package chinchon.dominio;

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

	public abstract void decisionMaking(Round round);
	
	public void addPoints(int points) {
		this.points+=points;
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
