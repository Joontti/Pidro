import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Player
{
    private Hand hand;
    private Hand playedHand;
    private final String name;
    private boolean isDealer;
    private final boolean isComputer;
    private int bet;

    public Player()
    {
        this("Bot");
    }
    /**
     * @param n the name of the player
     */
    public Player(String n)
    {
        this(n, false);
    }
    public Player(String n, boolean computer)
    {
        name = n;
        hand = new Hand();
        playedHand = new Hand();
        hand.setOwner(n);
        playedHand.setOwner(n);
        isComputer = computer;
        bet = 0;
    }

    public abstract void startBetting();

    /** Setters and Getters **/
    public void setIsDealer(boolean dealer)
    {
        isDealer = dealer;
    }
    public void setBet(int b)
    {
        bet = b;
    }
    public Hand getHand()
    {
        return hand;
    }
    public Hand getPlayedHand() {return playedHand;}
    public String getName()
    {
        return name;
    }
    public boolean getIsDealer()
    {
        return isDealer;
    }
    public boolean getIsComputer()
    {
        return isComputer;
    }
    public int getBet()
    {
        return bet;
    }
}
class Computer extends Player
{
    Game game;
    public Computer(Game g)
    {
        game = g;

    }
    public int getComputerInt(int min, int max)
    {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
    public double getRandomChance()
    {
        return Math.random();
    }
    public int getBetAI(Player player) {
        double chance = getRandomChance();
        ArrayList<Integer> pointsInSuit = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            pointsInSuit.add(game.calculatePointsInHand(player.getHand(), i));
        }
        int maxBet = Collections.max(pointsInSuit);
        int index = pointsInSuit.indexOf(maxBet);
        return (int) (chance * maxBet);
    }
}
class User extends Player
{

}
