package chinchon.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import chinchon.dominio.Card;
import chinchon.dominio.Combination;
import chinchon.dominio.CombinationAnalyzer;
import chinchon.dominio.DiscardPile;
import chinchon.dominio.Player;
import chinchon.dominio.PlayerFactory;
import chinchon.dominio.Suit;
import chinchon.dominio.Value;


public class WhiteBoxTests {

	private final CombinationAnalyzer analyzer = CombinationAnalyzer.getInstance();
	
	@Test
	public void detectsCorrectlyLadderAndTripleSimultaneously() {
		List<Card> hand;
		List<Combination> combs;
		
        hand = List.of(
                new Card(Suit.COINS, Value.FIVE),
                new Card(Suit.CUPS, Value.FIVE),
                new Card(Suit.SWORDS, Value.FIVE),
                new Card(Suit.COINS, Value.SIX),
                new Card(Suit.COINS, Value.SEVEN),
                new Card(Suit.CLUBS, Value.FIVE)
        );

        combs = analyzer.obtainCombinations(hand);

        assertEquals(2, combs.size());
    }
	
	@Test
	public void protectedCardsIncludesCombinationsAndAlmostLaddersTriples() {
		List<Card> hand,protectedCards;
		
        hand = List.of(
                new Card(Suit.CUPS, Value.FOUR),
                new Card(Suit.CUPS, Value.FIVE),
                new Card(Suit.CUPS, Value.SIX),
                new Card(Suit.SWORDS, Value.HORSE),
                new Card(Suit.SWORDS, Value.KING),
                new Card(Suit.CLUBS, Value.ONE),
                new Card(Suit.COINS, Value.ONE)
        );

       protectedCards = analyzer.protectedCards(hand);

       /* Detecta los 7 como protegidas ya que son parte de combinacion o casi combinacion 
        * pero en protected se le pasa la mano para encontrar las casi combinacion,
        * por lo tanto, detecta como extra la FOUR - FIVE, FIVE - SIX, evita errores 
        * en la lógica de la IA pero hace que la lista sea más grande de lo esperado. 
        */
        assertEquals(11, protectedCards.size());
    }
	
	@Test
	public void returnFirstAndLastOfLaddersWhenAllAreCombinated() {
        Player p = PlayerFactory.createHumanPlayer("Conejillo de indias 2");
        List<Card> discardableCards;

        p.addCardToHand(new Card(Suit.CUPS, Value.FOUR));
        p.addCardToHand(new Card(Suit.CUPS, Value.FIVE));
        p.addCardToHand(new Card(Suit.CUPS, Value.SIX));
        p.addCardToHand(new Card(Suit.CUPS, Value.SEVEN)); 

        discardableCards = p.getDiscardableCards();

        assertEquals(2, discardableCards.size());
        assertEquals(Value.FOUR, discardableCards.get(0).getValue());
        assertEquals(Value.SEVEN, discardableCards.get(1).getValue());
	}
	
	@Test
	public void returnFirstAndLastOfLaddersWhenAllAreCombinatedAndAllOfTheTriplesCombinations() {
        Player p = PlayerFactory.createHumanPlayer("Conejillo de indias 3");
        List<Card> discardableCards;

        /* La condición de 8 cartas siempre se da, porque es un método que se hace antes de descartar
         * una carta para evitar que el jugador rompa las combinaciones obtenidas
         */
        p.addCardToHand(new Card(Suit.CUPS, Value.FOUR));
        p.addCardToHand(new Card(Suit.CUPS, Value.FIVE));
        p.addCardToHand(new Card(Suit.CUPS, Value.SIX));
        p.addCardToHand(new Card(Suit.CUPS, Value.SEVEN));
        p.addCardToHand(new Card(Suit.SWORDS, Value.ONE));
        p.addCardToHand(new Card(Suit.COINS, Value.ONE));
        p.addCardToHand(new Card(Suit.CLUBS, Value.ONE));
        p.addCardToHand(new Card(Suit.CUPS, Value.ONE));

        discardableCards = p.getDiscardableCards();
        
        /*
         * Devuelve 6 porque deja descartar la primera o última de combinación en caso de que 
         * la escalera sea >= 4 y de los tríos >= 4 deja descartar cualquiera. 
         */

        assertEquals(6, discardableCards.size());
        assertEquals(Value.FOUR, discardableCards.get(0).getValue());
        assertEquals(Value.SEVEN, discardableCards.get(1).getValue());
        assertEquals(Suit.SWORDS, discardableCards.get(2).getSuit());
        assertEquals(Suit.COINS, discardableCards.get(3).getSuit());
        assertEquals(Suit.CLUBS, discardableCards.get(4).getSuit());
        assertEquals(Suit.CUPS, discardableCards.get(5).getSuit());
	}
	
	@Test
	public void returnNonCombinatedCards() {
        Player p = PlayerFactory.createHumanPlayer("Conejillo de indias 4");
        List<Card> discardableCards;

        p.addCardToHand(new Card(Suit.CUPS, Value.FOUR));
        p.addCardToHand(new Card(Suit.CUPS, Value.FIVE));
        p.addCardToHand(new Card(Suit.CUPS, Value.SIX));
        p.addCardToHand(new Card(Suit.CUPS, Value.SEVEN)); 
        p.addCardToHand(new Card(Suit.SWORDS, Value.HORSE));

        discardableCards = p.getDiscardableCards();

        assertEquals(1, discardableCards.size());
        assertEquals(Value.HORSE, discardableCards.get(0).getValue());
        assertEquals(Suit.SWORDS, discardableCards.get(0).getSuit());
	}
	
	@Test
    public void returnACopyOfDiscardPileAndClearDiscardPile() {
        DiscardPile pile = new DiscardPile();
        pile.addCard(new Card(Suit.COINS, Value.FIVE));
        pile.addCard(new Card(Suit.CLUBS, Value.JACK));

        var returned = pile.clearAndReturn();

        assertEquals(2, returned.size());
        assertTrue(pile.getCards().isEmpty());
    }
}
