package chinchon.dominio;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import chinchon.app.Round;

public class MachinePlayer extends Player {

	private Difficulty difficulty;

	public MachinePlayer(String name, Difficulty difficulty) {
		super(name);
		this.difficulty = difficulty;
	}

	@Override
	public void decisionMaking(Round round) {
		if (difficulty == Difficulty.EASY) {
			aleatoryDecisions(round);
		} else if (difficulty == Difficulty.MEDIUM) {
			normalDecisions(round);
		} else {
			cheaterDecisions(round);
		}
	}

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

		rNumber = r.nextInt(8);
		round.discardCard(hand.getCards().get(rNumber));
		hand.removeCard(hand.getCards().get(rNumber));

		if (canEndRound) {
			round.setRoundEnd(true);
		} 

	}

	public void normalDecisions(Round round) {

		System.out.printf("Jugador %s\n", nickname);
		System.out.printf("Cartas actuales: %s\n", hand.toString());
		chooseWhereToPick(round);
		chooseCardToDiscard(round);

	}

	public void cheaterDecisions(Round round) {

	}

	public void chooseWhereToPick(Round round) {

		List<Card> searchCards = new ArrayList<>(analyzer.cardsForSearch(hand.getCards()));

		System.out.println("===== Robar =====");
		System.out.println("1) Robar de la baraja");
		System.out.printf("2) Robar de la pila de descarte: %s\n", round.seeLastCardDiscardPile());

		for (Card c1 : searchCards) {
			if (c1.equals(round.peekLastFromDiscard())) {
				hand.getCards().add(round.drawDiscardPileCard());
				return;
			}
		}
		hand.getCards().add(round.drawDeckCard());
	}

	public void chooseCardToDiscard(Round round) {

		List<Card> secureCards = new ArrayList<>(analyzer.protectedCards(hand.getCards()));
		List<Card> insecureCards = new ArrayList<>();
		Card cardToDiscard;
		boolean canEndRound;

		canEndRound= analyzer.checkCombinations(hand.getCards()) && round.getTurn() != 1
				&& round.checkScore(analyzer.currentPoints(
						analyzer.obtainCombinations(hand.getCards()), hand.getCards(), points));

		System.out.println("===== Descartar =====");
		System.out.println("1) Descartar una carta");
		if (canEndRound) {
			System.out.println("2) Descartar una carta y terminar la ronda");
		}

		System.out.println("===== Mano actual =====");
		System.out.println(hand.enumeratedCards());
		System.out.println("Escoje cual descartar: ");

		insecureCards.addAll(hand.getCards());
		insecureCards.removeAll(secureCards);

		if (!insecureCards.isEmpty()) {
			cardToDiscard = insecureCards.get(insecureCards.size() - 1);
		} else if (!secureCards.isEmpty()) {
			cardToDiscard = secureCards.get(secureCards.size() - 1);
		} else {
			cardToDiscard = hand.getCards().get(0);
		}

		System.out.printf("Se ha descartado la siguiente carta: %s\n", cardToDiscard);
		round.discardCard(cardToDiscard);
		hand.removeCard(cardToDiscard);

		if (canEndRound) {
			round.setRoundEnd(true);
		}

	}

}
