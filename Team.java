import java.util.*;
public class Team
{
    private final String name;
    private final List<Player> members;
    private int score;
    private int bet;
    public Team(String teamName, List<Player> teamMates)
    {
         name = teamName;
         members = teamMates;
    }

    /**
     * Setters and Getters
     */
    public void addScore(int s)
    {
        score += s;
    }
    public void setBet(int b)
    {
        bet = b;
    }
    public List<Player> getMembers()
    {
        return members;
    }
    public String getName()
    {
        return name;
    }
    public int getScore()
    {
        return score;
    }
    public int getBet()
    {
        return bet;
    }
}
