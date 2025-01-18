package project.players;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.demo.Card;
import project.demo.Deck;
import project.demo.Game;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class ComputerTest
{
    Player p;
    Game g;
    @BeforeEach
    void startUp()
    {
        g = new Game(null);
        g.initializeGame(false);
        p = new Computer("Test", g);
        List<Player> l = new ArrayList<>();
        l.add(p);
        g.setPlayers(l);
    }
    //@Test
    public void isDealerTester() {
        String msg = this.p.getIsDealer() ? " is Dealer" : " is not Dealer";
        PrintStream var10000 = System.out;
        String var10001 = this.p.getName();
        var10000.println(var10001 + msg);
        this.p.setIsDealer(true);
        msg = this.p.getIsDealer() ? " is Dealer" : " is not Dealer";
        var10000 = System.out;
        var10001 = this.p.getName();
        var10000.println(var10001 + msg);
    }
    //@Test
    public void isComputerTest() {
        Player p2 = new Computer("Petah", (Game)null);
        int testCompleted = 0;
        int totalTests = 3;
        if (!p2.getName().equals("Bot")) {
            System.out.println("Test failed. Expected Bot, got " + p2.getName());
        } else {
            ++testCompleted;
        }
        
        System.out.println("Completed tests: " + testCompleted + " of " + totalTests);
    }
    
    @Test
    void testStartBetting()
    {
        Deck d = new Deck();
        LinkedList<Card> cards = new LinkedList<>();
        
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
        
        d.setCards(cards);
        g.setDeck(d);
        g.giveCards();
        p.startBetting();
        
        
    }
    
    @Test
    void testDecideSuit()
    {
    }
}
