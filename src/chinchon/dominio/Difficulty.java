package chinchon.dominio;

/**
 * Represents the difficulty levels available for machine-controlled players
 * in the Chinchon game.
 * <p>
 * The difficulty level influences the decision-making logic of AI players,
 * affecting how they evaluate combinations, discards, and strategic choices.
 */

public enum Difficulty {
	
	/**
     * Easy difficulty.
     * <p>
     * AI players make random decisions, incapable of making any combination, 
     * if they do it, it is purely by luck
	*/
	EASY,
	
	
	/**
     * Medium difficulty.
     * <p>
     * AI players use more advanced logic, evaluating combinations, protected
     * cards, and potential future plays with greater accuracy, plays almost 
     * like a human, this is the base difficulty and the recommended to play.
     */
	MEDIUM;
}
