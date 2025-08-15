package com.github.jmp.poker;

/**
 * Represents the four suits in a standard deck of playing cards.
 * Each suit has an associated bitmask value for efficient set operations
 * and a single character symbol for display purposes.
 */
public enum Suit {
    /** Clubs suit with bitmask value 0x8000 and symbol "c" */
    CLUBS(0x8000, "c"),
    /** Diamonds suit with bitmask value 0x4000 and symbol "d" */
    DIAMONDS(0x4000, "d"),
    /** Hearts suit with bitmask value 0x2000 and symbol "h" */
    HEARTS(0x2000, "h"),
    /** Spades suit with bitmask value 0x1000 and symbol "s" */
    SPADES(0x1000, "s");

    private final int value;
    private final String symbol;

    /**
     * Constructs a Suit with the specified bitmask value and symbol.
     *
     * @param value the bitmask value for this suit (hexadecimal power of 2)
     * @param symbol the single character symbol for this suit
     */
    Suit(int value, String symbol) {
        this.value = value;
        this.symbol = symbol;
    }

    /**
     * Returns the bitmask value of this suit.
     *
     * <p>The bitmask values are powers of 2 in hexadecimal format,
     * allowing for efficient bitwise operations when working with
     * suit combinations.
     *
     * @return the bitmask value for this suit
     */
    public int getValue() {
        return value;
    }

    /**
     * Creates a Suit from its bitmask value.
     *
     * <p>Accepts the specific hexadecimal bitmask values:
     * 0x8000 (CLUBS), 0x4000 (DIAMONDS), 0x2000 (HEARTS), 0x1000 (SPADES).
     *
     * @param value the bitmask value
     * @return the corresponding Suit enum constant
     * @throws IllegalArgumentException if the value does not correspond to a valid suit
     */
    static Suit fromValue(int value) {
        return switch (value) {
            case 0x8000 -> CLUBS;
            case 0x4000 -> DIAMONDS;
            case 0x2000 -> HEARTS;
            case 0x1000 -> SPADES;
            default -> throw new IllegalArgumentException("Invalid value: " + value);
        };
    }

    /**
     * Returns the single character symbol representation of this suit.
     *
     * <p>Returns lowercase single character symbols:
     * "c" for CLUBS, "d" for DIAMONDS, "h" for HEARTS, "s" for SPADES.
     *
     * @return the character symbol for this suit
     */
    @Override
    public String toString() {
        return symbol;
    }
}
