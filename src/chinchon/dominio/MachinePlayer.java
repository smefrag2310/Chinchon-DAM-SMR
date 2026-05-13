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
		} else {
			normalDecisions(round);
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

		rNumber = r.nextInt(hand.getCards().size());
		round.discardCard(hand.getCards().get(rNumber));
		hand.removeCard(hand.getCards().get(rNumber));

		if (canEndRound) {
			round.setRoundEnd(true);
		} 

	}

	public void normalDecisions(Round round) {

		System.out.printf(">> Jugador actual: %s\n", nickname);
		System.out.printf(">> Cartas actuales: %s\n", hand.toString());
		chooseWhereToPick(round);
		chooseCardToDiscard(round);

	}

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
