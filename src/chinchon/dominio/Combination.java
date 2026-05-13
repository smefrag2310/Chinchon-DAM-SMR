package chinchon.dominio;

import java.util.List;

/**
 * Represents a detected card combination in the Chinchón game.
 * <p>
 * A combination groups together a set of cards that form a valid structure
 * according to the game rules, such as:
 * <ul>
 *   <li>Runs (ladders)</li>
 *   <li>Sets (three or four of a kind)</li>
 *   <li>Chinchon (a perfect run of seven cards)</li>
 * </ul>
 * Each combination is associated with a {@link CombinationType} that identifies
 * its nature and is used for scoring and game logic.
 */

public class Combination {

	private CombinationType type;
	private List<Card> cards;
	
	/**
     * Creates a new combination with the specified list of cards and type.
     *
     * @param cards the cards that form the combination
     * @param type  the type of combination (e.g., LADDER, TRIPLE, CHINCHON)
     */

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
	
	/**
     * Returns a formatted string representation of the combination.
     * <p>
     * Example:
     * <pre>
     *  LADDER -> 3⚔️ 4⚔️ 5⚔️
     * </pre>
     *
     * @return a string describing the cards and their combination type
     */
	
	@Override
	public String toString() {
		
		StringBuilder sb= new StringBuilder();
		
		for(Card c: cards) {
			sb.append(String.format("%s ", c.toString()));
		}
		
		return String.format("%s -> %s",type.getName(),sb.toString());
	}
}
