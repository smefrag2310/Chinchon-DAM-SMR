package chinchon.dominio;

/**
 * Represents the different types of valid card combinations in the Chinchon game.
 * <p>
 * Each combination type corresponds to a specific structure detected in a
 * player's hand, and is used for scoring, game logic, and display purposes.
 * The enum also stores a human-readable name for UI or console output.
 */

public enum CombinationType {
	
	/**
     * A sequence of at least three consecutive cards of the same suit.
     */
	LADDER("Escalera"),
	
	/**
     * A set of three cards sharing the same numeric value.
     */
	
	TRIPLE("Triple"),
	
	/**
     * A perfect seven‑card ladder (run), which immediately wins the game.
     */
	
	CHINCHON("Chinchón");
	
	private String name; 
	
	/**
     * Creates a new combination type with the specified display name.
     *
     * @param name the human-readable name of the combination
     */
	
	CombinationType(String name){
		this.name=name;
	}
	
	public String getName() {
		return name; 
	}
}
