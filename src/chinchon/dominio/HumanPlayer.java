package chinchon.dominio;

import chinchon.app.ConsoleInput;
import chinchon.app.Round;

public class HumanPlayer extends Player{
	
	private ConsoleInput console;

	public HumanPlayer(String name) {
		super(name);
		console= ConsoleInput.getInstance();
	}

	@Override
	public void decisionMaking(Round round) {
		
		System.out.printf("Jugador %s\n",nickname);
		System.out.printf("Cartas actuales: %s\n",hand.toString());
		drawACard(round);
		discardACardOrEnd(round);
		
	}
	
	private void drawACard(Round round) {
		
		int option;
		
		console.writeLine("===== Robar =====");
		console.writeLine("1) Robar de la baraja");
		System.out.printf("2) Robar de la pila de descarte: %s\n",round.seeLastCardDiscardPile());
		option= console.readIntInRange(1, 2);
		
		if(option==1) {
			hand.getCards().add(round.drawDeckCard());
		}else {
			hand.getCards().add(round.drawDiscardPileCard());
		}
	}
	
	private void discardACardOrEnd(Round round) {
		
		int option,discardIndex;
		boolean canEndRound;
		Card card;
		
		canEndRound= analyzer.checkCombinations(hand.getCards()) && round.getTurn() != 1
				&& round.checkScore(analyzer.currentPoints(
						analyzer.obtainCombinations(hand.getCards()),
						hand.getCards(), points));
		
		console.writeLine("===== Descartar =====");
		console.writeLine("1) Descartar una carta");
		if(canEndRound) {
			console.writeLine("2) Descartar una carta y terminar la ronda");
		}
		
		option= console.readIntInRange(1, canEndRound ? 2:1);
		
		console.writeLine("===== Mano actual =====");
		console.writeLine(hand.enumeratedCards());
		console.writeLine("Escoje cual descartar: ");
		discardIndex= console.readIntInRange(1, hand.getCards().size());
		card= hand.getCards().get(discardIndex-1);
		
			System.out.printf("Se ha descartado la siguiente carta: %s\n",card);
			hand.removeCard(card);
			round.discardCard(card);
			
			if(option==2) {
			round.setRoundEnd(true);
			}
		
	}

}
