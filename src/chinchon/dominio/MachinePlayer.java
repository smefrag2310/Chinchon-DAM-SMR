package chinchon.dominio;

import java.util.ArrayList;
import java.util.Comparator;
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

		rNumber = r.nextInt(2);
		if (rNumber == 0) {
			hand.getCards().add(round.drawDeckCard());
		} else if (rNumber == 1) {
			hand.getCards().add(round.drawDiscardPileCard());
		}

		rNumber = r.nextInt(8);
		round.discardCard(hand.getCards().get(rNumber));
		hand.removeCard(hand.getCards().get(rNumber));

		if (checkCombinations()) {
			round.setRoundEnd(true);
		} else {
			round.setRoundEnd(false);
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

		List<Card> searchCards = new ArrayList<>(cardsForSearch());

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

		List<Card> secureCards = new ArrayList<>(protectedCards());
		List<Card> insecureCards = new ArrayList<>();
		Card cardToDiscard;
		boolean canEndRound;

		canEndRound = checkCombinations() && round.getTurn() != 1 && round.checkScore(currentPoints(obtainCombinations()));

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

	public List<Card> protectedCards() {

		List<Card> secureCards = new ArrayList<>();
		List<Card> remaining = new ArrayList<>(hand.getCards());
		List<Card> combinations = new ArrayList<>();
		
		combinations= obtainCombinations().stream()
				.flatMap(c -> c.getCards().stream())
				.toList();

		secureCards.addAll(combinations);
		remaining.removeAll(secureCards);
		secureCards.addAll(findAlmostLadders(remaining));
		remaining.removeAll(secureCards);
		secureCards.addAll(findAlmostTriples(remaining));
		remaining.removeAll(secureCards);

		return secureCards;

	}

	public List<Card> cardsForSearch() {

		List<Card> missing = new ArrayList<>();
		Card c1, c2;
		int v1, v2, min, max;

		for (int i = 0; i < hand.getCards().size(); i++) {
			for (int j = i + 1; j < hand.getCards().size(); j++) {

				c1 = hand.getCards().get(i);
				c2 = hand.getCards().get(j);

				if (c1.getSuit() == c2.getSuit()) {

					v1 = c1.getValue().getOrder();
					v2 = c2.getValue().getOrder();

					min = Math.min(v1, v2);
					max = Math.max(v1, v2);

					if (max - min == 2) {
						missing.add(new Card(c1.getSuit(), Value.fromOrder(min + 1)));
					}

					if (max - min == 1) {
						if (min > 1) {
							missing.add(new Card(c1.getSuit(), Value.fromOrder(min - 1)));
						}
						if (max < 12) {
							missing.add(new Card(c1.getSuit(), Value.fromOrder(max + 1)));
						}
					}

				}

				if (c1.getValue() == c2.getValue()) {

					for (Suit s : Suit.values()) {
						missing.add(new Card(s, c1.getValue()));
					}

				}

			}
		}

		return missing;

	}

	public List<Card> findAlmostLadders(List<Card> cards) {

		List<Card> list = new ArrayList<>();
		List<Card> actual = new ArrayList<>();
		List<Card> sequence = new ArrayList<>();
		Card last;

		for (Suit suit : Suit.values()) {

			list = cards.stream()
					.filter(c -> c.getSuit() == suit)
					.sorted(Comparator.comparing(c -> c.getValue()))
					.toList();

			for (Card c : list) {
				if (actual.isEmpty()) {
					actual.add(c);
				} else {
					last = actual.get(actual.size() - 1);

					if (c.getValue().getOrder() == last.getValue().getOrder() + 1) {
						actual.add(c);
					} else {

						if (actual.size() == 2) {
							sequence.addAll(actual);
						}
						actual.clear();
						actual.add(c);
					}
				}
			}
			if (actual.size() == 2) {
				sequence.addAll(actual);
			}
		}
		return sequence;

	}

	public List<Card> findAlmostTriples(List<Card> cards) {

		List<Card> list = new ArrayList<>();
		List<Card> sequence = new ArrayList<>();

		for (Value value : Value.values()) {

			list = cards.stream().filter(c -> c.getValue() == value).toList();

			if (list.size() == 2) {
				sequence.addAll(list);
			}
		}
		return sequence;

	}

}
