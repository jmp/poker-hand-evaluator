package com.github.jmp.poker;

import java.util.Arrays;

/**
 * Represents an immutable 5-card poker hand with evaluation capabilities.
 *
 * <p>This record provides functionality to create, validate, and evaluate poker hands
 * according to standard poker rules. Each hand must contain exactly 5 unique cards
 * from a standard 52-card deck.
 *
 * <p>The hand evaluation is based on Kevin Suffecool's 5-card hand evaluator with
 * Paul Senzee's pre-computed hash tables, providing fast and accurate poker hand
 * rankings from 1 (best possible hand) to 7462 (worst possible hand).
 *
 * @param cards the 5 cards that comprise this hand
 * @see Card
 * @see Rank
 * @see Suit
 */
public record Hand(Card... cards) {
    /**
     * Constructs a new Hand with exactly 5 unique cards.
     *
     * <p>Validates that exactly 5 cards are provided and that no duplicate
     * cards exist in the hand. Duplicate detection is based on the internal
     * card values to ensure proper poker hand validation.
     *
     * @param cards the cards to include in this hand (must be exactly 5 unique cards)
     * @throws IllegalArgumentException if cards is null, does not contain exactly 5 cards,
     *         or contains duplicate cards
     */
    public Hand {
        if (cards == null || cards.length != 5) {
            throw new IllegalArgumentException("Exactly 5 cards are required.");
        }

        final int c1 = cards[0].getValue();
        final int c2 = cards[1].getValue();
        final int c3 = cards[2].getValue();
        final int c4 = cards[3].getValue();
        final int c5 = cards[4].getValue();
        if (hasDuplicates(new int[]{c1, c2, c3, c4, c5})) {
            throw new IllegalArgumentException("Illegal hand.");
        }
    }

    /**
     * Evaluates this poker hand and returns its ranking value.
     *
     * <p>Uses Kevin Suffecool's 5-card hand evaluator algorithm with Paul Senzee's
     * pre-computed hash tables for fast and accurate evaluation. The algorithm
     * handles all poker hand types including straight flushes, four of a kind,
     * full houses, flushes, straights, three of a kind, two pair, one pair,
     * and high card hands.
     *
     * <p>The evaluation process:
     * <ol>
     * <li>Checks for flush conditions using bitwise operations</li>
     * <li>Evaluates straights and high card hands using lookup tables</li>
     * <li>Handles remaining hands (pairs, trips, etc.) using prime number products and hashing</li>
     * </ol>
     *
     * @return the ranking value of this hand as an integer between 1 and 7462,
     *         where 1 represents the best possible hand (royal flush) and 7462
     *         represents the worst possible hand (7-5-4-3-2 offsuit)
     * @throws IllegalArgumentException if the hand contains duplicate cards
     */
    public int evaluate() {
        // Binary representations of each card
        final int c1 = cards[0].getValue();
        final int c2 = cards[1].getValue();
        final int c3 = cards[2].getValue();
        final int c4 = cards[3].getValue();
        final int c5 = cards[4].getValue();

        // No duplicate cards allowed
        if (hasDuplicates(new int[]{c1, c2, c3, c4, c5})) {
            throw new IllegalArgumentException("Illegal hand.");
        }

        // Calculate index in the flushes/unique table
        final int index = (c1 | c2 | c3 | c4 | c5) >> 16;

        // Flushes, including straight flushes
        if ((c1 & c2 & c3 & c4 & c5 & 0xF000) != 0) {
            return Tables.Flushes.TABLE[index];
        }

        // Straight and high card hands
        final int value = Tables.Unique.TABLE[index];
        if (value != 0) {
            return value;
        }

        // Remaining cards
        final int product = (c1 & 0xFF) * (c2 & 0xFF) * (c3 & 0xFF) * (c4 & 0xFF) * (c5 & 0xFF);
        return Tables.Hash.Values.TABLE[hash(product)];
    }

    /**
     * Creates a new 5-card hand from its string representation.
     *
     * <p>Parses a space-separated string of card representations into a Hand object.
     * Each card in the string must follow the two-character format defined by
     * {@link Card#fromString(String)}.
     *
     * @param string the space-separated string representation of 5 cards
     * @return a new Hand instance corresponding to the parsed cards
     * @throws IllegalArgumentException if the string does not contain exactly 5 valid cards
     *         or if any individual card string is invalid
     * @see Card#fromString(String)
     */
    public static Hand fromString(String string) {
        final var parts = string.split(" ");
        final var cards = new Card[parts.length];

        var index = 0;
        for (var part : parts) {
            cards[index++] = Card.fromString(part);
        }

        return new Hand(cards);
    }

    /**
     * Returns the string representation of this hand.
     *
     * <p>Creates a space-separated string of the individual card representations,
     * in the order they were provided to the constructor.
     *
     * <p>Example outputs:
     * <ul>
     * <li>"As Kd Qh Jc Ts"</li>
     * <li>"2c 7s 9h Jd Ac"</li>
     * </ul>
     *
     * @return a space-separated string representation of all cards in this hand
     */
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        for (var i = 0; i < cards.length; i++) {
            builder.append(cards[i]);
            if (i < cards.length - 1) {
                builder.append(" ");
            }
        }

        return builder.toString();
    }

    /**
     * Checks if the given array of card values contains any duplicates.
     *
     * <p>Sorts the array and performs a linear scan to detect consecutive
     * identical values, indicating duplicate cards in the hand.
     *
     * @param values the card values to check for duplicates
     * @return {@code true} if duplicates are found, {@code false} otherwise
     */
    private static boolean hasDuplicates(int[] values) {
        Arrays.sort(values);
        for (int i = 1; i < values.length; i++) {
            if (values[i] == values[i - 1])
                return true;
        }
        return false;
    }

    /**
     * Computes a hash value for the given key using the Suffecool algorithm.
     *
     * <p>This hash function is specifically designed for poker hand evaluation,
     * mapping prime number products of card ranks to unique hash values that
     * can be used to look up hand rankings in pre-computed tables.
     *
     * <p>The algorithm applies a series of bit manipulations and uses an
     * adjustment table to ensure proper distribution and collision avoidance
     * for all possible poker hand combinations.
     *
     * @param key the prime number product of the card ranks
     * @return the computed hash value for table lookup
     */
    private static int hash(int key) {
        key += 0xE91AAA35;
        key ^= key >>> 16;
        key += key << 8;
        key ^= key >>> 4;
        return ((key + (key << 2)) >>> 19) ^ Tables.Hash.Adjust.TABLE[(key >>> 8) & 0x1FF];
    }
}
