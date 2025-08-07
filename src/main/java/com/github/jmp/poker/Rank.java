package com.github.jmp.poker;

public enum Rank {
    TWO(0), THREE(1), FOUR(2), FIVE(3), SIX(4), SEVEN(5), EIGHT(6),
    NINE(7), TEN(8), JACK(9), QUEEN(10), KING(11), ACE(12);

    private final int value;

    Rank(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Rank fromString(String string) {
        return switch (string) {
            case "2" -> TWO;
            case "3" -> THREE;
            case "4" -> FOUR;
            case "5" -> FIVE;
            case "6" -> SIX;
            case "7" -> SEVEN;
            case "8" -> EIGHT;
            case "9" -> NINE;
            case "T" -> TEN;
            case "J" -> JACK;
            case "Q" -> QUEEN;
            case "K" -> KING;
            case "A" -> ACE;
            default -> throw new IllegalArgumentException("Invalid rank: " + string);
        };
    }

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
