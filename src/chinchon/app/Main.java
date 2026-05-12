package chinchon.app;

public class Main {
	
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
