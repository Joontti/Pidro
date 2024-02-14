import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Game
{
    private List<Player> players;
    private List<Team> teams;
    private Deck deck;
    private int dealerIndex;
    private int betIndex;
    private String suit;
    private final HashMap<Integer, Integer> pointMap;

    /**
     * Used for testing purposes
     * @param people a list of players used for the game
     */
    public void setPlayers(List<Player> people)
    {
        players = people;
    }
    public void setDeck(Deck d)
    {
        deck = d;
    }
    public void setDealerIndex(int i)
    {
        if (!(i == -1)) {
            clearIsDealer();
            players.get(i).setIsDealer(true);
        }
        dealerIndex = i;
    }
    public void setBetIndex(int i)
    {
        if (!(i == -1)) {
            // Sets the bet index for the team.
            if (playerInTeam(0, i))
                teams.get(0).setBet(players.get(i).getBet());
            else
                teams.get(1).setBet(players.get(i).getBet());
            clearBetsPlayers(players);
        }
        betIndex = i;
    }
    public void setSuit(String s)
    {
        suit = s;
    }
    public Deck getDeck()
    {
        return deck;
    }
    public List<Player> getPlayers()
    {
        return players;
    }
    public int getDealerIndex()
    {
        return dealerIndex;
    }
    public int getBetIndex()
    {
        return betIndex;
    }
    public List<Team> getTeams()
    {
        return teams;
    }

    public Game()
    {
        players = new ArrayList<>();
        teams = new ArrayList<>();
        pointMap = new HashMap<>();
    }
    public boolean playerInTeam(int teamIndex, int playerIndex)
    {
        return teams.get(teamIndex).getMembers().contains(players.get(playerIndex));
    }
    public void clearIsDealer()
    {
        for (Player player: players)
        {
            player.setIsDealer(false);
        }
    }
    public void clearBetsPlayers(List<Player> players)
    {
        for (Player player: players)
        {
            player.setBet(0);
        }
    }
    public void clearBetsTeams(List<Team> teams)
    {
        for (Team team: teams)
        {
            team.setBet(0);
        }
    }
    public void clearPlayedHands()
    {
        for (Player player: players)
        {
            player.getPlayedHand().clear();
        }
    }
    public void initializeGame()
    {
        refreshDeck();

        // Adding values to the pointMap
        pointMap.put(2, 1);     // The card '2' has the value of 1
        pointMap.put(5, 5);     // The card '5' has the value of 5
        pointMap.put(10, 1);    // The card '10' has the value of 1
        pointMap.put(Card.JACK, 1);    // The card 'Jack' has the value of 1
        pointMap.put(Card.ACE, 1);    // The card 'Ace' has the value of 1

        // Player control index, depending on which player you want to control for the game
        // if set to -1 then all players are controlled by the computer
        int playerIndex = -1;

        // Adding players to 'players' list
        String[] names = new String[] {"Homer Simpsons", "Peter Griffin", "Bart Simpsons", "Stewie Griffin"};
        for (int i = 0; i < 4; i++)
        {
            players.add(new Player(names[i], i != playerIndex));    // computer boolean true means computer controlled
        }

        // Adding teams to 'teams' list
        List<Player> team1 = new ArrayList<>();
        List<Player> team2 = new ArrayList<>();
        String[] teamNames = new String[] {"The Simpsons", "Family Guy"};

        team1.add(players.get(0));
        team1.add(players.get(2));
        team2.add(players.get(1));
        team2.add(players.get(3));

        teams.add(new Team(teamNames[0], team1));
        teams.add(new Team(teamNames[1], team2));

        // Setting primary values to the indexes
        setDealerIndex(-1);
        setBetIndex(-1);
    }
    public void refreshDeck()
    {
        deck = new Deck();
        deck.shuffle();
    }
    public void startGame()
    {
        boolean winners = false;
        int round = 0;
        while (!winners)
        {
            // Clearing data from previous rounds
            clearPlayedHands();
            clearBetsTeams(teams);

            System.out.println("===== ROUND " + ++round + " =====");

            refreshDeck();
            decideDealer();
            refreshDeck();
            giveCards();
            sortBySuit();
            betting();
            decideSuit();
            fixHands();
            sortBySuit();
            startRound();
            displayScore();
            winners = checkScore();
        }
        Team winner = decideWinner(teams);
        displayWinner(winner);
    }
    public Team decideWinner(List<Team> teams)
    {
        Team team1 = teams.get(0);
        Team team2 = teams.get(1);

        int score1 = team1.getScore();
        int score2 = team2.getScore();

        if (score1 >= 62 && score2 >= 62) {
            if (team1.getBet() > 0)
                return team1;
        }
        else if (score1 > score2)
            return team1;
        return team2;
    }
    public void displayWinner(Team team)
    {
        System.out.println(team.getName() + " has won with a score of " + team.getScore());
        System.out.print("The team consisted of " + team.getMembers().get(0).getName() + " and " + team.getMembers().get(1).getName());
    }
    public void displayScore()
    {
        for (Team team: teams)
        {
            System.out.println("Score of " + team.getName() + ": " + team.getScore());
        }
    }
    public void displayRoundScore(int[] scoreList, List<Team> teams)
    {
        for (int i = 0; i < teams.size(); i++)
        {
            int score = scoreList[i];
            String msg = "Team " + teams.get(i).getName();
            if (score < 0)
                msg += " lost " + score + " points.";
            else
                msg += " gained " + score + " points.";
            System.out.println(msg);
        }
    }
    public void decideFirstDealer()
    {
        List<Card> cards = new ArrayList<>();
        int index;
        while (true)
        {
            for (Player player: players)
            {
                Card card = deck.drawCard();
                cards.add(card);
                System.out.println(player.getName() + " got " + card.toString() + ".");
            }
            index = checkHighestCard(cards, "");    // empty suit means that suit doesn't matter
            if (index != -1) break;
            cards.clear();                              // if there are multiple attempts then this is needed
        }
        Player dealer = players.get(index);
        setDealerIndex(index);
        System.out.println("The first dealer is " + dealer.getName() + "!");
    }
    public void decideDealer()
    {
        if (!(dealerIndex == -1))
        {
            setDealerIndex(nextPlayerIndex(dealerIndex, 1));
        }
        else
        {
            decideFirstDealer();
        }
    }

    /**
     *
     */
    public void decideSuit()
    {
        Player player = players.get(betIndex);
        String msg = player.getName() + " chooses: ";
        int answer;
        if (!player.getIsComputer()) {
            player.getHand().showHandCompact(true);
            System.out.println("Enter one of the following digits to decide suit:");
            for (int i = 0; i < 4; i++) {
                System.out.println((i + 1) + ": " + new Card(3, i).getSuitAsString());
            }
            answer = getInt(msg) - 1;
        }
        else {
            answer = getComputerInt(0, 3);
            System.out.print(msg);
        }
        setSuit(new Card(3, answer).getSuitAsString());
        System.out.println(suit);
    }
    int nextPlayerIndex(int index, int steps)
    {
        return (index + steps) % 4;
    }
    public void giveCards()
    {
        for (int i = 0; i < 3; i++)
        {
            for (Player player: players)
            {
                player.getHand().addCards(deck.drawCards(3));
            }
        }
    }

    /** This function checks also if the amount is correct, between 0 and 6.
     *
     * @param amount the amount of cards to be added to hand
     * @param player the player whose hand will receive the cards
     */
    public void fillHand(int amount, Player player)
    {
        Hand hand = player.getHand();
        if (!player.getIsDealer())
            System.out.println(player.getName() + " requested " + amount + " cards.");

        if (amount == 1) {
            hand.addCard(deck.drawCard());
        }
        else if ( (1 < amount && amount < 7) || player.getIsDealer())
        {
            hand.addCards(deck.drawCards(amount));
        }
        else if (amount != 0)
        {
            System.out.println("Invalid card amount.");
        }
    }
    public void sortBySuit()
    {
        for (Player player: players)
        {
            player.getHand().sortBySuit();
        }
    }

    /**
     *
     */
    public void betting()
    {
        System.out.println("Welcome to the betting round. Enter an integer between 6 and 14" +
                " if you want to bet. Enter 0 if you want to skip.");
        boolean noBets = true;
        int index = dealerIndex;
        int highestBet = 5;             // will change in the for loop
        for (int i = 0; i < 4; i++)
        {
            index = nextPlayerIndex(index, 1);
            Player player = players.get(index);
            int bet;
            while (true)
            {
                if (player.getIsComputer())
                {
                    bet = getBetAI(player);
                }
                else
                {
                    player.getHand().showHandCompact(true);
                    try {
                        bet = getInt("Enter bet for " + player.getName() + ": ");
                    } catch (Exception InputMismatchException) {
                        bet = 0;
                    }
                }
                if (validBet(bet, highestBet, noBets, player))
                {
                    player.setBet(bet);
                    if (player.getIsComputer())
                        System.out.println(player.getName() + " bet " + bet);
                    if (highestBet < bet)
                    {
                        highestBet = bet;
                        noBets = false;
                    }
                    break;
                }
            }
        }
        setBetIndex(checkBetWinner());
    }
    /**
     *
     * @param msg the message that should be displayed before getting the input
     * @return an integer input by the user
     */
    private int getInt(String msg)
    {
        Scanner sc = new Scanner(System.in);
        System.out.print(msg);
        return sc.nextInt();
    }
    // THE AI METHODS BELOW

    /**
     *
     * @param min the minimum value
     * @param max the maximum value (inclusive)
     * @return the random int
     */
    public int getComputerInt(int min, int max)
    {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
    public double getRandomChance()
    {
        return Math.random();
    }
    public int getBetAI(Player player)
    {
        double chance = getRandomChance();
        ArrayList<Integer> pointsInSuit = new ArrayList<>();
        for (int i = 0; i < 4; i++)
        {
            pointsInSuit.add( calculatePointsInHand(player.getHand(), i) );
        }
        int maxBet = Collections.max(pointsInSuit);
        int index = pointsInSuit.indexOf(maxBet);
        return (int) (chance * maxBet);

        /**
        if (chance > 0.5)
            return 0;
        else if (chance < 0.15)
            return getComputerInt(10, 14);
        return getComputerInt(6, 9);
         **/
    }

    // THE AI METHODS ENDS HERE

    /**
     *
     * @param bet the bet by the player
     * @param highestBet the highest bet placed
     * @param noBets true if no bets have been placed
     * @param player the player who placed the bet
     * @return boolean value whether the bet is valid or not
     */
    public boolean validBet(int bet, int highestBet, boolean noBets, Player player)
    {
        boolean isAI = player.getIsComputer();
        String msg;
        if (((bet <= highestBet) || (bet > 14)) && !(bet == 0) && !(bet == 14)) {
            msg = "Invalid bet.";
        } else if (bet == 0) {
            if (noBets && player.getIsDealer()){
                msg = "Dealer must bet!";
            } else return true;
        } else
            return true;
        if (!isAI)
            System.out.println(msg);
        return false;
    }

    /**
     *
     * @return the index of the player who won the betting.
     */
    public int checkBetWinner()
    {
        int betWinner = nextPlayerIndex(dealerIndex, 1);
        int nextIndex = betWinner;
        for (int x = 0; x < 3; x++)     // the x value is never used and is only used to count to 3
        {
            nextIndex = nextPlayerIndex(nextIndex, 1);
            if (players.get(betWinner).getBet() <= players.get( nextIndex ).getBet())
                betWinner = nextIndex;
        }
        return betWinner;
    }

    /** TODO: Make it possible to see all cards that have been placed in a subround after the last person has done an action
     *
     *
     */
    public void startRound()
    {
        List<Card> playedCards = new ArrayList<>();
        int[] score = new int[2];
        int playerIndex = betIndex;
        int firstPlayerIndex;
        boolean isFirstLoop = true;

        while (cardsCanBePlayed())
        {
            firstPlayerIndex = playerIndex;
            if (!isFirstLoop)
                displayPlayedHands();
            for (int cardsPlayed = 0; cardsPlayed < 4; cardsPlayed ++)
            {
                Player player = players.get(playerIndex);
                playedCards.add( playCard( player ) );

                // Prints only if the next player isn't a computer
                if (!players.get( nextPlayerIndex(playerIndex, 1) ).getIsComputer())
                    printPlayedCards(playedCards, firstPlayerIndex);

                playerIndex = nextPlayerIndex(playerIndex, 1);
            }
            for (int i = 0; i < 2; i++)
            {
                score[i] += calculateScore(teams.get(i), playedCards, firstPlayerIndex);
            }

            playerIndex = nextPlayerIndex(firstPlayerIndex, checkHighestCard(playedCards, suit));
            playedCards.clear();
            isFirstLoop = false;
        }
        addScore(score, teams);
        displayRoundScore(score, teams);
    }
    public void printPlayedCards(List<Card> playedCards, int index)
    {
        int playerIndex = index;
        if (!playedCards.isEmpty()) {
            System.out.println("Previous played cards:");
            for (Card playedCard : playedCards)
            {
                System.out.print(players.get(playerIndex).getName());
                if (playedCard == null)
                    System.out.print(" is cold. ");
                else
                    System.out.print(" played " + playedCard + ". ");
                playerIndex = nextPlayerIndex(playerIndex, 1);
            }
            System.out.println();
        }
    }
    public void displayPlayedHands()
    {
        Hand playedHand;
        for (Player player: players)
        {
            playedHand = player.getPlayedHand();
            playedHand.showHandCompact(false);
        }
    }

    /**
     *
     * @param hand the hand which contains the cards
     * @param suitValue the integer value for the play suit
     * @return the amount of points the hand is worth
     */
    public int calculatePointsInHand(Hand hand, int suitValue)
    {
        // Dirty hack ik
        String previousSuit = suit;
        suit = new Card(1,2).getSuitAsString(suitValue);

        int maxPoints = 0;

        for (Card card: hand.getCards())
        {
            if (validPlayingCard(card))
            {
                int cardValue = card.getValue();
                if (pointMap.containsKey(cardValue))
                    maxPoints += pointMap.get(cardValue);
            }
        }
        suit = previousSuit;
        return maxPoints;
    }

    /**
     *
     * @param team the team whose score for the round sequence should be calculated
     * @param playedCards the cards which have been played in the whole round sequence
     * @param firstPlayerIndex the index of the player who played the first card this subround
     * @return the score for the @param team.
     */
    public int calculateScore(Team team, List<Card> playedCards, int firstPlayerIndex)
    {
        int highestCardIndex = checkHighestCard(playedCards, suit);
        int score = 0;
        if (highestCardIndex == -1) return 0;

        Player highCardPlayer = players.get(nextPlayerIndex(firstPlayerIndex, highestCardIndex));
        for (int i = 0; i < playedCards.size(); i++)
        {
            Player player = players.get(nextPlayerIndex(firstPlayerIndex, i));
            Card card = playedCards.get(i);

            // Checking if the card is real and valid.
            if (card == null) continue;
            if (!validPlayingCard(card)) return 0;

            int cardValue = card.getValue();

            if (cardValue == 2) {
                if (playerInTeam(player, team))
                    score++;
            }
            else if (playerInTeam(highCardPlayer, team)) {
                if (cardIsPointCard(card)) score += pointMap.get(cardValue);
            }
        }
        return score;
    }
    public void addScore(int[] scoreArray, List<Team> teams)
    {
        for (int i = 0; i < teams.size(); i++)
        {
            Team team = teams.get(i);
            int score = scoreArray[i];
            int bet = team.getBet();
            if (bet > score)
                team.addScore(-bet);
            else
                team.addScore(score);
        }
    }

    /**
     *
     * @return boolean value whether either team has 62 or more points
     */
    public boolean checkScore()
    {
        if (teams.get(0).getScore() >= 62) return true;
        return teams.get(1).getScore() >= 62;
    }
    public boolean cardsCanBePlayed()
    {
        boolean canPlayRound = false;
        for (Player player: players)
        {
            if (player.getHand().getCardCount() != 0)
                canPlayRound = true;
        }
        return canPlayRound;
    }
    public boolean playerInTeam(Player player, Team team)
    {
        return team.getMembers().contains(player);
    }
    public boolean cardIsPointCard(Card card)
    {
        return pointMap.containsKey(card.getValue());
    }
    /**
     * @param player the player who plays one of its cards
     * @return the card that should be played.
     * returns 'null' if no valid playing card exist in hand.
     */
    public Card playCard(Player player)
    {
        Hand hand = player.getHand();
        Hand playedHand = player.getPlayedHand();
        while (playerCanPlay( hand.getCards() ))
        {
            Card playCard = decideCard(player);
            if (validPlayingCard(playCard))
            {
                hand.removeCard(playCard);

                playedHand.addCard(playCard);   // Adding the card to be displayed later

                return playCard;
            }
            if (!player.getIsComputer())
                System.out.println("Not a valid playing card.");
        }
        // Clears hand and return null when no playing card exist in hand
        hand.clear();
        return null;
    }
    public Card decideCard(Player player)
    {
        int answer;
        Hand hand = player.getHand();
        int cardsInHand = hand.getCardCount();
        if (!player.getIsComputer()) {
            System.out.println("-----Enter the number for what card you want to play-----");
            System.out.println("---------Current suit is " + suit + "---------");
            int cardIndex = 1;
            for (Card card : hand.getCards()) {
                System.out.println(cardIndex++ + ": " + card);
            }
            do {
                answer = getInt(player.getName() + " plays card ") - 1;
            } while (answer < 0 || answer >= cardsInHand);
        }
        else
            answer = getComputerInt(0, cardsInHand - 1);
        return hand.getCard(answer);
    }
    public boolean validPlayingCard(Card card)
    {
        if (card.getSuitAsString().equals(suit))
            return true;
        return (card.getValue() == 5) && sameColorFive(card);
    }
    public boolean playerCanPlay(List<Card> cards)
    {
        boolean canPlay = false;
        if (cards.isEmpty())
            return canPlay;
        for (Card card: cards)
        {
            if (validPlayingCard(card))
            {
                canPlay = true;
                return canPlay;
            }
        }
        return canPlay;
    }
    /**
     *
     * @param cards list of cards to compare
     * @param suit is the suit which was picked to play in.
     * If the suit is "", then it doesn't matter.
     * @return the index of the player with the highest card.
     * returns -1 if there are more than one of the highest card value
     *
     */
    public int checkHighestCard(List<Card> cards, String suit)
    {
        int indexOfHighestCard = 0;
        Card highestCard;
        Card otherCard;
        for (int i = 1; i < cards.size(); i++)
        {
            highestCard = cards.get(indexOfHighestCard);
            otherCard = cards.get(i);
            if (nullCheck(highestCard, otherCard))
                continue;
            else if (highestCard == null)
                highestCard = otherCard;
            if (highestCard.getValue() <= otherCard.getValue())
            {
                indexOfHighestCard = i;
            }
        }
        if (cards.get(indexOfHighestCard) == null)      // checks if the list is only filled with null objects
            return -1;

        for (int j = 0; j < cards.size() - 1; j++)
        {
            if (j == indexOfHighestCard) continue;
            Card currentCard = cards.get(j);
            if (nullCheck(cards.get(indexOfHighestCard), currentCard))
                continue;
            if ( (currentCard.getValue() == cards.get(indexOfHighestCard).getValue()))
            {
                if (suit.isEmpty()) return -1;
                if (currentCard.getSuitAsString().equals(suit)) indexOfHighestCard = j;
            }
        }
        return indexOfHighestCard;
    }
    public boolean nullCheck(Card highestCard, Card card)
    {
        if (highestCard == null && card == null)
            return true;
        return (card == null);
    }
    /**
     *
     * @param player whose hands useless cards should be removed
     */
    public void removeUselessCards(Player player)
    {
        Hand hand = player.getHand();
        List<Card> cards = new ArrayList<>(hand.getCards());
        for (Card card: cards)
        {
            /**
            String cardSuit = card.getSuitAsString();
            if (!cardSuit.equals(suit)) {
                if (player.getIsDealer() && hand.getCardCount() == 6) break;
                else if (card.getValue() != 5 || !sameColorFive(card)){
                    hand.removeCard(card);
                }
            }
             **/
            if (!validPlayingCard(card))
            {
                if (player.getIsDealer() && hand.getCardCount() == 6) break;
                hand.removeCard(card);
            }
        }
        // if the player has more than 6 cards that can be played then the lowest non point card
        // must be discarded
        if (hand.getCardCount() > 6)
        {
            // sorting the hand by value so that the lowest cards get removed first
            hand.sortByValue();

            // Updating the previous list
            cards = new ArrayList<>(hand.getCards());

            for (Card card: cards)
            {
                // Checking if the card is a point card
                if (!pointMap.containsKey(card.getValue()))
                {
                    System.out.println(player.getName() + " discarded " + card + ".");
                    hand.removeCard(card);
                }
                if (hand.getCardCount() == 6) break;
            }
        }
    }
    /**
     *
     * @param card the card to be checked
     * @return boolean value whether the card 5 is the same color
     */
    public boolean sameColorFive(Card card)
    {
        if (card.getValue() != 5) return false;
        String[] suits = {"Spades", "Clubs", "Hearts", "Diamonds"};
        List<String> blackCards = new ArrayList<>();
        List<String> redCards = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (i < 2)
                blackCards.add(suits[i]);
            else
                redCards.add(suits[i]);
        }
        if (blackCards.contains(suit)) return blackCards.contains(card.getSuitAsString());
        else return redCards.contains(card.getSuitAsString());
    }
    public void fixHands()
    {
        /**
         * The 'i' value is never used, it does three loops and afterward the dealer gets
         * the remaining cards from the deck
         */
        int index = dealerIndex;
        for (int i = 0; i < 3; i++)
        {
            index = nextPlayerIndex(index, 1);
            Player player = players.get(index);
            Hand hand = player.getHand();
            removeUselessCards(player);
            int amount = 6 - hand.getCardCount();
            fillHand(amount, player);
        }
        // The dealers turn now
        Player dealer = players.get(dealerIndex);
        fillHand(deck.cardsLeft(), dealer);
        removeUselessCards(dealer);
    }
}
