package chinchon.app;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import chinchon.dominio.Card;
import chinchon.dominio.Deck;
import chinchon.dominio.Difficulty;
import chinchon.dominio.DiscardPile;
import chinchon.dominio.HumanPlayer;
import chinchon.dominio.Player;
import chinchon.dominio.PlayerFactory;

public class GameManager {

	private List<Player> players;
	private Deck deck;
	private DiscardPile discardPile;
	private int maxScore;
	private ConsoleInput console;
	private Round round;
	private boolean gameEnd;

	public GameManager(int maxScore) {
		this.players = new ArrayList<>();
		this.maxScore = maxScore;
		console = ConsoleInput.getInstance();
		discardPile = new DiscardPile();
	}

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

	public void startGame() {
		System.out.println("===== Ha empezado un nuevo juego =====");
		Round.setNumber(1);
		startRound();
		announceWinner();
	}

	public boolean gameEndByPoints() {
		return players.stream().anyMatch(p -> p.getPoints() >= maxScore);
	}

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

	public void resetNextRound() {
		deck.resetDeck();
		discardPile.clear();
		round.setRoundEnd(false);
		round.resetPlayersHand();
		round = new Round(deck, discardPile, players, maxScore);
	}

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
