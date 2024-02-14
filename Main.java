public class Main
{
    public static void main(String[] args)
    {
        playerTest(false);
        gameTest(false);
    }
    public static void gameTest(boolean testRunGame)
    {
        if (testRunGame)
        {
            GameTestDrive test = new GameTestDrive(9);
            test.start();
        }
        else
        {
            Game g = new Game();
            g.initializeGame();
            g.startGame();
        }
    }
    public static void playerTest(boolean testRunPlayer)
    {
        if (testRunPlayer)
        {
            PlayerTestDrive test = new PlayerTestDrive();
            test.start(1);
        }
    }

}
