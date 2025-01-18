package project.players.ai;

import project.demo.Card;
import project.demo.Game;
import project.demo.Hand;
import project.players.Computer;
import project.players.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AI_v2 extends Computer implements AI
{
    Game game;
    int bestSuit;
    public AI_v2(String name, Game g)
    {
        super(name, g, 0);
        game = g;
        bestSuit = -1;
    }
    @Override
    public Card decideCard()
    {
        try
        {
            Hand hand = getHand();
            List<Card> cards = new ArrayList<>(hand.getCards());
            for (int i = 0; i < cards.size(); i++)
            {
                // Adds a null element in place for non-valid playing cards
                if (!game.validPlayingCard(cards.get(i)))
                {
                    cards.set(i, null);
                }
            }
            return hand.getCard(game.checkHighestCard(cards));
        } catch(RuntimeException e) {
            System.out.println("DEBUG: PLAYER HAND:\n" + getHand().getHandAsString(true));
            throw e;
        }
    }
    
    @Override
    public int decideBet()
    {
        return super.decideBet();
    }
    
    @Override
    public int decideSuit()
    {
        if (super.getBestSuit() == -1)
            throw new IllegalArgumentException("No suit value found");
        return super.getBestSuit();
    }
}
