package project.demo;

/**
 * An object of type demo.Card represents a playing card from a
 * standard Poker deck.  The card has a suit, which
 * can be spades, hearts, diamonds, clubs, or joker.  A spade, heart,
 * diamond, or club has one of the 13 values: 2, 3, 4, 5, 6, 7,
 * 8, 9, 10, jack, queen, king or ace. */

public class Card
{
    public final static int SPADES = 0;   // Codes for the 4 suits
    public final static int CLUBS = 1;
    public final static int HEARTS = 2;
    public final static int DIAMONDS = 3;

                                        // Codes for the non-numeric cards.

    public final static int JACK = 11;    //   Cards 2 through 10 have their
    public final static int QUEEN = 12;   //   numerical values for their codes.
    public final static int KING = 13;
    public final static int ACE = 14;

    private static String[] suits = {"Spades", "Clubs", "Hearts", "Diamonds"};
    private static String[] ranks = {"Dummy", "Dummy", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};

    /**
     * This card's suit, one of the constants SPADES, HEARTS, DIAMONDS,
     * CLUBS, or JOKER.  The suit cannot be changed after the card is
     * constructed.
     */
    private final int suit;

    /**
     * The card's value.  For a normal card, this is one of the values
     * 2 through 14.  The value cannot be changed after the card
     * is constructed.
     */
    private final int value;

    /**
     * Creates a card with a specified suit and value.
     *
     * @param theValue the value of the new card.  For a regular card (non-joker),
     *                 the value must be in the range 2 through 14.
     *                 You can use the constants demo.Card.JACK, demo.Card.QUEEN, demo.Card.KING and demo.Card.ACE
     * @param theSuit  the suit of the new card.  This must be one of the values
     *                 demo.Card.SPADES, demo.Card.HEARTS, demo.Card.DIAMONDS, demo.Card.CLUBS, or demo.Card.JOKER.
     * @throws IllegalArgumentException if the parameter values are not in the
     *                                  permissible ranges
     */
    public Card(int theValue, int theSuit) {
        if (theSuit != SPADES && theSuit != HEARTS && theSuit != DIAMONDS &&
                theSuit != CLUBS)
            throw new IllegalArgumentException("Illegal playing card suit");
        if ((theValue < 2 || theValue > 14))
            throw new IllegalArgumentException("Illegal playing card value");
        value = theValue;
        suit = theSuit;

    }

    /**
     * Returns the suit of this card.
     *
     * @return the suit, which is one of the constants demo.Card.SPADES,
     * demo.Card.HEARTS, demo.Card.DIAMONDS, demo.Card.CLUBS, or demo.Card.JOKER
     */
    public int getSuit() {
        return suit;
    }

    /**
     * Returns the value of this card.
     *
     * @return the value, which is one of the numbers 2 through 14, inclusive for
     * a regular card.
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns a String representation of the card's suit.
     *
     * @return one of the strings "Spades", "Hearts", "Diamonds", "Clubs"
     */
    public String getSuitAsString() {
        return switch (suit) {
            case SPADES -> "Spades";
            case HEARTS -> "Hearts";
            case DIAMONDS -> "Diamonds";
            default -> "Clubs";
        };
    }
    public static String getSuitAsString(int s) {
        return suits[s];
    }

    /**
     * Returns a String representation of the card's value.
     *
     * @return for a regular card, one of the strings "2",
     * "3", ..., "10", "Jack", "Queen", "King" or "Ace".
     */
    public String getValueAsString() {
        return switch (value) {
            case 2 -> "2";
            case 3 -> "3";
            case 4 -> "4";
            case 5 -> "5";
            case 6 -> "6";
            case 7 -> "7";
            case 8 -> "8";
            case 9 -> "9";
            case 10 -> "10";
            case 11 -> "Jack";
            case 12 -> "Queen";
            case 13 -> "King";
            default -> "Ace";
        };
    }

    /**
     * Returns a string representation of this card, including both
     * its suit and its value.  Sample return values
     * are: "Queen of Hearts", "10 of Diamonds", "Ace of Spades"
     */
    public String toString() {
        return (getValueAsString() + " of " + getSuitAsString());
    }
}
