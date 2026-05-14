package chinchon.test;

import chinchon.dominio.Card;
import chinchon.dominio.Combination;
import chinchon.dominio.CombinationAnalyzer;
import chinchon.dominio.CombinationType;
import chinchon.dominio.Difficulty;
import chinchon.dominio.DiscardPile;
import chinchon.dominio.MachinePlayer;
import chinchon.dominio.Player;
import chinchon.dominio.PlayerFactory;
import chinchon.dominio.Suit;
import chinchon.dominio.Value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;

import org.junit.Test;

public class BlackBoxTests {

	private final CombinationAnalyzer analyzer = CombinationAnalyzer.getInstance();

	@Test
	public void detectsTripleCorrectly() {
		List<Card> hand;
		List<Combination> combs;

		hand = List.of(
				new Card(Suit.COINS, Value.FIVE),
				new Card(Suit.CUPS, Value.FIVE),
				new Card(Suit.CLUBS, Value.FOUR),
				new Card(Suit.SWORDS, Value.FIVE));
				

		combs = analyzer.obtainCombinations(hand);

		assertEquals(1, combs.size());
		assertEquals(CombinationType.TRIPLE, combs.get(0).getType());
	}

	@Test
	public void detectsLadderCorrectly() {
		List<Card> hand;
		List<Combination> combs;
		
		hand = List.of(
				new Card(Suit.CUPS, Value.FOUR),
				new Card(Suit.CUPS, Value.FIVE),
				new Card(Suit.CUPS, Value.SIX));

		combs = analyzer.obtainCombinations(hand);

		assertEquals(1, combs.size());
		assertEquals(CombinationType.LADDER, combs.get(0).getType());
	}

	@Test
	public void detectsChinchonCorrectly() {
		List<Card> hand;
		List<Combination> combs;
		
		hand = List.of(
				new Card(Suit.SWORDS, Value.ONE),
				new Card(Suit.SWORDS, Value.TWO),
				new Card(Suit.SWORDS, Value.THREE),
				new Card(Suit.SWORDS, Value.FOUR),
				new Card(Suit.SWORDS, Value.FIVE),
				new Card(Suit.SWORDS, Value.SIX),
				new Card(Suit.SWORDS, Value.SEVEN));

		combs = analyzer.obtainCombinations(hand);

		assertEquals(1, combs.size());
		assertEquals(CombinationType.CHINCHON, combs.get(0).getType());
	}
	
	@Test
	public void returnOnlyNonCombinatedCards() {
		Player p= PlayerFactory.createHumanPlayer("Conejillo de indias");
		List<Card> discardableCards;
		
		p.addCardToHand(new Card(Suit.CUPS,Value.THREE));
		p.addCardToHand(new Card(Suit.CUPS,Value.FOUR));
		p.addCardToHand(new Card(Suit.CUPS,Value.FIVE));
		p.addCardToHand(new Card(Suit.COINS,Value.JACK));
		
		discardableCards = p.getDiscardableCards();
		
		assertEquals(1, discardableCards.size());
		assertEquals(Suit.COINS, discardableCards.get(0).getSuit());
	}
	
	@Test
	public void returnCorrectlyLastCardFromDiscardPile() {
		DiscardPile pile = new DiscardPile();
		Card c1,c2,cardFromDiscard;
		
		c1 = new Card(Suit.CLUBS,Value.HORSE);
		c2 = new Card(Suit.SWORDS,Value.KING);
		
		pile.addCard(c1);
		pile.addCard(c2);
		
		cardFromDiscard = pile.removeCard();
		
		assertEquals(c2,cardFromDiscard);
		
	}
	
	@Test
	public void correctCreationOfMachinePlayer() {
		MachinePlayer machine= (MachinePlayer) PlayerFactory.createMachinePlayer("Máquina test", Difficulty.MEDIUM);
		
		assertEquals("Máquina test",machine.getNickname());
		assertEquals(0,machine.getPoints());
	}
}
