package chinchon.app;

import java.util.List;

import chinchon.dominio.Card;
import chinchon.dominio.Combination;
import chinchon.dominio.CombinationAnalyzer;
import chinchon.dominio.CombinationType;
import chinchon.dominio.Deck;
import chinchon.dominio.DiscardPile;
import chinchon.dominio.Player;

/**
 * Represents a single round within a Chinchón game.
 * <p>
 * A round manages card distribution, player turns, deck refilling, scoring,
 * and detection of special winning conditions such as Chinchon. It interacts
 * closely with the {@link GameManager}, the deck, the discard pile, and all
 * participating players.
 */

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
	
	/**
     * Creates a new round with the given deck, discard pile, players, and score limit.
     *
     * @param deck       the deck used during the round
     * @param discardPile the discard pile shared across the game
     * @param players    the list of players participating in the round
     * @param maxScore   the maximum score allowed before the game ends
     */

	public Round(Deck deck, DiscardPile discardPile, List<Player> players, int maxScore) {
		this.deck = deck;
		this.discardPile = discardPile;
		this.players = players;
		this.maxScore = maxScore;
		console = ConsoleInput.getInstance();
		analyzer = CombinationAnalyzer.getInstance();
		number++;
	}
	
	 /**
     * Draws a card from the deck. If the deck is empty, it is refilled using
     * the discard pile before drawing.
     *
     * @return the drawn card
     */

	public Card drawDeckCard() {
		if (deck.getCards().isEmpty()) {
			console.writeLine("No quedan cartas en el mazo, se rellenará con la pila de descarte");
			refillDeckFromDiscard();
		}
		return deck.removeCard();
	}
	
	/**
     * Draws the top card from the discard pile. If the discard pile is empty,
     * a card is drawn from the deck instead.
     *
     * @return the drawn card
     */

	public Card drawDiscardPileCard() {
		if (discardPile.getCards().isEmpty()) {
			console.writeLine("No hay cartas en la pila de descarte, se robará del mazo en su lugar");
			return drawDeckCard();
		}
		return discardPile.removeCard();
	}
	
	/**
     * Places a card onto the discard pile.
     *
     * @param card the card to discard
     */

	public void discardCard(Card card) {
		discardPile.addCard(card);
	}
	
	 /**
     * Starts the round by distributing cards and executing player turns.
     */

	public void start() {
		initialDistribution();
		playersTurn();
	}
	
	/**
     * Deals 7 cards to each player and places one card into the discard pile
     * to begin the round.
     */

	public void initialDistribution() {
		for (Player p : players) {
			for (int i = 0; i < 7; i++) {
				p.addCardToHand(deck.removeCard());
			}
		}
		discardPile.addCard(deck.removeCard());
	}
	
	 /**
     * Executes the turn sequence for all players until the round ends or the game ends.
     * <p>
     * Each loop iteration represents a full turn cycle across all players.
     */

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
	
	/**
     * Executes the decision-making logic for a single player, unless the round
     * or game has already ended.
     *
     * @param player the player whose turn is being processed
     */

	public void playerManagement(Player player) {
		if (!roundEnd && !gameEnd) {
			player.decisionMaking(this);
		}
	}
	
	 /**
     * Refills the deck using the discard pile when the deck becomes empty.
     * The discard pile is shuffled before reuse.
     */

	public void refillDeckFromDiscard() {
		if (deck.getCards().isEmpty()) {
			deck.getCards().addAll(discardPile.clearAndReturn());
			deck.shuffle();
		}
	}
	
	/**
     * Resets the hand of every player, typically used when preparing for a new round.
     */

	public void resetPlayersHand() {
		for (Player p : players) {
			p.getHand().resetHand();
		}
	}
	
	/**
     * Ends the round by calculating and displaying the points earned by each player.
     * <p>
     * Points are computed using the {@link CombinationAnalyzer}, and total scores
     * are updated accordingly.
     */

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
	
	/**
     * Checks whether a given score is within the allowed maximum score.
     *
     * @param score the score to validate
     * @return {@code true} if the score is ≤ maxScore, otherwise {@code false}
     */

	public boolean checkScore(int score) {
		return score <= maxScore;
	}
	
	  /**
     * Returns a string representation of the last card in the discard pile.
     *
     * @return the last discarded card as a string
     */

	public String seeLastCardDiscardPile() {
		return String.format("%s", discardPile.getCards().get(discardPile.getCards().size() - 1).toString());
	}
	
	/**
     * Returns the last card from the discard pile without removing it.
     *
     * @return the top card of the discard pile
     */

	public Card peekLastFromDiscard() {
		return discardPile.getLastCard();
	}
	
	/**
     * Returns a string describing the last card drawn from the deck.
     *
     * @return a formatted message containing the drawn card
     */

	public String peekLastFromDeck() {
		return String.format("Se ha robado la siguiente carta: %s", deck.peekLastCard());
	}
	
	 /**
     * Checks whether the given player has achieved a Chinchon combination.
     * <p>
     * If a Chinchon is detected:
     * <ul>
     *   <li>The round ends immediately</li>
     *   <li>The game ends</li>
     *   <li>The player is declared the winner</li>
     * </ul>
     *
     * @param player the player to evaluate
     */

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
