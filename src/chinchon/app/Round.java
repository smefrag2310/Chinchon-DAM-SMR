package chinchon.app;

import java.util.List;

import chinchon.dominio.Card;
import chinchon.dominio.Combination;
import chinchon.dominio.CombinationAnalyzer;
import chinchon.dominio.CombinationType;
import chinchon.dominio.Deck;
import chinchon.dominio.DiscardPile;
import chinchon.dominio.Player;

public class Round {

	private Deck deck;
	private DiscardPile discardPile;
	private List<Player> players;
	private int maxScore;
	private ConsoleInput console;
	private static int number = 0;
	private boolean roundEnd;
	private int turn = 1;
	private CombinationAnalyzer analyzer;
	private boolean gameEnd;
	private Player winner = null;

	public Round(Deck deck, DiscardPile discardPile, List<Player> players, int maxScore) {
		this.deck = deck;
		this.discardPile = discardPile;
		this.players = players;
		this.maxScore = maxScore;
		console = ConsoleInput.getInstance();
		analyzer = CombinationAnalyzer.getInstance();
		number++;
	}

	public Card drawDeckCard() {
		if (deck.getCards().isEmpty()) {
			console.writeLine("No quedan cartas en el mazo, se rellenará con la pila de descarte");
			refillDeckFromDiscard();
		}
		return deck.removeCard();
	}

	public Card drawDiscardPileCard() {
		if (discardPile.getCards().isEmpty()) {
			console.writeLine("No hay cartas en la pila de descarte, se robará del mazo en su lugar");
			return drawDeckCard();
		}
		return discardPile.removeCard();
	}

	public void discardCard(Card card) {
		discardPile.addCard(card);
	}

	public void start() {
		initialDistribution();
		playersTurn();
	}

	public void initialDistribution() {
		for (Player p : players) {
			for (int i = 0; i < 7; i++) {
				p.addCardToHand(deck.removeCard());
			}
		}
		discardPile.addCard(deck.removeCard());
	}

	public void playersTurn() {

		do {
			System.out.printf("===== Turno %d =====\n", turn);
			for (Player p : players) {
				playerManagement(p);
			}
			turn++;
		} while (!isRoundEnd() && !isGameEnd());

		if (!isGameEnd()) {
			roundEnd();
		}
	}

	public void playerManagement(Player player) {
		if (!roundEnd && !gameEnd) {
			player.decisionMaking(this);
		}
	}

	public void refillDeckFromDiscard() {
		if (deck.getCards().isEmpty()) {
			deck.getCards().addAll(discardPile.clearAndReturn());
			deck.shuffle();
		}
	}

	public void resetPlayersHand() {
		for (Player p : players) {
			p.getHand().resetHand();
		}
	}

	public void roundEnd() {

		StringBuilder sb = new StringBuilder();
		int roundPoints;

		sb.append("\n=========================================\n");
		sb.append("              FIN DE LA RONDA\n");
		sb.append("=========================================\n\n");

		sb.append(String.format("%-15s | %-12s | %-12s\n", "Jugador", "Puntos Ronda", "Puntos Totales"));
		sb.append("-----------------------------------------\n");

		for (Player player : players) {

			roundPoints = analyzer.calculatePoints(analyzer.obtainCombinations(player.getHand().getCards()),
					player.getHand().getCards());

			player.addPoints(roundPoints);

			sb.append(String.format("%-15s | %-12d | %-12d\n", player.getNickname(), roundPoints, player.getPoints()));
		}

		sb.append("\n=========================================\n");

		console.writeLine(sb.toString());
	}

	public boolean checkScore(int score) {
		return score <= maxScore;
	}

	public String seeLastCardDiscardPile() {
		return String.format("%s", discardPile.getCards().get(discardPile.getCards().size() - 1).toString());
	}

	public Card peekLastFromDiscard() {
		return discardPile.getLastCard();
	}

	public String peekLastFromDeck() {
		return String.format("Se ha robado la siguiente carta: %s", deck.peekLastCard());
	}

	public void checkForChinchon(Player player) {

		List<Combination> combinations = analyzer.obtainCombinations(player.getHand().getCards());
		boolean hasChinchon;

		hasChinchon = combinations.stream()
				.anyMatch(c -> c.getType() == CombinationType.CHINCHON);

		if (hasChinchon) {
			roundEnd = true;
			gameEnd = true;
			winner = player;
		}
	}

	public int getNumber() {
		return number;
	}

	public static void setNumber(int number) {
		Round.number = number;
	}

	public boolean isRoundEnd() {
		return roundEnd;
	}

	public void setRoundEnd(boolean roundEnd) {
		this.roundEnd = roundEnd;
	}

	public boolean isGameEnd() {
		return gameEnd;
	}

	public void setGameEnd(boolean gameEnd) {
		this.gameEnd = gameEnd;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public Player getWinner() {
		return winner;
	}

	public void setWinner(Player winner) {
		this.winner = winner;
	}

}
