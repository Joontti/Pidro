package project;

import project.demo.Card;
import project.demo.Deck;
import project.demo.Game;
import project.demo.Team;
import project.players.Computer;
import project.players.Player;
import project.players.User;
import project.players.ai.AI_v1;
import project.players.ai.AI_v2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameDrive
{
    private Game game;
    public void start(TestType test, boolean controlPlayers)
    {
        game = new Game(new Logger("logs", "test.txt"));
        game.initializeGame();
        List<Player> players = new ArrayList<>();
        
        // Adding players to 'players' list
        String[] names = new String[]{"Rick Sanchez", "Walter White", "Morty Smith", "Jesse Pinkman"};
        for (int i = 0; i < 4; i++)
        {
            // Creates a User Player if the controlPlayer bool is true, else creates a Computer Player
            //players.add(controlPlayers ? new User(names[i], game) : new Computer(names[i], game));
            Player player;
            if (controlPlayers)
            {
                player = new User(names[i], game);
            }
            else
            {
                // Decide what version of AI here
                
                // The first team
                if (i % 2 == 0)
                    player = new AI_v2(names[i], game);
                // The second team
                else
                    player = new Computer(names[i], game, 0);
            }
            players.add(player);
        }
        // Adding teams to 'teams' list
        List<Player> team1 = new ArrayList<>();
        List<Player> team2 = new ArrayList<>();
        String[] teamNames = new String[]{"Rick and Morty", "Breaking Bad"};
        
        team1.add(players.get(0));
        team1.add(players.get(2));
        team2.add(players.get(1));
        team2.add(players.get(3));
        
        List<Team> teams = new ArrayList<>();
        teams.add(new Team(teamNames[0], team1));
        teams.add(new Team(teamNames[1], team2));
        
        game.setPlayers(players);
        game.setTeams(teams);
        
        
        // normal deck of cards, not shuffled
        //game.setDeck(new Deck());
        
        //game.giveCards();
        //game.sortBySuit();
        
        if (test == TestType.ALL)
            fullTest();
        else if (test == TestType.SELECTED)
            selectedTest();
        else playGame();
    }
    private void playGame()
    {
        game.startGame();
    }
    private void fullTest()
    {
        bettingTest();
        roundTest();
    }
    private void selectedTest()
    {
        loggingTest();
    }
    
    private void bettingTest()
    {
        // Rick is the dealer
        game.setDealerIndex(0);
        
        game.betting();
        Player winner = game.getPlayers().get(game.getBetIndex());
        System.out.println("Winner of betting: " + winner.getName() + " with a bet of " + winner.getBet());
    }
    private void roundTest()
    {
        // Rick is the dealer
        game.setDealerIndex(0);
        // Rick is the better
        game.setBetIndex(0);
        
        game.giveCards();
        game.setSuit(Card.getSuitAsString(Card.CLUBS));
        game.sortBySuit();
        
        game.startRound();
    }
    private void loggingTest()
    {
        Logger logger = new Logger("logs", "test.txt");
        logger.write("Hello, I am under the water! plz help!");
    }
    
}
