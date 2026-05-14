package chinchon.dominio;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import chinchon.app.Round;

/**
 * Represents a machine-controlled (AI) player in the Chinchón game.
 * <p>
 * The AI behavior varies depending on the selected {@link Difficulty}:
 * <ul>
 *   <li>{@code EASY}: random decisions with minimal strategy</li>
 *   <li>{@code MEDIUM}: strategic decisions based on combination analysis</li>
 * </ul>
 * The AI uses the shared {@link CombinationAnalyzer} inherited from {@link Player}
 * to evaluate combinations, protected cards, and optimal discards.
 */

public class MachinePlayer extends Player {

	private Difficulty difficulty;

	/**
     * Creates a new machine player with the given name and difficulty level.
     *
     * @param name       the player's nickname
     * @param difficulty the AI difficulty level
     */
	
	public MachinePlayer(String name, Difficulty difficulty) {
		super(name);
		this.difficulty = difficulty;
	}
	
	/**
     * Executes the decision-making process for the machine player.
     * <p>
     * Behavior depends on the difficulty:
     * <ul>
     *   <li>{@code EASY}: random draw and random discard</li>
     *   <li>{@code MEDIUM}: strategic draw and discard</li>
     * </ul>
     *
     * @param round the current round context
     */

	@Override
	public void decisionMaking(Round round) {
		if (difficulty == Difficulty.EASY) {
			aleatoryDecisions(round);
		} else {
			normalDecisions(round);
		}
	}
	
	 /**
     * AI logic for EASY difficulty.
     * <p>
     * Behavior:
     * <ul>
     *   <li>Randomly chooses between drawing from the deck or discard pile</li>
     *   <li>Randomly discards one card</li>
     *   <li>If allowed, may end the round</li>
     * </ul>
     *
     * @param round the current round context
     */

	public void aleatoryDecisions(Round round) {

		Random r = new Random();
		int rNumber;
		boolean canEndRound;

		rNumber = r.nextInt(2);
		if (rNumber == 0) {
			hand.getCards().add(round.drawDeckCard());
		} else if (rNumber == 1) {
			hand.getCards().add(round.drawDiscardPileCard());
		}
		
		canEndRound= analyzer.checkCombinations(hand.getCards()) && round.getTurn() != 1
				&& round.checkScore(analyzer.currentPoints(
						analyzer.obtainCombinations(hand.getCards()),
						hand.getCards(), points));

		rNumber = r.nextInt(hand.getCards().size());
		round.discardCard(hand.getCards().get(rNumber));
		hand.removeCard(hand.getCards().get(rNumber));

		if (canEndRound) {
			round.setRoundEnd(true);
		} 

	}
	
	/**
     * AI logic for MEDIUM difficulty.
     * <p>
     * Behavior:
     * <ul>
     *   <li>Displays current hand (for debugging/visibility)</li>
     *   <li>Strategically chooses where to draw a card from</li>
     *   <li>Strategically chooses the best card to discard</li>
     * </ul>
     *
     * @param round the current round context
     */

	public void normalDecisions(Round round) {

		System.out.printf(">> Jugador actual: %s\n", nickname);
		System.out.printf(">> Cartas actuales: %s\n", hand.toString());
		chooseWhereToPick(round);
		chooseCardToDiscard(round);

	}
	
	/**
     * Chooses whether to draw from the deck or the discard pile.
     * <p>
     * Strategy:
     * <ul>
     *   <li>If the top discard card helps complete a ladder or triple → pick it</li>
     *   <li>Otherwise → draw from the deck</li>
     * </ul>
     *
     * @param round the current round context
     */

	public void chooseWhereToPick(Round round) {

		List<Card> searchCards = new ArrayList<>(analyzer.cardsForSearch(hand.getCards()));

		for (Card c1 : searchCards) {
			if (c1.equals(round.peekLastFromDiscard())) {
				hand.getCards().add(round.drawDiscardPileCard());
				return;
			}
		}
		hand.getCards().add(round.drawDeckCard());
		round.checkForChinchon(this);
	}
	
	/**
     * Chooses the best card to discard based on protected and unprotected cards.
     * <p>
     * Strategy:
     * <ul>
     *   <li>Prefer discarding unprotected cards</li>
     *   <li>If all cards are protected, discard the least valuable protected card</li>
     *   <li>If ending the round is allowed, discard only from discardable cards</li>
     * </ul>
     *
     * @param round the current round context
     */

	public void chooseCardToDiscard(Round round) {

		List<Card> secureCards = new ArrayList<>(analyzer.protectedCards(hand.getCards()));
		List<Card> insecureCards = new ArrayList<>();
		List<Card> discardableCards = new ArrayList<>(getDiscardableCards());
		Card cardToDiscard;
		boolean canEndRound;
		
		

		canEndRound= analyzer.checkCombinations(hand.getCards()) && round.getTurn() != 1
				&& round.checkScore(analyzer.currentPoints(
						analyzer.obtainCombinations(hand.getCards()), hand.getCards(), points));

		insecureCards.addAll(hand.getCards());
		insecureCards.removeAll(secureCards);

		if(!canEndRound) {
		
		if (!insecureCards.isEmpty()) {
			cardToDiscard = insecureCards.get(insecureCards.size() - 1);
		} else if (!secureCards.isEmpty()) {
			cardToDiscard = secureCards.get(secureCards.size() - 1);
		} else {
			cardToDiscard = hand.getCards().get(0);
		}
		}else {
			cardToDiscard= discardableCards.get(0);
		}

		System.out.printf("Se ha descartado la siguiente carta: %s\n", cardToDiscard);
		round.discardCard(cardToDiscard);
		hand.removeCard(cardToDiscard);
		round.checkForChinchon(this);

		if (canEndRound) {
			round.setRoundEnd(true);
		}
	}

}
