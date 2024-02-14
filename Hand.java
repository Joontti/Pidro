import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Hand
{
    private List<Card> cards; // The cards in the hand
    private String owner;
    public Hand() {
        cards = new LinkedList<>();
    }

    /**
     * Clears the hand from all cards
     */
    public void clear() {
        cards.clear();
    }

    /**
     * Adds a card to the hand
     * @param c the card to be added
     */
    public void addCard(Card c) {
        if(c == null)
            throw new NullPointerException("Can't add a null card to a hand.");
        cards.add(c);
    }

    /**
     * Adds a list of cards to the hand
     * @param c the list of cards to add
     */
    public void addCards(List<Card> c) {
        if(c == null)
            throw new NullPointerException("Can't add a null card to a hand.");
        cards.addAll(c);
    }

    /**
     * Removes a specific card from the hand
     * @param c the card to remove
     * @return the card that was removed
     */
    public Card removeCard(Card c) {
        Iterator<Card> it = cards.iterator();
        Card removed = null;
        while(it.hasNext()) {
            removed = it.next();
            if(removed.equals(c))
                break;
        }
        cards.remove(c);
        return removed;
    }

    /**
     * Removes a card by its index
     * @param index the index in the hand for the card to remove
     * @return the card removed
     */
    public Card removeCard(int index) {
        if(index < 0 || index >= cards.size())
            throw new IllegalArgumentException("Position does not exist in hand: " + index);
        return cards.remove(index);
    }

    /**
     * Returns a card at a specific index
     * @param index the index for the card to return
     * @return the card at the index
     */
    public Card getCard(int index) {
        if(index < 0 || index >= cards.size())
            throw new IllegalArgumentException("Position does not exist in hand: " + index);
        return cards.get(index);
    }

    /**
     * Returns the number of cards in the hand
     * @return the number of cards
     */
    public int getCardCount() {
        return cards.size();
    }

    /**
     * Sorts the hand by the card suits
     */
    public void sortBySuit() {
        LinkedList<Card> newHand = new LinkedList<>();
        while (cards.size() > 0) {
            int pos = 0;  // Position of minimal card.
            Card c = cards.get(0);  // Minimal card.
            for (int i = 1; i < cards.size(); i++) {
                Card c1 = cards.get(i);
                if ( c1.getSuit() < c.getSuit() ||
                        (c1.getSuit() == c.getSuit() && c1.getValue() < c.getValue()) ) {
                    pos = i;
                    c = c1;
                }
            }
            cards.remove(pos);
            newHand.add(c);
        }
        cards = newHand;
    }

    /**
     * Sorts the hand by the card values
     */
    public void sortByValue() {
        LinkedList<Card> newHand = new LinkedList<Card>();
        while (cards.size() > 0) {
            int pos = 0;  // Position of minimal card.
            Card c = cards.get(0);  // Minimal card.
            for (int i = 1; i < cards.size(); i++) {
                Card c1 = cards.get(i);
                if ( c1.getValue() < c.getValue() ||
                        (c1.getValue() == c.getValue() && c1.getSuit() < c.getSuit()) ) {
                    pos = i;
                    c = c1;
                }
            }
            cards.remove(pos);
            newHand.add(c);
        }
        cards = newHand;
    }

    /**
     * Prints out the cards in the hand to the console
     */
    public void showHand() {
        System.out.println("The hand of " + owner + ": ");
        for(Card c: cards) {
            System.out.println(c);
        }
        System.out.println();
    }
    public void showHandCompact(boolean isNormalHand)
    {
        if (!cards.isEmpty()) {
            String playCards = cards.get(0).toString();
            for (int i = 1; i < cards.size(); i++) {
                playCards = playCards + ", " + cards.get(i).toString();
            }
            if (isNormalHand) {
                System.out.print("The hand of " + owner + ": ");
            }
            else System.out.print(owner + " has played: ");
            System.out.println(playCards);
        }
        else System.out.println("The hand of " + owner + " is empty.");
    }
    public void setOwner(String name)
    {
        owner = name;
    }
    /**
     * Returns a String representation of all the cards in the hand
     * @param enumerated if true, the String contents will be enumerated
     * @return the String representation of the hand
     */
    public String getHandAsString(boolean enumerated) {
        StringBuffer buf = new StringBuffer();
        int i=1;
        for(Card c: cards) {
            if(enumerated) {
                buf.append("[" + i + "]:    " + c + "\n");
                i++;
            } else {
                buf.append(c + "\n");
            }

        }
        return buf.toString();
    }

    /**
     * Returns all the cards in the hand as a list
     * @return the list of cards in the hand
     */
    public List<Card> getCards(){
        return cards;
    }
    /**
     * Returns a String representation of all the cards in the hand,
     * which is not enumerated
     */
    public String toString() {
        return getHandAsString(false);
    }
}