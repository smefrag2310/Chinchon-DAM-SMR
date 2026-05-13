package chinchon.dominio;

import java.util.ArrayList;
import java.util.List;

import chinchon.app.Round;

/**
 * Base abstract class representing a player in the Chinchón game.
 * <p>
 * Both human and machine players extend this class. It provides shared
 * functionality such as:
 * <ul>
 *   <li>Managing the player's hand</li>
 *   <li>Tracking accumulated points</li>
 *   <li>Accessing the {@link CombinationAnalyzer}</li>
 *   <li>Determining which cards can be discarded</li>
 * </ul>
 * Subclasses must implement the {@link #decisionMaking(Round)} method to define
 * their specific behavior during a turn.
 */

public abstract class Player {

	protected String nickname;
	protected Hand hand;
	protected int points;
	protected CombinationAnalyzer analyzer;

	/**
     * Creates a new player with the given nickname.
     *
     * @param nickname the player's display name
     */
	
	public Player(String nickname) {
		this.nickname = nickname;
		this.hand = new Hand();
		points = 0;
		analyzer = CombinationAnalyzer.getInstance();
	}

	/**
     * Adds a card to the player's hand.
     *
     * @param card the card to add
     */
	
	public void addCardToHand(Card card) {
		hand.getCards().add(card);
	}

	/**
     * Returns a formatted string representation of the player's hand.
     *
     * @return the hand as a string
     */
	
	public String showCards() {
		return hand.toString();
	}
	
	/**
     * Defines the decision-making behavior for the player during their turn.
     * <p>
     * Human and machine players implement this method differently.
     *
     * @param round the current round context
     */

	public abstract void decisionMaking(Round round);
	
	 /**
     * Adds the given number of points to the player's total score.
     *
     * @param punctuation the points to add
     */
	
	public void addPoints(int punctuation) {
		points += punctuation;
	}
	
	/**
     * Determines which cards the player is allowed to discard.
     * <p>
     * Rules:
     * <ul>
     *   <li>If the player has non-combined cards → only those can be discarded</li>
     *   <li>If all cards form combinations → the player may break long combinations (size ≥ 4)</li>
     *   <li>For ladders → only the first or last card can be discarded</li>
     *   <li>For triples → any card in the triple can be discarded</li>
     * </ul>
     *
     * @return a list of cards the player is allowed to discard
     */

	public List<Card> getDiscardableCards() {

		List<Combination> combinations = analyzer.obtainCombinations(hand.getCards());
		List<Card> remaining = new ArrayList<>(hand.getCards());
		List<Card> combinationsCards = new ArrayList<>();
		List<Card> discardable = new ArrayList<>();

		combinationsCards = combinations.stream()
				.flatMap(c -> c.getCards().stream())
				.toList();

		remaining.removeAll(combinationsCards);

		if (!remaining.isEmpty()) {
			return remaining;
		}

		for (Combination comb : combinations) {

			if (comb.getCards().size() >= 4) {

				List<Card> cards = comb.getCards();

				if (comb.getType() == CombinationType.LADDER) {
					
					discardable.add(cards.get(0));
					discardable.add(cards.get(cards.size() - 1));
				} else {
					
					discardable.addAll(cards);
				}
			}
		}

		return discardable;
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
	
	/**
     * Returns a formatted string representation of the player, including
     * their nickname and current score.
     *
     * @return a string describing the player
     */

	@Override
	public String toString() {
		return String.format("%s: %s, Puntuación actual: %d", nickname, points);
	}
}
