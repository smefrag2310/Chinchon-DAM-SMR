package chinchon.dominio;

/**
 * Factory class responsible for creating instances of {@link Player}.
 * <p>
 * This class centralizes the creation logic for both human and machine players,
 * ensuring that the rest of the application does not depend on concrete
 * implementations such as {@link HumanPlayer} or {@link MachinePlayer}.
 * <p>
 * Using a factory improves maintainability and allows new player types to be
 * added without modifying the game flow.
 */

public class PlayerFactory {
	
	/**
     * Creates a new human player with the given name.
     *
     * @param name the nickname of the human player
     * @return a new {@link HumanPlayer} instance
     */

	public static Player createHumanPlayer(String name) {
		return new HumanPlayer(name);
	}
	
	/**
     * Creates a new machine player with the given name and difficulty level.
     *
     * @param name       the nickname of the machine player
     * @param difficulty the AI difficulty level
     * @return a new {@link MachinePlayer} instance
     */
	
	public static Player createMachinePlayer(String name,Difficulty difficulty) {
		return new MachinePlayer(name,difficulty);
	}
}
