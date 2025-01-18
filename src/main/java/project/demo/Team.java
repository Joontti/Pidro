package project.demo;

import project.players.Player;

import java.util.ArrayList;
import java.util.List;
public class Team
{
    // This affects displayRoundScore in Game class
    private final boolean autoLog = true;
    private final String name;
    private final List<Player> members;
    private List<Integer> scoreHistory;
    private List<Integer> potentialScoreHistory;
    private int score;
    private int bet;
    public Team(String teamName, List<Player> teamMates)
    {
        scoreHistory = new ArrayList<>();
        potentialScoreHistory = new ArrayList<>();
        name = teamName;
        members = teamMates;
    }

    /**
     * Setters and Getters
     */
    public void addScore(int s)
    {
        score += s;
        
        if (autoLog)
        {
            scoreHistory.add(s);
        }
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
    public List<Integer> getScoreHistory()
    {
        return scoreHistory;
    }
    public List<Integer> getPotentialScoreHistory()
    {
        return potentialScoreHistory;
    }
    
}
