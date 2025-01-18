package project.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.players.Player;
import project.players.User;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest
{
    Deck deck;
    Game game;
    List<Player> players;
    @BeforeEach
    void setUpTest()
    {
        this.game = new Game(null);
        this.deck = new Deck();
        this.players = new ArrayList<>();
        String[] names = new String[]{"Kalle", "Peter", "Lucas", "Stewie"};
        
        for (String name : names)
        {
            this.players.add(new User(name, game));
        }
        game.setPlayers(players);
        game.setDeck(deck);
    }
    @Test
    void initializeGameSettingsCorrect()
    {
        game = new Game(null);
        game.initializeGame();
        
        assertNotNull(game.getDeck());
        assertEquals(2, game.getTeams().size());
        assertEquals(-1, game.getBetIndex());
        assertEquals(-1, game.getDealerIndex());
        assertEquals(4, game.getPlayers().size());
        
        
    }
    @Test
    public void largestCardTest() {
        game.setSuit("");
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(6, 1));
        cards.add(new Card(2, 0));
        cards.add(new Card(9, 2));
        cards.add(new Card(9, 0));
        assertEquals(-1, game.checkHighestCard(cards));
        
        // Found weird results with these cards
        game.setSuit(Card.getSuitAsString(0));
        cards = new ArrayList<>();
        cards.add(null);
        cards.add(new Card(10, 0));
        cards.add(null);
        cards.add(new Card(Card.QUEEN, 0));
        assertEquals(3, game.checkHighestCard(cards));
    }
    @Test
    public void decideDealerTest() {
        //game.initializeGame();
        game.setDealerIndex(0);
        game.decideDealer();
        assertEquals(1, game.getDealerIndex());
        
    }
    @Test
    public void firstDealerTest() {
        //game.initializeGame();
        game.decideFirstDealer();
        assertEquals(3, game.getDealerIndex());
        
        LinkedList<Card> cards = new LinkedList<>();
        for (int i = 0; i < 4; i++)
        {
            cards.addFirst(new Card(4, i % 3));
            // Every card at the end of the deck has a value of 6 and 5 respectively
            if (i < 3) cards.addLast(new Card(6 - (i % 3), 2));
        }
        cards.addLast(new Card(4,1));
        deck.setCards(cards);
        game.decideFirstDealer();
        assertEquals(0, game.getDealerIndex());
    }
    @Test
    public void giveCardTest()
    {
        game.giveCards();
        
        int suitValue = Card.SPADES;
        int j = 0;
        for (Player player : players)
        {
            Hand hand = player.getHand();
            int cardValue = 2 + j * 3;
            j = cardValue + 3 > Card.ACE ? 0 : j + 1;
            if (cardValue + 3 > Card.ACE) {suitValue++;}
            assertEquals(new Card(cardValue, suitValue).toString(), hand.getCard(0).toString());
            assertNotEquals(new Card(cardValue, suitValue).toString(), hand.getCard(3).toString());
        }
        
    }
    /*
    @Test
    public void bettingTester() {
        game.giveCards();
        game.sortBySuit();
        game.setDealerIndex(0);
        game.betting();
    }
    */
    
    /**
     * This cannot be tested, due to user input not being accepted in this test.txt environment
     */
    public void decideSuitTester() {
        this.game.initializeGame();
        this.game.setBetIndex(2);
        this.game.decideSuit();
    }
    
    public void sortHandTest() {
        this.game.initializeGame();
        this.game.setPlayers(this.players);
        this.game.giveCards();
        Hand hand = ((Player)this.players.get(0)).getHand();
        hand.showHand();
        hand.sortBySuit();
        hand.showHandCompact(true);
    }
    
    public void fixHandTest() {
        this.game.initializeGame();
        this.game.setPlayers(this.players);
        this.game.giveCards();
        this.game.setDealerIndex(1);
        this.game.setSuit("Hearts");
        Iterator var1 = this.players.iterator();
        
        Player player;
        while(var1.hasNext()) {
            player = (Player)var1.next();
            player.getHand().showHandCompact(true);
        }
        
        this.game.fixHands();
        var1 = this.players.iterator();
        
        while(var1.hasNext()) {
            player = (Player)var1.next();
            player.getHand().showHandCompact(true);
        }
        
        System.out.println("Cards left in deck: " + this.game.getDeck().cardsLeft());
    }
    
    public void teamTest() {
        this.game.initializeGame();
        this.game.setDealerIndex(0);
        this.game.giveCards();
        this.game.betting();
        List<Team> teams = new ArrayList(this.game.getTeams());
        Iterator var2 = teams.iterator();
        
        while(var2.hasNext()) {
            Team team = (Team)var2.next();
            if (team.getBet() > 0) {
                PrintStream var10000 = System.out;
                String var10001 = team.getName();
                var10000.println("demo.Team " + var10001 + " bet " + team.getBet());
            }
        }
        
    }
    
    public void startRoundTest() {
        this.game.initializeGame();
        this.game.setDealerIndex(0);
        this.game.setDeck(this.deck);
        this.game.giveCards();
        this.game.setBetIndex(3);
        this.game.setSuit("Hearts");
        this.game.fixHands();
        this.game.sortBySuit();
        this.game.startRound();
        this.game.displayScore();
    }
    /*
    public void calculateScoreTest() {
        this.game.initializeGame();
        this.game.setSuit("Hearts");
        Team team1 = (Team)this.game.getTeams().get(0);
        Team team2 = (Team)this.game.getTeams().get(1);
        List<Card> playedCards = new ArrayList();
        playedCards.add(new Card(2, 2));
        playedCards.add(new Card(3, 2));
        playedCards.add((Object)null);
        playedCards.add((Object)null);
        int score1 = this.game.calculateScore(team1, playedCards, 0);
        int score2 = this.game.calculateScore(team2, playedCards, 0);
        int numberOfTests = 2;
        int testCompleted = 0;
        int answer1 = 1;
        int answer2 = 0;
        if (score1 != answer1) {
            System.out.println("Test failed! Expected " + answer1 + ", got " + score1);
        } else {
            ++testCompleted;
        }
        
        if (score2 != answer2) {
            System.out.println("Test failed! Expected " + answer2 + ", got " + score2);
        } else {
            ++testCompleted;
        }
        
        System.out.println("Completed tests: " + testCompleted + " of " + numberOfTests);
    }
    
     */
    
    public void addScoreTester() {
        this.game.initializeGame();
        List<Team> teams = new ArrayList(this.game.getTeams());
        Team team1 = (Team)teams.get(0);
        Team team2 = (Team)teams.get(1);
        team1.setBet(0);
        team2.setBet(7);
        int[] scoreList = new int[]{6, 8};
        this.game.addScore(scoreList, teams);
        int totalTests = 2;
        int completedTests = 0;
        int answer1 = 6;
        int answer2 = 8;
        int score1 = team1.getScore();
        int score2 = team2.getScore();
        if (score1 != answer1) {
            System.out.println("Test failed! Expected " + answer1 + ", got " + score1);
        } else {
            ++completedTests;
        }
        
        if (score2 != answer2) {
            System.out.println("Test failed! Expected " + answer2 + ", got " + score2);
        } else {
            ++completedTests;
        }
        
        System.out.println("Completed tests: " + completedTests + " of " + totalTests);
    }
    
    public void decideWinnerTest() {
        this.game.initializeGame();
        List<Team> teams = this.game.getTeams();
        Team team1 = (Team)teams.get(0);
        Team team2 = (Team)teams.get(1);
        team1.setBet(8);
        team2.setBet(0);
        team1.addScore(68);
        team2.addScore(70);
        Team winner = this.game.decideWinner(teams);
        if (team1 != winner) {
            System.out.println("Test failed!");
        } else {
            this.game.displayWinner(winner);
        }
        
    }
    
    public void validBetTester() {
        this.game.initializeGame();
        this.game.setDealerIndex(0);
        if (this.game.validBet(0, 5, true, (Player)this.game.getPlayers().get(0))) {
            System.out.println("Test failed");
        } else {
            System.out.println("Test Complete");
        }
        
    }
    @Test
    public void removeUselessCardsFalseTest()
    {
        game.setSuit("Spades");
        Hand hand = players.getFirst().getBettingHand();
        List<Card> cards = new ArrayList<>();
        
        cards.add(new Card(5, Card.SPADES));
        cards.add(new Card(9, Card.SPADES));
        cards.add(new Card(Card.QUEEN, Card.SPADES));
        cards.add(new Card(Card.KING, Card.SPADES));
        cards.add(new Card(Card.ACE, Card.SPADES));
        cards.add(new Card(5, Card.DIAMONDS));
        
        hand.addCards(cards);
        
        game.removeUselessCards(players.getFirst(), false);
        assertEquals(5, hand.getCardCount());
    }
    @Test
    void fixHandsWhenThreePlayersNeedSixCards()
    {
        // Kalle is dealer
        game.setDealerIndex(0);
        
        deck.drawCards(9*4); // 16 cards left
        
        game.fixHands();
    }
}