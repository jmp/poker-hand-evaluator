package com.github.jmp.poker;

/**
 * Represents the rank of a playing card in standard deck order.
 * Ranks are ordered from lowest to highest value: TWO (0) through ACE (12).
 * This enum provides methods for converting between different representations
 * of card ranks including string notation, numeric values, and enum constants.
 */
public enum Rank {
    /** Two - lowest rank with value 0 */
    TWO(0),
    /** Three - rank with value 1 */
    THREE(1),
    /** Four - rank with value 2 */
    FOUR(2),
    /** Five - rank with value 3 */
    FIVE(3),
    /** Six - rank with value 4 */
    SIX(4),
    /** Seven - rank with value 5 */
    SEVEN(5),
    /** Eight - rank with value 6 */
    EIGHT(6),
    /** Nine - rank with value 7 */
    NINE(7),
    /** Ten - rank with value 8 */
    TEN(8),
    /** Jack - rank with value 9 */
    JACK(9),
    /** Queen - rank with value 10 */
    QUEEN(10),
    /** King - rank with value 11 */
    KING(11),
    /** Ace - highest rank with value 12 */
    ACE(12);

    private final int value;

    /**
     * Constructs a Rank with the specified numeric value.
     *
     * @param value the numeric value for this rank (0-12)
     */
    Rank(int value) {
        this.value = value;
    }

    /**
     * Returns the numeric value of this rank.
     *
     * @return the numeric value (0 for TWO, 12 for ACE)
     */
    public int getValue() {
        return value;
    }

    /**
     * Creates a Rank from its numeric value.
     *
     * @param value the numeric value (0-12)
     * @return the corresponding Rank enum constant
     * @throws IllegalArgumentException if the value is not in the valid range (0-12)
     */
    static Rank fromValue(int value) {
        return switch (value) {
            case 0 -> TWO;
            case 1 -> THREE;
            case 2 -> FOUR;
            case 3 -> FIVE;
            case 4 -> SIX;
            case 5 -> SEVEN;
            case 6 -> EIGHT;
            case 7 -> NINE;
            case 8 -> TEN;
            case 9 -> JACK;
            case 10 -> QUEEN;
            case 11 -> KING;
            case 12 -> ACE;
            default -> throw new IllegalArgumentException("Invalid value: " + value);
        };
    }

    /**
     * Returns the string representation of this rank.
     *
     * <p>Uses standard card notation: "2"-"9" for number cards,
     * "T" for Ten, "J" for Jack, "Q" for Queen, "K" for King, "A" for Ace.
     *
     * @return the string representation of this rank
     */
    @Override
    public String toString() {
        return switch (this) {
            case TWO -> "2";
            case THREE -> "3";
            case FOUR -> "4";
            case FIVE -> "5";
            case SIX -> "6";
            case SEVEN -> "7";
            case EIGHT -> "8";
            case NINE -> "9";
            case TEN -> "T";
            case JACK -> "J";
            case QUEEN -> "Q";
            case KING -> "K";
            case ACE -> "A";
        };
    }
}
