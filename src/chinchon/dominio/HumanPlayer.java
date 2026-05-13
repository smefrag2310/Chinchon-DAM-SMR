package chinchon.dominio;

import java.util.ArrayList;
import java.util.List;

import chinchon.app.ConsoleInput;
import chinchon.app.Round;

/**
 * Represents a human-controlled player in the Chinchon game.
 * <p>
 * This class handles all user interactions through the console, including:
 * <ul>
 *   <li>Choosing where to draw a card from</li>
 *   <li>Selecting a card to discard</li>
 *   <li>Deciding whether to end the round when allowed</li>
 * </ul>
 * Human players rely on {@link ConsoleInput} for input and output, and use
 * the shared {@link CombinationAnalyzer} logic inherited from {@link Player}.
 */

public class HumanPlayer extends Player {

	private ConsoleInput console;

	/**
     * Creates a new human player with the given name.
     *
     * @param name the player's nickname
     */
	
	public HumanPlayer(String name) {
		super(name);
		console = ConsoleInput.getInstance();
	}
	
	/**
     * Executes the decision-making process for a human player during their turn.
     * <p>
     * The process consists of:
     * <ol>
     *   <li>Displaying the player's current hand</li>
     *   <li>Drawing a card (deck or discard pile)</li>
     *   <li>Discarding a card or optionally ending the round</li>
     * </ol>
     *
     * @param round the current round context
     */

	@Override
	public void decisionMaking(Round round) {

		System.out.printf(">> Jugador actual: %s\n", nickname);
		System.out.printf(">> Cartas actuales: %s\n", hand.toString());
		drawACard(round);
		discardACardOrEnd(round);

	}
	
	/**
     * Allows the player to choose where to draw a card from:
     * <ul>
     *   <li>1 → Draw from the deck</li>
     *   <li>2 → Draw from the discard pile</li>
     * </ul>
     * After drawing, the method checks whether the player has achieved Chinchón.
     *
     * @param round the current round context
     */

	private void drawACard(Round round) {

		int option;

		console.writeLine("===== Robar =====");
		console.writeLine("1) Robar de la baraja");
		System.out.printf("2) Robar de la pila de descarte: %s\n", round.seeLastCardDiscardPile());
		option = console.readIntInRange(1, 2);

		if (option == 1) {
			console.writeLine(round.peekLastFromDeck());
			hand.getCards().add(round.drawDeckCard());
		} else {
			hand.getCards().add(round.drawDiscardPileCard());
		}
		
		round.checkForChinchon(this);
	}
	
	 /**
     * Allows the player to discard a card, and optionally end the round if allowed.
     * <p>
     * A player may end the round only if:
     * <ul>
     *   <li>They have at least 6 cards forming combinations</li>
     *   <li>It is not the first turn of the round</li>
     *   <li>The resulting score does not exceed the maximum allowed</li>
     * </ul>
     * The method handles two discard modes:
     * <ul>
     *   <li>Normal discard (choose any card)</li>
     *   <li>Strategic discard when ending the round (only discardable cards)</li>
     * </ul>
     *
     * @param round the current round context
     */

	private void discardACardOrEnd(Round round) {

		List<Card> discardableCards = new ArrayList<>(getDiscardableCards());
		int option, discardIndex;
		boolean canEndRound;
		Card card;

		canEndRound = analyzer.checkCombinations(hand.getCards()) && round.getTurn() != 1 && round.checkScore(
				analyzer.currentPoints(analyzer.obtainCombinations(hand.getCards()), hand.getCards(), points));

		console.writeLine("===== Descartar =====");
		console.writeLine("1) Descartar una carta");
		if (canEndRound) {
			console.writeLine("2) Descartar una carta y terminar la ronda");
		}

		option = console.readIntInRange(1, canEndRound ? 2 : 1);

		if (option == 1) {
			console.writeLine("===== Mano actual =====");
			console.writeLine(hand.enumeratedCards());
			console.writeLine("Escoje cual descartar: ");
			discardIndex = console.readIntInRange(1, hand.getCards().size());
			card = hand.getCards().get(discardIndex - 1);
		} else {
			console.writeLine(analyzer.combinatedCardsToString(hand.getCards()));
			console.writeLine(analyzer.nonCombinatedCardsToString(hand.getCards()));
			discardIndex = console.readIntInRange(1,discardableCards.size());
			card= discardableCards.get(discardIndex - 1);
		}

		System.out.printf("Se ha descartado la siguiente carta: %s\n", card);
		hand.removeCard(card);
		round.discardCard(card);
		round.checkForChinchon(this);

		if (option == 2 && canEndRound) {
			round.setRoundEnd(true);
		}

	}

}
