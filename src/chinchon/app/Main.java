package chinchon.app;

/**
 * Entry point of the Chinchon application.
 * <p>
 * This class is responsible for initializing the game environment and
 * delegating control to the {@link GameManager}. It handles the initial
 * configuration steps such as reading the maximum score and launching the
 * game setup sequence.
 */

public class Main {
	
	/**
     * Displays the initial configuration menu and starts the game.
     * <p>
     * This method:
     * <ul>
     *   <li>Obtains the maximum score from the user</li>
     *   <li>Creates a {@link GameManager} instance</li>
     *   <li>Configures players</li>
     *   <li>Configures the deck(s) and round</li>
     *   <li>Starts the game loop</li>
     * </ul>
     */
	
	public void show() {
		
		ConsoleInput console= ConsoleInput.getInstance();
		GameManager manager;
		
		System.out.println("Introduce la puntuación máxima de la partida(50-300): ");
		manager= new GameManager(console.readIntInRange(50, 300));
		
		
			manager.configurePlayers();
			manager.configureDecksAndRound();;
			manager.startGame();
		
		
	}

	public static void main(String[] args) {
		new Main().show();
	}

}
