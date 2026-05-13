package chinchon.app;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import chinchon.dominio.Deck;
import chinchon.dominio.Difficulty;
import chinchon.dominio.DiscardPile;
import chinchon.dominio.Player;
import chinchon.dominio.PlayerFactory;

/**
 * Manages the overall flow of a Chinchon game, including player configuration,
 * deck setup, round progression, and determining the final winner.
 * 
 * This class acts as the central controller of the game lifecycle. It handles
 * user input for configuration, initializes rounds, checks end-game conditions,
 * and announces the winner based on either Chinchon or lowest score.
 */

public class GameManager {

	private List<Player> players;
	private Deck deck;
	private DiscardPile discardPile;
	private int maxScore;
	private ConsoleInput console;
	private Round round;
	private boolean gameEnd;
	
	/**
     * Creates a new {@code GameManager} with the specified maximum score.
     *
     * @param maxScore the score threshold that ends the game
     */

	public GameManager(int maxScore) {
		this.players = new ArrayList<>();
		this.maxScore = maxScore;
		console = ConsoleInput.getInstance();
		discardPile = new DiscardPile();
	}
	
	/**
     * Configures the list of players participating in the game.
     * <p>
     * The user selects:
     * <ul>
     *   <li>Total number of players (2–5)</li>
     *   <li>Number of human players</li>
     *   <li>Names for human players</li>
     *   <li>Difficulty level for machine players</li>
     * </ul>
     * Machine players are automatically named and assigned a difficulty.
     */

	public void configurePlayers() {

		String name;
		int numHuman, numMachine, numPlayers, diff;

		System.out.println("Introduce el número de jugadores totales (2-5): ");
		numPlayers = console.readIntInRange(2, 5);

		System.out.printf("Introduce el número de jugadores humanos(0-%d): \n", numPlayers);
		numHuman = console.readIntInRange(0, numPlayers);
		numMachine = numPlayers - numHuman;

		for (int i = 0; i < numHuman; i++) {
			System.out.printf("Introduce el nombre del jugador %d:\n", i + 1);
			name = console.readString();
			players.add(PlayerFactory.createHumanPlayer(name));
		}

		for (int i = 0; i < numMachine; i++) {
			name = String.format("Máquina %d", i+1);
			System.out.printf("Elige la dificultad de %s: \n1)Fácil\n2)Medio\n",name);
			diff = console.readIntInRange(1, 2);
			if (diff == 1) {
				players.add(PlayerFactory.createMachinePlayer(name, Difficulty.EASY));
			} else{
				players.add(PlayerFactory.createMachinePlayer(name, Difficulty.MEDIUM));
			} 
		}
	}

	/**
     * Configures the number of decks used in the game and initializes a new
     * {@link Round} instance.
     * <p>
     * The user chooses whether to play with 1 or 2 decks. The discard pile is
     * cleared before creating the new round.
     */
	
	public void configureDecksAndRound() {
		int numDecks;
		System.out.printf("Introduce el nº de barajas con las que quieres jugar(1 o 2): \n");
		numDecks = console.readIntTwoOptions(1, 2);
		if (numDecks == 1) {
			deck = new Deck(1);
		} else {
			deck = new Deck(2);
		}
		discardPile.clear();
		round = new Round(deck, discardPile, players, maxScore);
	}
	
	 /**
     * Starts the game, initializes the first round, and continues until the
     * game-ending condition is met. Once the game ends, the winner is announced.
     */

	public void startGame() {
		System.out.println("===== Ha empezado un nuevo juego =====");
		Round.setNumber(1);
		startRound();
		announceWinner();
	}
	
	/**
     * Checks whether any player has reached or exceeded the maximum score.
     *
     * @return {@code true} if the game should end due to points, otherwise {@code false}
     */

	public boolean gameEndByPoints() {
		return players.stream().anyMatch(p -> p.getPoints() >= maxScore);
	}
	
	/**
     * Executes the game loop, playing rounds until a game-ending condition occurs.
     * <p>
     * The loop ends when:
     * <ul>
     *   <li>A player wins by Chinchon</li>
     *   <li>A player reaches the maximum score</li>
     * </ul>
     */

	public void startRound() {
		while (!gameEnd) {
			System.out.printf("\n===== Ronda nº %d =====\n", round.getNumber());
			round.start();
			
			if(round.isGameEnd()) {
				gameEnd=true;
			}
			
			if(gameEndByPoints()) {
				gameEnd=true;
			}
			
			resetNextRound();
		}
	}
	
	/**
     * Resets the game state for the next round by:
     * <ul>
     *   <li>Resetting the deck</li>
     *   <li>Clearing the discard pile</li>
     *   <li>Resetting players' hands</li>
     *   <li>Creating a new {@link Round} instance</li>
     * </ul>
     */

	public void resetNextRound() {
		deck.resetDeck();
		discardPile.clear();
		round.setRoundEnd(false);
		round.resetPlayersHand();
		round = new Round(deck, discardPile, players, maxScore);
	}
	
	/**
     * Determines and announces the winner of the game.
     * <p>
     * The winner is decided by:
     * <ul>
     *   <li>Immediate victory by Chinchon</li>
     *   <li>Lowest score among all players (if no Chinchon occurred)</li>
     * </ul>
     */

	public void announceWinner() {
		
		Player winnerChinchon;
		Optional<Player> winnerPoints;

		 if (round.getWinner() != null) {
	            winnerChinchon = round.getWinner();
	            System.out.printf("\n¡%s ha ganado la partida por CHINCHÓN!\n", winnerChinchon.getNickname());
	            return;
	        }

	        winnerPoints = players.stream()
	                .collect(Collectors.minBy(Comparator.comparing(p -> p.getPoints())));

	        if (winnerPoints.isPresent()) {
	            System.out.printf("¡%s ha ganado la partida con %d puntos!\n",
	                    winnerPoints.get().getNickname(),
	                    winnerPoints.get().getPoints());
	        }
	}

	public List<Player> getPlayers() {
		return players;
	}

	public int getMaxScore() {
		return maxScore;
	}

	public Deck getDeck() {
		return deck;
	}

	public DiscardPile getDiscardPile() {
		return discardPile;
	}

	public Round getRound() {
		return round;
	}
	
	public void setGameEnd(boolean gameEnd) {
		this.gameEnd = gameEnd;
	}

}
