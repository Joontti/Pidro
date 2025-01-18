package project.players.ai;

import project.demo.Card;
import project.demo.Game;
import project.demo.Hand;
import project.players.Computer;

public class AI_v1 extends Computer implements AI
{
    
    public AI_v1(String name, Game g)
    {
        super(name, g);
    }
    
    @Override
    public Card decideCard()
    {
        return super.decideCard();
    }
    
    @Override
    public int decideBet()
    {
        int bet = super.getInt("", 0, 14);
        
        // If the suggested bet is lower than 6 then skip
        return bet < 6 ? 0: bet;
    }
    
    @Override
    public int decideSuit()
    {
        return super.getInt("", 0, 3);
    }
}
