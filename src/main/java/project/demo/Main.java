package project.demo;

import project.GameDrive;
import project.Logger;
import project.TestType;

import java.util.Arrays;

public class Main
{
    public static void main(String[] args)
    {
        // This value is changed to what is passed in args
        boolean testRun = false;
        
        try {
            testRun = Boolean.parseBoolean(args[0]);
        } catch (Exception e){
            e.printStackTrace();
        }
        
        if (testRun)
        {
            runGameDrive();
        } else
        {
            Game g = new Game(new Logger("logs", "logging.txt"));
            g.initializeGame(true);
            g.startGame();
        }
    }
    public static void runGameDrive()
    {
        GameDrive gameDrive = new GameDrive();
        for (int i = 0; i < 10; i++)
            gameDrive.start(TestType.NONE, false);
    }
}
