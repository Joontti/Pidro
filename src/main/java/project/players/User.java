package project.players;

import project.demo.Card;
import project.demo.Game;
import project.demo.Hand;

import java.util.Scanner;

public class User extends Player
{
    public User(String name, Game game)
    {
        // Runs the Player Class constructor
        super(name, game);
    }
    
    @Override
    public int startBetting()
    {
        int bet = 0;
        this.getHand().showHandCompact(true);
        bet = getInt("Enter bet for " + getName() + ": ", 0, 14);
        return bet;
    }
    
    @Override
    public int decideSuit()
    {
        getHand().showHandCompact(true);
        System.out.println("Enter one of the following digits to decide suit:");
        for (int i = 0; i < 4; i++) {
            System.out.println((i + 1) + ": " + new Card(3, i).getSuitAsString());
        }
        return getInt("Suit: ", 0, 4) - 1;
    }
    
    @Override
    public int getInt(String msg, int min, int max)
    {
        Scanner sc = new Scanner(System.in);
        System.out.print(msg);
        int ans = sc.nextInt();
        if (ans > max || ans < min)
        {
            System.out.println("Invalid number given");
            ans = -1;
        }
        return ans;
    }
    
    @Override
    public Card decideCard()
    {
        Hand hand = getHand();
        System.out.println("-----Enter the number for what card you want to play-----");
        System.out.println("---------Current suit is " + super.getGame().getSuit() + " ---------");
        int cardIndex = 1;
        int answer = 0;
        int cardsInHand = hand.getCardCount();
        for (Card card : hand.getCards())
        {
            System.out.println(cardIndex++ + ": " + card);
        } do
        {
            answer = getInt(getName() + " chooses card ", 1, cardsInHand) - 1;
        } while (answer < 0 || answer >= cardsInHand);
        return hand.getCard(answer);
    }
}

