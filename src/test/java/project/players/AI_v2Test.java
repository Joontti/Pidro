package project.players;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.demo.Card;
import project.demo.Deck;
import project.demo.Game;
import project.players.ai.AI_v2;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AI_v2Test
{
    Player p;
    Game g;
    @BeforeEach
    void startUp()
    {
        g = new Game(null);
        g.initializeGame(false);
        p = new AI_v2("Test", g);
        List<Player> l = new ArrayList<>();
        l.add(p);
        g.setPlayers(l);
        g.setDeck(new Deck());
    }
    @Test
    public void decideCardTest()
    {
        List<Card> cards = new ArrayList<>();
        
        // The new deck consists of these cards
        cards.add(new Card(5, Card.HEARTS));
        cards.add(new Card(5, Card.DIAMONDS));
        cards.add(new Card(10, Card.HEARTS));
        cards.add(new Card(Card.ACE, Card.HEARTS));
        cards.add(new Card(5, Card.SPADES));
        cards.add(new Card(5, Card.CLUBS));
        cards.add(new Card(Card.KING, Card.HEARTS));
        cards.add(new Card(3, Card.SPADES));
        cards.add(new Card(Card.QUEEN, Card.CLUBS));
        
        p.getHand().addCards(cards);
        g.setSuit(Card.getSuitAsString(Card.HEARTS));
        
        Card card = p.decideCard();
        assertEquals(cards, p.getHand().getCards());
        //assertEquals(card.toString(), new Card(10, Card.SPADES).toString());
        /*
        g.giveCards();
        g.setSuit(Card.getSuitAsString(1));
        card = p.decideCard();
        assertEquals(card.toString(), new Card(6, Card.CLUBS).toString());
         */
    }
    
    @Test
    public void bettingTest()
    {
        g.giveCards();
        g.setSuit(Card.getSuitAsString(0));
        p.startBetting();
        System.out.println("Bet: " + p.getBet());
        assertTrue(p.getBet() == 0 || (p.getBet() >= 6 || !(p.getBet() > 14)));
    }
}
