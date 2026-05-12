package chinchon.app;

import java.util.List;

import chinchon.dominio.Card;
import chinchon.dominio.Deck;
import chinchon.dominio.DiscardPile;
import chinchon.dominio.Player;

public class Round {

	private Deck deck;
	private DiscardPile discardPile;
	private List<Player> players;
	private int maxScore;
	private ConsoleInput console;
	private static int number=0;
	private boolean roundEnd;
	private int turn=1;
	
	public Round(Deck deck,DiscardPile discardPile,List<Player> players,int maxScore) {
		this.deck=deck;
		this.discardPile=discardPile;
		this.players=players;
		this.maxScore= maxScore;
		console= ConsoleInput.getInstance();
		number++;
	}
	
	public Card drawDeckCard() {
		if(deck.getCards().isEmpty()) {
			console.writeLine("No quedan cartas en el mazo, se rellenará con la pila de descarte");
			refillDeckFromDiscard();
		}
		System.out.printf("Se ha robado la siguiente carta: %s\n",deck.peekLastCard());
		return deck.removeCard();
	}
	
	public Card drawDiscardPileCard() {
		if(discardPile.getCards().isEmpty()) {
			console.writeLine("No hay cartas en la pila de descarte, se robará del mazo en su lugar");
			return drawDeckCard();
		}
		return discardPile.removeCard();
	}
	
	public void discardCard(Card card) {
		discardPile.addCard(card);
	}
	
	public void roundEnd() {
		
		StringBuilder sb = new StringBuilder();
		
		for(Player player: players) {
			sb.append(String.format("\t%s\t|",player.getNickname()));
		}
		sb.append("\n");
		for(Player player: players) {
			sb.append(String.format("\t%d\t|",player.calculatePoints()));
		}
		sb.append("\n");
		for(Player player: players) {
			sb.append(String.format("\t%d\t|",player.getPoints()));
		}
		sb.append("\n");
		
		console.writeLine(sb.toString());
		return;
	}
	
	public void refillDeckFromDiscard() {
		if(deck.getCards().isEmpty()) {
			deck.getCards().addAll(discardPile.clearAndReturn());
			deck.shuffle();
		}
	}
	
	public void playerManagement(Player player) {
		if(!roundEnd) {
		player.decisionMaking(this);
		}else {
			roundEnd();
		}
	}
	
	public String seeLastCard() {
		if(discardPile.getCards().isEmpty()) {
			return "Vacío";
		}
		return discardPile.getLastCard().toString();
	}
	
	public void start() {
		initialDistribution();
		playersTurn();
	}
	
	public void initialDistribution() {
		for(Player p: players) {
			for(int i= 0; i<7;i++) {
				p.addCardToHand(deck.removeCard());
			}
		}
		discardPile.addCard(deck.removeCard());
	}
	
	public void playersTurn() {
		
		do {
			System.out.printf("===== Turno %d =====\n",turn);
		for(Player p: players) {
			playerManagement(p);
		}
		setTurn(getTurn() + 1);
		}while(!isRoundEnd());
	}
	
	public boolean checkScore(int score) {
		return score <= maxScore;
	}

	public int getNumber() {
		return number;
	}
	
	public static void setNumber(int number) {
		Round.number= number;
	}
	
	public String seeLastCardDiscardPile() {
		return String.format("%s",discardPile.getCards().get(discardPile.getCards().size()-1).toString());
	}
	
	public Card peekLastFromDiscard() {
		return discardPile.getLastCard();
	}
	
	public void resetPlayersHand() {
		for(Player p: players) {
			p.getHand().resetHand();
		}
	}
	
	public List<Card> getAllPossibleCards(){
		return deck.obtainAllCards();
	}

	public boolean isRoundEnd() {
		return roundEnd;
	}

	public void setRoundEnd(boolean roundEnd) {
		this.roundEnd = roundEnd;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}
	
}
