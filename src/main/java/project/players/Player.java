package project.players;

import project.demo.Card;
import project.demo.Game;
import project.demo.Hand;
import project.demo.Team;

public abstract class Player
{
    private Hand hand;
    private Hand playedHand;
    private Hand bettingHand;
    private String name;
    private boolean isDealer;
    private int bet;
    private Game game;

    public Player()
    {
        //this("Bot");
    }
    /**
     * @param n the name of the player
     */
    public Player(String n, Game g)
    {
        name = n;
        hand = new Hand();
        playedHand = new Hand();
        bettingHand = new Hand();
        // What does setOwner do and why is it needed?
        hand.setOwner(n);
        playedHand.setOwner(n);
        bettingHand.setOwner(n);
        bet = 0;
        game = g;
    }

    public abstract int startBetting();
    public abstract int decideSuit();
    

    /** Setters and Getters **/
    
    /**
     * @param msg the message that should be displayed before getting the input
     * @return an integer input by the player
     */
    public abstract int getInt(String msg, int min, int max);
    public void setIsDealer(boolean dealer)
    {
        isDealer = dealer;
    }
    public void setBet(int b)
    {
        bet = b;
    }
    void setName(String name) {this.name = name;}
    public void setGame(Game g)
    {
        game = g;
    }
    public Hand getHand()
    {
        return hand;
    }
    public Hand getPlayedHand()
    {
        return playedHand;
    }
    public Hand getBettingHand()
    {
        return bettingHand;
    }
    public abstract Card decideCard();
    public String getName()
    {
        return name;
    }
    public boolean getIsDealer()
    {
        return isDealer;
    }
    public int getBet()
    {
        return bet;
    }
    public Game getGame()
    {
        return game;
    }
    public void updateBettingHand()
    {
        // Empty the old betting hand
        bettingHand.clear();
        
        // Make the betting hand more compact in logging
        bettingHand.addCards(hand.getCards());
        game.removeUselessCards(this, false);
    }
}
