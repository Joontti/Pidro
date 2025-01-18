package project.demo;

import project.Logger;
import project.players.Computer;
import project.players.Player;
import project.players.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import project.players.ai.*;

public class Game
{
    public enum HAND{
        NORMAL,
        PLAYED,
        BET
    }
    private final Logger logger;
    private List<Player> players;
    private List<Team> teams;
    private Deck deck;
    private int dealerIndex;
    private int betIndex;
    private String suit;
    private final HashMap<Integer, Integer> pointMap;
    private boolean gameIsInitialized;
    
    public Game(Logger logger)
    {
        this.logger = logger;
        players = new ArrayList<>();
        teams = new ArrayList<>();
        pointMap = new HashMap<>();
    }
    
    public void initializeGame(boolean controlPlayers)
    {
        refreshDeck();
        
        // Adding values to the pointMap
        pointMap.put(2, 1);     // The card '2' has the value of 1
        pointMap.put(5, 5);     // The card '5' has the value of 5
        pointMap.put(10, 1);    // The card '10' has the value of 1
        pointMap.put(Card.JACK, 1);    // The card 'Jack' has the value of 1
        pointMap.put(Card.ACE, 1);    // The card 'Ace' has the value of 1
        
        // Adding players to 'players' list
        String[] names = new String[]{"Homer Simpsons", "Peter Griffin", "Bart Simpsons", "Stewie Griffin"};
        for (int i = 0; i < 4; i++)
        {
            // Creates a User Player if the controlPlayer bool is true, else creates a Computer Player
            //players.add(controlPlayers ? new User(names[i], this) : new Computer(names[i], this));
            Player player;
            if (controlPlayers)
            {
                player = new User(names[i], this);
            }
            else
            {
                // Use different versions of AI here:
                
                // i=0, i=2
                if (i % 2 == 0)
                    player = new Computer(names[i], this, 4);
                else
                    player = new AI_v2(names[i], this);
            }
            players.add(player);
            
            // To only create one playable character
            controlPlayers = false;
            
        }
        
        // Adding teams to 'teams' list
        List<Player> team1 = new ArrayList<>();
        List<Player> team2 = new ArrayList<>();
        String[] teamNames = new String[]{"The Simpsons", "Family Guy"};
        
        team1.add(players.get(0));
        team1.add(players.get(2));
        team2.add(players.get(1));
        team2.add(players.get(3));
        
        teams.add(new Team(teamNames[0], team1));
        teams.add(new Team(teamNames[1], team2));
        
        // Setting primary values to the indexes
        setDealerIndex(-1);
        setBetIndex(-1);
        
        // Marking game as initialized
        setGameIsInitialized(true);
    }
    
    public void startGame()
    {
        if (!gameIsInitialized) throw new RuntimeException("Game isn't initialized");
        
        boolean winners = false;
        int round = 0;
        while (!winners)
        {
            try {
                // Clearing data from previous rounds
                clearHands(HAND.PLAYED);
                clearBetsTeams(teams);
                clearBetsPlayers(players);
                
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
                pauseGame();
                startRound();
                displayScore();
                winners = checkScore(62);
                logBetData();
                pauseGame();
                
            } catch (IllegalStateException e)
            {
                System.out.println("Deck ran out of cards. Restarting the round.");
                round--;
                
                // Errors occur if the hands are not cleared
                clearHands(HAND.NORMAL);
            }
        }
        Team winner = decideWinner(teams);
        logGameData();
        displayWinner(winner);
        setGameIsInitialized(false);
    }
    
    
    // ---------- TESTING SETTERS AND GETTERS ----------
    /**
     * Used for testing purposes
     *
     * @param people a list of players used for the game
     */
    public void setPlayers(List<Player> people)
    {
        players = people;
    }
    
    /**
     * Used for testing purposes
     * @param teams
     */
    public void setTeams(List<Team> teams)
    {
        this.teams = teams;
    }
    public void setDeck(Deck d)
    {
        deck = d;
    }
    
    
    // ----------- TESTING SETTERS END HERE -------------
    
    
    /**
     *
     * @param player
     * @return the team of the player
     */
    public Team playerTeam(Player player)
    {
        if (player == null)
            throw new RuntimeException("Error: Cannot find team of null Player.");
        
        if (playerInTeam(0, players.indexOf(player)))
            return teams.get(0);
        
        else if (playerInTeam(1, players.indexOf(player)))
            return teams.get(1);
        
        else throw new RuntimeException("Error: Cannot find the team of" + player.getName() + ".");
    }
    
    private void setGameIsInitialized(boolean bool)
    {
        gameIsInitialized = bool;
    }
    
    public void setDealerIndex(int i)
    {
        if (!(i == -1))
        {
            clearIsDealer();
            players.get(i).setIsDealer(true);
        }
        dealerIndex = i;
    }
    
    public void setBetIndex(int i)
    {
        if (!(i == -1))
        {
            // Sets the bet index for the team.
            if (playerInTeam(0, i))
                teams.get(0).setBet(players.get(i).getBet());
            else
                teams.get(1).setBet(players.get(i).getBet());
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
    
    public String getSuit()
    {
        return suit;
    }
    
    public boolean playerInTeam(int teamIndex, int playerIndex)
    {
        return teams.get(teamIndex).getMembers().contains(players.get(playerIndex));
    }
    
    public void clearIsDealer()
    {
        for (Player player : players)
        {
            player.setIsDealer(false);
        }
    }
    
    public void clearBetsPlayers(List<Player> players)
    {
        for (Player player : players)
        {
            player.setBet(0);
        }
    }
    
    public void clearBetsTeams(List<Team> teams)
    {
        for (Team team : teams)
        {
            team.setBet(0);
        }
    }
    
    public void clearHands(HAND hand)
    {
        for (Player player : players)
        {
            Hand h;
            switch(hand) {
                case PLAYED -> h = player.getPlayedHand();
                case NORMAL -> h = player.getHand();
                case BET -> h = player.getBettingHand();
                default -> throw new RuntimeException("No such hand found.");
            }
            h.getCards().clear();
        }
    }
    public void initializeGame()
    {
        initializeGame(true);
    }
    
    /**
     * Creates a new deck and shuffles it
     */
    public void refreshDeck()
    {
        deck = new Deck();
        deck.shuffle();
    }
    
    /**
     * Checks if there are no human controlled players in the game
     * @return true if there's only computer controlled players, else false
     */
    public boolean computersOnly()
    {
        return !players.stream().map(Player::getClass).toList().contains(User.class);
    }
    
    /**
     * Pauses the game if the game consists of NOT only computer controlled players
     */
    public void pauseGame()
    {
        if (!computersOnly())
            getUserInput("Press ENTER to continue");
    }
    
    /**
     * Decides which team won the game.
     * @param teams
     * @return the winning team
     */
    public Team decideWinner(List<Team> teams)
    {
        Team team1 = teams.get(0);
        Team team2 = teams.get(1);
        
        int score1 = team1.getScore();
        int score2 = team2.getScore();
        
        if (score1 >= 62 && score2 >= 62)
        {
            if (team1.getBet() > 0)
                return team1;
        } else if (score1 > score2)
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
        for (Team team : teams)
        {
            System.out.println("Score of " + team.getName() + ": " + team.getScore());
        }
    }
    
    public void displayRoundScore(List<Team> teams)
    {
        for (Team team: teams)
        {
                String msg = team.getName();
                int points;
                try {
                    points = Math.abs(team.getScoreHistory().getLast());
                } catch(NullPointerException e) {
                    System.out.println("Error when trying to get ScoreHistory. " +
                            "There's probably a problem in the addScore method in the Team Class");
                    throw e;
                }
                msg += points < 0 ? " lost " : " gained ";
                msg += points + " points.";
                
                System.out.println(msg);
        }
    }
    
    public void decideFirstDealer()
    {
        List<Card> cards = new ArrayList<>();
        int index;
        while (true)
        {
            for (Player player : players)
            {
                Card card = deck.drawCard();
                cards.add(card);
                System.out.println(player.getName() + " got " + card.toString() + ".");
            }
            setSuit("");
            index = checkHighestCard(cards);
            if (index != -1) break;
            cards.clear();                              // if there are multiple attempts then this is needed
        }
        Player dealer = players.get(index);
        setDealerIndex(index);
        System.out.println("The first dealer is " + dealer.getName() + "!");
    }
    
    public void decideDealer()
    {
        if (dealerIndex != -1)
            setDealerIndex(nextPlayerIndex(dealerIndex, 1));
        
        else decideFirstDealer();
    }
    
    public void decideSuit()
    {
        Player player = players.get(betIndex);
        String suit = Card.getSuitAsString(player.decideSuit());
        String msg = player.getName() + " chooses " + suit;
        
        System.out.println(msg);
        setSuit(suit);
        
        // Used in logging to only display important cards
        player.updateBettingHand();
        
        // Debug
        // System.out.println(suit);
    }
    
    int nextPlayerIndex(int index, int steps)
    {
        return (index + steps) % 4;
    }
    
    /**
     * Draws 3 cards for each player 3 times (total 9) and adds them to their hand
     */
    public void giveCards()
    {
        for (int i = 0; i < 3; i++)
        {
            for (Player player : players)
            {
                player.getHand().addCards(deck.drawCards(3));
            }
        }
    }
    
    /**
     * This function checks also if the amount is correct, between 0 and 6.
     *
     * @param amount the amount of cards to be added to hand
     * @param player the player whose hand will receive the cards
     */
    public void fillHand(int amount, Player player)
    {
        if (deck.cardsLeft() < amount)
        {
            throw new IllegalStateException("Not enough cards in the deck.");
        }
        Hand hand = player.getHand();
        if (!player.getIsDealer())
            System.out.println(player.getName() + " requested " + amount + " cards.");
        
        if (amount == 1)
        {
            hand.addCard(deck.drawCard());
        } else if ((1 < amount && amount < 7) || player.getIsDealer())
        {
            hand.addCards(deck.drawCards(amount));
        } else if (amount != 0)
        {
            System.out.println("Invalid card amount.");
        }
    }
    
    public void sortBySuit()
    {
        for (Player player : players)
        {
            player.getHand().sortBySuit();
        }
    }
    
    /**
     *
     */
    public void betting()
    {
        // Print the welcome text only if there's a human among us
        if (!computersOnly())
            System.out.println("Welcome to the betting round. Enter an integer between 6 and 14" +
                " if you want to bet. Enter 0 if you want to skip.");
        boolean noBets = true;
        int index = dealerIndex;
        int highestBet = 5;             // will change in the for loop
        for (int _i = 0; _i < 4; _i++)
        {
            index = nextPlayerIndex(index, 1);
            Player player = players.get(index);
            int bet;
            int attempts = 0;
            while (true)
            {
                attempts += 1;
                if (attempts < 3)
                {
                    bet = player.startBetting();
                } else
                {
                    // If the player tries too many times, auto assign the bet to either 0 or 6.
                    bet = (noBets && player.getIsDealer()) ? 6: 0;
                    /* TODO REMOVE IF EVERYTHING IS OK
                    if (noBets && player.getIsDealer())
                        bet = 6;
                    else bet = 0;
                    
                     */
                }
                
                if (validBet(bet, highestBet, noBets, player))
                {
                    player.setBet(bet);
                    
                    // Print the bet information
                    String text = player.getName();
                    text += (bet == 0) ? " skipped.": " bet " + bet;
                    System.out.println(text);
                    
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
     * @param bet        the bet by the player
     * @param highestBet the highest bet placed
     * @param noBets     true if no bets have been placed
     * @param player     the player who placed the bet
     * @return boolean value whether the bet is valid or not
     */
    public boolean validBet(int bet, int highestBet, boolean noBets, Player player)
    {
        String msg;
        if (((bet <= highestBet) || (bet > 14)) && !(bet == 0) && !(bet == 14))
        {
            msg = "Invalid bet.";
        } else if (bet == 0)
        {
            if (noBets && player.getIsDealer())
            {
                msg = "Dealer must bet!";
            } else return true;
        } else
            return true;
        
        // Prints out the error message if the player isn't a computer
        if (playerIsHuman(player))
            System.out.println(msg);
        return false;
    }
    
    /**
     * @return the index of the player who won the betting.
     */
    public int checkBetWinner()
    {
        int betWinner = nextPlayerIndex(dealerIndex, 1);
        int nextIndex = betWinner;
        for (int _x = 0; _x < 3; _x++)     // the x value is never used and is only used to count to 3
        {
            nextIndex = nextPlayerIndex(nextIndex, 1);
            if (players.get(betWinner).getBet() <= players.get(nextIndex).getBet())
                betWinner = nextIndex;
        }
        return betWinner;
    }
    
    /**
     * TODO: Make it possible to see all cards that have been placed in a subround after the last person has done an action
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
            /*
            if (!computersOnly() && !isFirstLoop)
                displayPlayedHands();
                
             */
            
            for (int cardsPlayed = 0; cardsPlayed < 4; cardsPlayed++)
            {
                Player player = players.get(playerIndex);
                
                // Print previous cards if the player is controlled by a human
                // Otherwise you cannot see what cards have been played
                /*
                if (playerIsHuman(player))
                    printPlayedCards(playedCards, firstPlayerIndex);
                    
                 */
                
                playedCards.add(playCard(player));
                
                // Change player to the next player in order
                playerIndex = nextPlayerIndex(playerIndex, 1);
            }
            // Calculate the subround score for both teams
            for (int i = 0; i < 2; i++)
            {
                score[i] += calculateScore(teams.get(i), playedCards, firstPlayerIndex);
            }
            
            pauseGame();
            printPlayedCards(playedCards, playerIndex);
            
            playerIndex = nextPlayerIndex(firstPlayerIndex, checkHighestCard(playedCards));
            playedCards.clear();
            isFirstLoop = false;
        }
        addScore(score, teams);
        displayRoundScore(teams);
        
    }
    
    public void printPlayedCards(List<Card> playedCards, int index)
    {
        int playerIndex = index;
        if (!playedCards.isEmpty())
        {
            System.out.println("\nPlayed cards:");
            for (Card playedCard : playedCards)
            {
                String text = players.get(playerIndex).getName();
                text += playedCard == null ? " is cold.": " played " + playedCard + ".";
                System.out.println(text);
                
                playerIndex = nextPlayerIndex(playerIndex, 1);
            }
            System.out.println();
        }
    }
    
    public void displayPlayedHands()
    {
        for (Player player : players)
        {
            player.getPlayedHand().showHandCompact(false);
        }
    }
    
    /**
     * @param team             the team whose score for the round sequence should be calculated
     * @param playedCards      the cards which have been played in the whole round sequence
     * @param firstPlayerIndex the index of the player who played the first card this subround
     * @return the score for the @param team.
     */
    public int calculateScore(Team team, List<Card> playedCards, int firstPlayerIndex)
    {
        int highestCardIndex = checkHighestCard(playedCards);
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
            
            if (cardValue == 2)
            {
                if (playerInTeam(player, team))
                    score++;
            } else if (playerInTeam(highCardPlayer, team))
            {
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
            int gainedScore = bet > score ? -bet: score;
            team.addScore(gainedScore);
            
            // Logs the potential score
            team.getPotentialScoreHistory().add(score);
        }
    }
    
    /**
     * Determinate if any team is over or equal to a certain score
     * @param score the value to compare to
     * @return boolean value whether either team has more or equal to a certain score
     */
    public boolean checkScore(int score)
    {
        if (teams.get(0).getScore() >= score) return true;
        return teams.get(1).getScore() >= score;
    }
    
    public boolean cardsCanBePlayed()
    {
        boolean canPlayRound = false;
        for (Player player : players)
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
        while (playerCanPlay(hand.getCards()))
        {
            Card playCard = player.decideCard();
            if (validPlayingCard(playCard))
            {
                System.out.println(player.getName() + " plays " + playCard);
                hand.removeCard(playCard);
                
                playedHand.addCard(playCard);   // Adding the card to be displayed later
                
                return playCard;
            }
            // Print error so the human knows it isn't valid
            if (playerIsHuman(player))
                System.out.println("Not a valid playing card.");
        }
        // Clears hand and return null when no playing card exist in hand
        hand.clear();
        return null;
    }
    
    public boolean validPlayingCard(Card card)
    {
        if (card == null)
            return false;
        if (suit.isBlank())
            throw new IllegalArgumentException("Suit must have a string value");
        if (card.getSuitAsString().equals(suit))
            return true;
        return (card.getValue() == 5) && sameColorFive(card);
    }
    
    public boolean playerCanPlay(List<Card> cards)
    {
        boolean canPlay = false;
        if (cards.isEmpty())
            return canPlay;
        for (Card card : cards)
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
     * Checks if there is a highest card in a card list in a given suit.
     * @param cards list of cards to compare
     * @return the index of the player with the highest card.
     * returns -1 if there are more than one of the highest card value
     */
    public int checkHighestCard(List<Card> cards)
    {
        int indexOfHighestCard = 0;
        Card highestCard;
        Card otherCard;
        for (int i = 1; i < cards.size(); i++)
        {
            highestCard = cards.get(indexOfHighestCard);
            otherCard = cards.get(i);
            
            // Checks if
            /*
            if (!suit.isEmpty()
                && (!validPlayingCard(highestCard)
                    || !validPlayingCard(otherCard)))
            {
                throw new IllegalArgumentException("Cards must be valid playingCards if suit is not empty");
            }
            */
            // If player is cold, to avoid NullPointerException
            if (otherCard == null)
                continue;
            if (highestCard == null)
                highestCard = otherCard;
            
            if (highestCard.getValue() <= otherCard.getValue())
            {
                indexOfHighestCard = i;
            }
        }
        if (cards.get(indexOfHighestCard) == null)      // checks if the list is only filled with null objects
            return -1;
        
        // Loops a second time to see if there are multiple cards with the same value as highestCard
        for (int j = 0; j < cards.size(); j++)
        {
            // Skips iteration if the index is the same as HighestCard
            if (j == indexOfHighestCard) continue;
            
            Card currentCard = cards.get(j);
            if (currentCard == null)
                continue;
            
            // Checks if the current and highest card is the same value
            if ((currentCard.getValue() == cards.get(indexOfHighestCard).getValue()))
            {
                // If dealer is decided by this comparison (a new game has begun)
                if (suit.isEmpty())
                    return -1;
                
                try
                {
                    // Only a card with value 5 can occur here because no other value cards can coexist in a pidro game
                    if (currentCard.getValue() != 5)
                    {
                        System.out.println(cards);
                        throw new RuntimeException("Error occurred when calculating highest card");
                    }
                } catch (RuntimeException e) {
                    throw e;
                }
                // Sets the highestCard index to currentCard index if the suit matches currentCard
                if (currentCard.getSuitAsString().equals(suit))
                    indexOfHighestCard = j;
            }
        }
        return indexOfHighestCard;
    }
    
    public int calculatePointsInHand(Hand hand, int suitValue)
    {
        // Dirty hack ik
        String previousSuit = suit;
        suit = Card.getSuitAsString(suitValue);
        
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
     * @param player whose hands useless cards should be removed
     * @param removeExtraCards decides if the playable card amount in hand is max 6. If the value is false then
     * additional valid cards won't be removed (used in logging purposes).
     */
    public void removeUselessCards(Player player, boolean removeExtraCards)
    {
        // Because the parameter is a player instead of hand. The betting hand, which is used in logging will
        // be the only scenario in which removeExtraCards is false. If this changes in the future then fuck me.
        Hand hand = removeExtraCards ? player.getHand(): player.getBettingHand();
        List<Card> cards = new ArrayList<>(hand.getCards());
        for (Card card : cards)
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
                // The dealer shouldn't discard cards if doing so would make the card amount lower than 6
                // Because the dealer doesn't get any more cards at this late stage in the game
                if (removeExtraCards && player.getIsDealer() && hand.getCardCount() == 6) break;
                hand.removeCard(card);
            }
        }
        // if the player has more than 6 cards that can be played then the lowest non point card
        // must be discarded
        if ((removeExtraCards) && hand.getCardCount() > 6)
        {
            // sorting the hand by value so that the lowest cards get removed first
            hand.sortByValue();
            
            // Updating the previous list
            cards = new ArrayList<>(hand.getCards());
            
            for (Card card : cards)
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
     * @param card the card to be checked
     * @return boolean value whether the card 5 is the same color
     */
    public boolean sameColorFive(Card card)
    {
        if (card.getValue() != 5) return false;
        String[] suits = {"Spades", "Clubs", "Hearts", "Diamonds"};
        List<String> blackCards = new ArrayList<>();
        List<String> redCards = new ArrayList<>();
        for (int i = 0; i < 4; i++)
        {
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
        try {
            int index = dealerIndex;
            for (int i = 0; i < 3; i++)
            {
                index = nextPlayerIndex(index, 1);
                Player player = players.get(index);
                Hand hand = player.getHand();
                removeUselessCards(player, true);
                int amount = 6 - hand.getCardCount();
                fillHand(amount, player);
            }
            // The dealers turn now
            Player dealer = players.get(dealerIndex);
            fillHand(deck.cardsLeft(), dealer);
            removeUselessCards(dealer, true);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }
    public Team decideRoundWinner()
    {
        Team team1 = teams.get(0);
        Team team2 = teams.get(1);
        
        int score1 = team1.getScoreHistory().getLast();
        int score2 = team2.getScoreHistory().getLast();
        
        if (score1 > score2)
            return team1;
        else if (score2 > score1)
            return team2;
        else return null;
    }
    private String getUserInput(String msg)
    {
        Scanner sc = new Scanner(System.in);
        System.out.print(msg);
        return sc.nextLine();
    }
    
    private boolean playerIsHuman(Player player) {
        return (player.getClass().equals(User.class));
    }
    
    /**
     * Logs data of the bettor to the Logging class defined location
     */
    public void logBetData()
    {
        if (betIndex == -1)
            throw new RuntimeException("Cannot log data when no bettingIndex exists.");
        
        Team roundWinner = decideRoundWinner();
        String winnerText = roundWinner == null ? "DRAW": roundWinner.getName();
        Player p = players.get(betIndex);
        
        logger.write("Round winner: " + winnerText
                + "\nBetting Team: " + playerTeam(p).getName()
                + "\nBet: " + p.getBet() + ", Bettor: " + p.getName()
                + ", Dealer: " + p.getIsDealer() + ", Suit: " + suit
                + "\nPotential points: " + playerTeam(p).getPotentialScoreHistory().getLast()
                + ", Actual points: " + playerTeam(p).getScoreHistory().getLast()
                + "\n" + p.getBettingHand().getHandAsString(true));
    }
    public void logGameData()
    {
        Logger l = new Logger("logs", "game.txt");
        l.write("Game winner: " + decideWinner(teams).getName()
                + "\nScore of team " + teams.get(0).getName() + ": " + teams.get(0).getScore()
                + "\nScore of team " + teams.get(1).getName() + ": " + teams.get(1).getScore() + '\n');
    }
}
