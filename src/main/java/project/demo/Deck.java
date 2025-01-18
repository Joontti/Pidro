package project.demo;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class Deck {

    private LinkedList<Card> cards;

    /**
     * Creates the deck and puts all cards. The deck is not shuffled after the
     * object has been created.
     */
    public Deck() {
        cards = new LinkedList<>();

        for(int suit = 0; suit < 4; suit++) {
            for(int v = 2; v <= 14; v++) {
                cards.add(new Card(v, suit));
            }
        }


    }

    /**
     * Shuffles the deck
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Returns the number of cards left in the deck
     * @return the number of cards left in the deck
     */
    public int cardsLeft() {
        return cards.size();
    }

    /**
     * Removes a card from the deck and returns it
     * @return the card drawn
     */
    public Card drawCard() {
        if (cardsLeft() == 0)
            throw new IllegalStateException("Not enough cards in the deck.");
        return cards.removeFirst();
    }

    /**
     * Draws a number of cards and returns them as a List
     * @param amount the number of cards to draw
     * @return the cards drawn
     */
    public List<Card> drawCards(int amount){
        List<Card> draw = new LinkedList<>();
        for(int i=0;i<amount;i++) {
            draw.add(this.drawCard());
        }
        return draw;
    }
    
    /**
     * ONLY TO BE USED IN TESTING
     * @param cards the cards that the deck should only contain.
     */
    public void setCards(LinkedList<Card> cards)
    {
        this.cards = cards;
    }
}