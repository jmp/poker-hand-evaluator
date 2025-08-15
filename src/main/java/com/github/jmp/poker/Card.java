package com.github.jmp.poker;

import static com.github.jmp.poker.Suit.SPADES;

/**
 * Represents an immutable playing card from a standard 52-card deck.
 *
 * <p>Each card consists of a {@link Rank} and a {@link Suit}, and maintains
 * an internal integer value for efficient poker hand evaluation. The internal
 * value uses a specific bit layout for fast comparison and evaluation operations.
 *
 * <p>Cards can be created from their rank and suit components, or parsed from
 * a two-character string representation (e.g., "Ks" for King of Spades).
 *
 * <p>String format examples:
 * <ul>
 * <li>"As" - Ace of Spades</li>
 * <li>"Kc" - King of Clubs</li>
 * <li>"Th" - Ten of Hearts</li>
 * <li>"2d" - Two of Diamonds</li>
 * </ul>
 *
 * @see Rank
 * @see Suit
 */
public class Card {
    private final int value;  // Format: xxxAKQJT 98765432 CDHSrrrr xxPPPPPP
    private final Rank rank;
    private final Suit suit;

    private static final String RANKS = "23456789TJQKA";
    private static final String SUITS = "shdc";

    /**
     * Creates a new card with the specified rank and suit.
     *
     * <p>The constructor computes an internal bit-packed value for efficient
     * poker hand evaluation operations.
     *
     * @param rank the rank of the card
     * @param suit the suit of the card
     * @throws NullPointerException if rank or suit is null
     */
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.value = (1 << (rank.getValue() + 16)) | suit.getValue() | (rank.getValue() << 8) | Tables.PRIMES[rank.getValue()];
    }

    /**
     * Creates a new Card instance from its string representation.
     *
     * <p>The string must be exactly two characters: the first character represents
     * the rank, and the second character represents the suit.
     *
     * <p>Rank characters: "2", "3", "4", "5", "6", "7", "8", "9", "T" (Ten),
     * "J" (Jack), "Q" (Queen), "K" (King), "A" (Ace)
     *
     * <p>Suit characters: "s" (Spades), "h" (Hearts), "d" (Diamonds), "c" (Clubs)
     *
     * @param string the two-character string representation (e.g., "Ks", "2h")
     * @return a new Card instance corresponding to the given string
     * @throws IllegalArgumentException if string is null, not exactly 2 characters,
     *         or contains invalid rank/suit characters
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
     * Returns the rank of this card.
     *
     * @return the rank of this card
     * @see Rank
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Returns the suit of this card.
     *
     * @return the suit of this card
     * @see Suit
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Returns the string representation of this card.
     *
     * <p>The format is a two-character string where the first character
     * represents the rank and the second represents the suit.
     *
     * <p>Examples: "Ks" (King of Spades), "Jh" (Jack of Hearts), "2c" (Two of Clubs)
     *
     * @return the string representation of this card
     */
    public String toString() {
        return rank.toString() + suit.toString();
    }

    /**
     * Returns the internal bit-packed value of this card.
     *
     * <p>The value is represented using the bit layout:
     * {@code xxxAKQJT 98765432 CDHSrrrr xxPPPPPP}
     *
     * <p>Bit breakdown:
     * <ul>
     * <li>{@code xxx} - unused bits</li>
     * <li>{@code AKQJT 98765432} - rank bits (one bit set for the card's rank)</li>
     * <li>{@code CDHS} - suit bits (Clubs, Diamonds, Hearts, Spades)</li>
     * <li>{@code rrrr} - rank value bits</li>
     * <li>{@code xx} - unused bits</li>
     * <li>{@code PPPPPP} - prime number associated with the rank</li>
     * </ul>
     *
     * <p>This encoding enables efficient poker hand evaluation and comparison operations.
     *
     * @return the bit-packed integer value representing this card
     */
    int getValue() {
        return value;
    }
}
