package chinchon.dominio;

public class PlayerFactory {

	public static Player createHumanPlayer(String name) {
		return new HumanPlayer(name);
	}
	
	public static Player createMachinePlayer(String name,Difficulty difficulty) {
		return new MachinePlayer(name,difficulty);
	}
}
