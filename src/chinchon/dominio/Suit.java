package chinchon.dominio;

public enum Suit {
	OROS("🪙"),COPAS("🍷"),ESPADAS("⚔️"),BASTOS("🦯");
	
	private String symbol;
	
	Suit(String symbol) {
		this.symbol=symbol;
	}

	public String getSymbol() {
		return symbol;
	}
	

}
