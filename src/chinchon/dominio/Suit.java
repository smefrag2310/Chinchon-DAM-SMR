package chinchon.dominio;

/**
 * Represents the four suits of the Spanish deck used in the Chinchón game.
 * <p>
 * The suits are translated into their English equivalents:
 * <ul>
 *   <li>COINS  → Oros</li>
 *   <li>CUPS   → Copas</li>
 *   <li>SWORDS → Espadas</li>
 *   <li>CLUBS  → Bastos</li>
 * </ul>
 * Each suit also stores a Unicode symbol used for display purposes.
 */

public enum Suit {
	COINS("🪙"),CUPS("🍷"),SWORDS("⚔️"),CLUBS("🦯");
	
	private String symbol;
	
	 /**
     * Creates a suit with the given display symbol.
     *
     * @param symbol the Unicode symbol representing the suit
     */
	
	Suit(String symbol) {
		this.symbol=symbol;
	}

	public String getSymbol() {
		return symbol;
	}
}
