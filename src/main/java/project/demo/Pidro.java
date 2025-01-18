package project.demo;

/**
public class Pidro
{
    public static int calculateCardPoints(List<Card> cards, int suitValue)
    {
        // Dirty hack ik
        String previousSuit = suit;
        suit = new Card(1,2).getSuitAsString(suitValue);
        
        int maxPoints = 0;
        
        for (Card card: hand.getCards())
        {
            if (validPlayingCard(card))
            {
                int cardValue = card.getValue();
                if (pointMap.containsKey(cardValue))
                    maxPoints += pointMap.get(cardValue);
            }
        }
        suit = previousSuit;
        return maxPoints;
    }
    public static boolean validPlayingCard(Card card, int suit)
    {
        if (card.getSuit() == suit)
            return true;
        return (card.getValue() == 5) && sameColorFive(card);
    }
    private static boolean sameColorFive(Card card)
    {
        return false;
    }
}
**/