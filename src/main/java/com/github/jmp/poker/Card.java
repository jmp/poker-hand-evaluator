package com.github.jmp.poker;

import static com.github.jmp.poker.Suit.SPADES;

/**
 * An immutable class representing a card from a normal 52-card deck.
 */
public class Card {
    private final int value;  // Format: xxxAKQJT 98765432 CDHSrrrr xxPPPPPP
    private final Rank rank;
    private final Suit suit;

    private static final String RANKS = "23456789TJQKA";
    private static final String SUITS = "shdc";

    /**
     * Creates a new card with the given rank and suit.
     * @param rank the rank of the card, see {@link Rank}
     * @param suit the suit of the card, see {@link Suit}
     */
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.value = (1 << (rank.getValue() + 16)) | suit.getValue() | (rank.getValue() << 8) | Tables.PRIMES[rank.getValue()];
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
        if (string == null || string.length() != 2) {
            throw new IllegalArgumentException("Card string must be non-null with length of exactly 2.");
        }

        final int rank = RANKS.indexOf(string.charAt(0));
        final int suit = SPADES.getValue() << SUITS.indexOf(string.charAt(1));

        return new Card(Rank.fromValue(rank), Suit.fromValue(suit));
    }

    /**
     * Returns the rank of the card.
     * @return Rank of the card.
     * @see Rank
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Returns the suit of the card.
     * @return Suit of the card.
     * @see Suit
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Returns a string representation of the card.
     * For example, the king of spades is "Ks", and the jack of hearts is "Jh".
     * @return a string representation of the card.
     */
    public String toString() {
        return rank.toString() + suit.toString();
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
}
