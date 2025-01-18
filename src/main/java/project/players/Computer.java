package project.players;

import project.demo.Card;
import project.demo.Game;
import project.demo.Hand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class Computer extends Player
{
    Game game;
    int bestSuit;
    int BETTINGVALUE;
    
    public Computer(String name, Game g)
    {
        // Runs the Player Class constructor
        super(name + (" (AI)"), g);
        game = super.getGame();
        bestSuit = -1;
        BETTINGVALUE = 4;
    }
    public Computer(String name, Game g, int value)
    {
        this(name, g);
        BETTINGVALUE = value;
    }
    public int getInt(String msg, int min, int max)
    {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
    
    @Override
    public Card decideCard()
    {
        Hand hand = getHand();
        return hand.getCard(getInt("", 0, hand.getCardCount() -1));
    }
    
    public double getRandomChance()
    {
        return Math.random();
    }
    public int decideBet() {
        double chance = getRandomChance();
        ArrayList<Integer> pointsInSuit = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            pointsInSuit.add(game.calculatePointsInHand(getHand(), i));
        }
        
        int value = BETTINGVALUE;
        
        int maxBet = Collections.max(pointsInSuit) + value;
        
        // The best suit is found by the highest value in pointsInSuit array
        bestSuit = pointsInSuit.indexOf(maxBet - value);
        
        // If the suggested bet is lower than 6 then skip
        return (int) (maxBet < 6 ? 0: (chance * maxBet));
    }
    
    @Override
    public int startBetting()
    {
        bestSuit = -1;
        return decideBet();
    }
    
    @Override
    public int decideSuit()
    {
        if (bestSuit == -1)
            throw new IllegalArgumentException("No suit value found");
        return bestSuit;
    }
    public void setBestSuit(int suit)
    {
        bestSuit = suit;
    }
    public int getBestSuit()
    {
        return bestSuit;
    }
    
}
