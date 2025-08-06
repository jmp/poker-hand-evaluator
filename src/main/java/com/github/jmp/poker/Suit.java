package com.github.jmp.poker;

/**
 * Represents the four suits in a standard deck of playing cards.
 * Each suit has an associated bitmask value for efficient set operations
 * and a single character symbol for display purposes.
 */
public enum Suit {
    CLUBS(0x8000, 'c'),
    DIAMONDS(0x4000, 'd'),
    HEARTS(0x2000, 'h'),
    SPADES(0x1000, 's');

    private final int value;
    private final char symbol;

    Suit(int value, char symbol) {
        this.value = value;
        this.symbol = symbol;
    }

    public int getValue() {
        return value;
    }

    public char getSymbol() {
        return symbol;
    }
}
