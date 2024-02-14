public class PlayerTestDrive
{
    Player p;

    public PlayerTestDrive()
    {
        p = new Player("Kalle");
    }

    public void start(int test)
    {
        switch (test)
        {
            case 0 -> isDealerTester();
            case 1 -> isComputerTest();
        }
    }
    public void isDealerTester()
    {
        String msg;
        msg = p.getIsDealer() ? " is Dealer": " is not Dealer";
        System.out.println(p.getName() + msg);
        p.setIsDealer(true);
        msg = p.getIsDealer() ? " is Dealer": " is not Dealer";
        System.out.println(p.getName() + msg);
    }
    public void isComputerTest()
    {
        Player p2 = new Player();
        int testCompleted = 0;
        int totalTests = 3;
        if (!p2.getName().equals("Bot"))
        {
            System.out.println("Test failed. Expected " + "Bot" + ", got " + p2.getName());
        }
        else testCompleted++;
        if (p2.getIsComputer())
        {
            System.out.println("Test failed. Expected " + false + ", got " + p2.getIsComputer());
        }
        else testCompleted++;
        Player p3 = new Player("Kalle", true);
        if (!p3.getIsComputer())
        {
            System.out.println("Test failed. Expected " + true + ", got " + p3.getIsComputer());
        }
        else testCompleted++;
        System.out.println("Completed tests: " + testCompleted + " of " + totalTests);
    }
}
