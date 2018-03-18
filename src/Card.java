/**
 * An immutable class representing a card from a normal 52-card deck.
 */
public class Card {
    private final int value;  // Format: xxxAKQJT 98765432 CDHSrrrr xxPPPPPP

    // Ranks
    public static final int DEUCE = 0;
    public static final int TREY = 1;
    public static final int FOUR = 2;
    public static final int FIVE = 3;
    public static final int SIX = 4;
    public static final int SEVEN = 5;
    public static final int EIGHT = 6;
    public static final int NINE = 7;
    public static final int TEN = 8;
    public static final int JACK = 9;
    public static final int QUEEN = 10;
    public static final int KING = 11;
    public static final int ACE = 12;

    // Suits
    public static final int CLUBS = 0x8000;
    public static final int DIAMONDS = 0x4000;
    public static final int HEARTS = 0x2000;
    public static final int SPADES = 0x1000;

    // Rank symbols
    private static final String RANKS = "23456789TJQKA";
    private static final String SUITS = "shdc";

    /**
     * Creates a new card with the given rank and suit.
     * @param rank the rank of the card, e.g. {@link Card#SIX}
     * @param suit the suit of the card, e.g. {@link Card#CLUBS}
     */
    public Card(int rank, int suit) {
        if (isValidRank(rank)) {
            throw new IllegalArgumentException("Invalid rank.");
        }

        if (isValidSuit(suit)) {
            throw new IllegalArgumentException("Invalid suit.");
        }

        value = (1 << (rank + 16)) | suit | (rank << 8) | Tables.PRIMES[rank];
    }

    /**
     * Create a new {@link Card} instance from the given string.
     * The string should be a two-character string where the first character
     * is the rank and the second character is the suit. For example, "Kc" means
     * the king of clubs, and "As" means the ace of spades.
     * @param string Card to create as a string.
     * @return a new {@link Card} instance corresponding to the given string.
     */
    public static Card fromString(String string) {
        if (string.length() != 2) {
            throw new IllegalArgumentException("Card string length must be exactly 2.");
        }

        final int rank = RANKS.indexOf(string.charAt(0));
        final int suit = SPADES << SUITS.indexOf(string.charAt(1));

        return new Card(rank, suit);
    }

    /**
     * Returns the rank of the card.
     * @return rank of the card as an integer.
     * @see Card#ACE
     * @see Card#DEUCE
     * @see Card#TREY
     * @see Card#FOUR
     * @see Card#FIVE
     * @see Card#SIX
     * @see Card#SEVEN
     * @see Card#EIGHT
     * @see Card#NINE
     * @see Card#TEN
     * @see Card#JACK
     * @see Card#QUEEN
     * @see Card#KING
     */
    public int getRank() {
        return (value >> 8) & 0xF;
    }

    /**
     * Returns the suit of the card.
     * @return Suit of the card as an integer.
     * @see Card#SPADES
     * @see Card#HEARTS
     * @see Card#DIAMONDS
     * @see Card#CLUBS
     */
    public int getSuit() {
        return value & 0xF000;
    }

    /**
     * Returns a string representation of the card.
     * For example, the king of spades is "Ks", and the jack of hearts is "Jh".
     * @return a string representation of the card.
     */
    public String toString() {
        char rank = RANKS.charAt(getRank());
        char suit = SUITS.charAt((int) (Math.log(getSuit()) / Math.log(2)) - 12);
        return "" + rank + suit;
    }

    /**
     * Returns the value of the card as an integer.
     * The value is represented as the bits <code>xxxAKQJT 98765432 CDHSrrrr xxPPPPPP</code>,
     * where <code>x</code> means unused, <code>AKQJT 98765432</code> are bits turned on/off
     * depending on the rank of the card, <code>CDHS</code> are the bits corresponding to the
     * suit, and <code>PPPPPP</code> is the prime number of the card.
     * @return the value of the card.
     */
    int getValue() {
        return value;
    }

    /**
     * Returns whether the given rank is valid or not.
     * @param rank rank to check.
     * @return true if the rank is valid, false otherwise.
     */
    private boolean isValidRank(int rank) {
        return rank < DEUCE || rank > ACE;
    }

    /**
     * Returns whether the given suit is valid or not.
     * @param suit suit to check.
     * @return true if the suit is valid, false otherwise.
     */
    private boolean isValidSuit(int suit) {
        return suit != CLUBS && suit != DIAMONDS && suit != HEARTS && suit != SPADES;
    }
}
