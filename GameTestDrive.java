import java.util.*;
public class GameTestDrive
{
    Deck deck;
    Game game;
    int test;
    List<Player> players;

    public GameTestDrive(int t)
    {
        game = new Game();
        deck = new Deck();
        test = t;
        players = new ArrayList<>();
        String[] names = new String[] {"Kalle", "Peter", "Lucas", "Stewie"};
        for (String username: names)
        {
            players.add(new Player(username));
        }
    }
    public void start()
    {
        switch (test)
        {
            case 0 ->
            {
                for (int i = 0; i < 10; i++) {
                    largestCardTestRandom();
                }
            }
            case 1 -> largestCardTest();
            case 2 -> decideDealerTest();
            case 3 -> giveCardTest();
            case 4 -> bettingTester();
            case 5 -> decideSuitTester();
            case 6 -> sortHandTest();
            case 7 -> fixHandTest();
            case 8 -> teamTest();
            case 9 -> startRoundTest();
            case 10 -> calculateScoreTest();
            case 11 -> addScoreTester();
            case 12 -> decideWinnerTest();
            case 13 -> validBetTester();
        }
    }

    /**
     * if test == 0
     */
    public void largestCardTestRandom()
    {
        List<Card> cards = new ArrayList<>();
        deck.shuffle();
        String playCards = "";
        for (int i = 0; i < 4; i++)
        {
            cards.add(deck.drawCard());
            playCards = playCards + ", " + cards.get(i).toString();
        }
        int highestCard = game.checkHighestCard(cards, "Clubs");
        System.out.println("Playcards: " + playCards + ".");
        if (highestCard == -1)
        {
            System.out.println("FAILED!");
        }
        else
        {
            System.out.println("Highest card: " + cards.get(highestCard).toString() );
        }
    }
    /**     If test == 1
     *
     *          cards.add(new Card(10, Card.DIAMONDS));
     *          cards.add(new Card(5, Card.HEARTS));
     *          cards.add(new Card(8, Card.CLUBS));
     *          cards.add(new Card(2, Card.SPADES));
     */
    public void largestCardTest()
    {
        List<Card> cards = new ArrayList<>();
        String status;
        cards.add(new Card(6, Card.CLUBS));
        cards.add(new Card(2, Card.SPADES));
        cards.add(new Card(9, Card.HEARTS));
        cards.add(new Card(9, Card.SPADES));
        int highestCard = game.checkHighestCard(cards, "");
        int expectedValue = -1;
        status = (highestCard == expectedValue) ? "completed": "failed";
        System.out.println("Test " + status + "!");
        System.out.println("Test expected " + expectedValue + ", got " + highestCard);

    }
    public void decideDealerTest()
    {
        game.initializeGame();
        game.setDealerIndex(0);
        game.decideDealer();
        if (game.getDealerIndex() != 1)
        {
            System.out.println("Test failed. Expected " + 1 + ", got " + game.getDealerIndex());
        }
        else {
            System.out.println("Test completed!");
        }
    }
    public void giveCardTest()
    {
        game.initializeGame();
        game.setDeck(deck);
        game.giveCards();
        List<Player> players = new ArrayList<>(game.getPlayers());
        for (Player player: players)
        {
            player.getHand().showHand();
        }
    }
    public void bettingTester()
    {
        game.initializeGame();
        game.giveCards();
        game.sortBySuit();
        game.setDealerIndex(0);
        game.betting();
    }
    public void decideSuitTester()
    {
        game.initializeGame();
        game.setBetIndex(2);
        game.decideSuit();
    }
    public void sortHandTest()
    {
        game.initializeGame();
        game.setPlayers(players);
        game.giveCards();
        Hand hand = players.get(0).getHand();
        hand.showHand();
        hand.sortBySuit();
        hand.showHandCompact(true);
    }
    public void fixHandTest()
    {
        game.initializeGame();
        //game.setDeck(deck);     // non-shuffled deck
        game.setPlayers(players);
        game.giveCards();
        /**
        Player p = players.get(0);
        p.getHand().clear();
        for (int i = 0; i < 9; i++) {
            p.getHand().addCard(new Card(2 + i, 1));
        }
         **/
        game.setDealerIndex(1);
        game.setSuit("Hearts");
        for (Player player: players)
        {
            player.getHand().showHandCompact(true);
        }
        game.fixHands();
        for (Player player: players)
        {
            player.getHand().showHandCompact(true);
        }
        System.out.println("Cards left in deck: " + game.getDeck().cardsLeft());
    }
    public void teamTest()
    {
        game.initializeGame();
        game.setDealerIndex(0);
        game.giveCards();
        game.betting();
        List<Team> teams = new ArrayList<>(game.getTeams());
        for (Team team: teams)
        {
            if (team.getBet() > 0)
                System.out.println("Team " + team.getName() + " bet " + team.getBet());
        }
    }
    public void startRoundTest()
    {
        game.initializeGame();
        game.setDealerIndex(0);

        // Setting the game deck to a non shuffled deck
        game.setDeck(deck);

        game.giveCards();
        game.setBetIndex(3);
        game.setSuit("Hearts");
        game.fixHands();
        game.sortBySuit();
        game.startRound();
        game.displayScore();
    }

    /**
     * IF TEST == 10
     */
    public void calculateScoreTest()
    {
        game.initializeGame();
        game.setSuit("Hearts");
        Team team1 = game.getTeams().get(0);
        Team team2 = game.getTeams().get(1);
        List<Card> playedCards = new ArrayList<>();
        playedCards.add(new Card(2, Card.HEARTS));
        playedCards.add(new Card(3, Card.HEARTS));
        playedCards.add(null);
        playedCards.add(null);
        int score1 = game.calculateScore(team1, playedCards, 0);
        int score2 = game.calculateScore(team2, playedCards, 0);

        int numberOfTests = 2;
        int testCompleted = 0;

        int answer1 = 1;
        int answer2 = 0;

        if (score1 != answer1) {
            System.out.println("Test failed! Expected " + answer1 + ", got " + score1);
        }
        else testCompleted ++;
        if (score2 != answer2)
            System.out.println("Test failed! Expected " + answer2 + ", got " + score2);
        else testCompleted ++;

        System.out.println("Completed tests: " + testCompleted + " of " + numberOfTests);
    }
    public void addScoreTester()
    {
        game.initializeGame();
        List<Team> teams = new ArrayList<>(game.getTeams());
        Team team1 = teams.get(0);
        Team team2 = teams.get(1);
        team1.setBet(0);
        team2.setBet(7);
        int[] scoreList = new int[2];
        scoreList[0] = 6;
        scoreList[1] = 8;

        game.addScore(scoreList, teams);

        int totalTests = 2;
        int completedTests = 0;

        int answer1 = 6;
        int answer2 = 8;

        int score1 = team1.getScore();
        int score2 = team2.getScore();
        if (score1 != answer1){
            System.out.println("Test failed! Expected " + answer1 + ", got " + score1);
        }
        else completedTests++;
        if (score2 != answer2){
            System.out.println("Test failed! Expected " + answer2 + ", got " + score2);
        }
        else completedTests++;
        System.out.println("Completed tests: " + completedTests + " of " + totalTests);
    }
    public void decideWinnerTest()
    {
        game.initializeGame();
        List<Team> teams = game.getTeams();
        Team team1 = teams.get(0);
        Team team2 = teams.get(1);
        team1.setBet(8);
        team2.setBet(0);
        team1.addScore(68);
        team2.addScore(70);
        Team winner = game.decideWinner(teams);
        if (team1 != winner)
            System.out.println("Test failed!");
        else {
            game.displayWinner(winner);
        }
    }
    public void validBetTester()
    {
        game.initializeGame();
        game.setDealerIndex(0);
        if (game.validBet(0, 5, true, game.getPlayers().get(0)))
            System.out.println("Test failed");
        else
            System.out.println("Test Complete");
    }
}
