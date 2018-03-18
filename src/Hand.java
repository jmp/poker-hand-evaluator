import java.util.Arrays;

/** Utility methods for evaluating or creating a hand of cards. */
public class Hand {
    /**
     * Poker hand evaluator based on Kevin Suffecool's 5-card hand evaluator and with Paul Senzee's pre-computed hash.
     * @param cards a hand of cards to evaluate
     * @return the value of the hand as an integer between 1 and 7462
     */
    public static int evaluate(Card[] cards) {
        // Only 5-card hands are supported
        if (cards == null || cards.length != 5)
            throw new IllegalArgumentException("Exactly 5 cards are required.");

        // Binary representations of each card
        final int c1 = cards[0].getValue();
        final int c2 = cards[1].getValue();
        final int c3 = cards[2].getValue();
        final int c4 = cards[3].getValue();
        final int c5 = cards[4].getValue();

        // No duplicate cards allowed
        if (hasDuplicates(new int[]{c1, c2, c3, c4, c5}))
            throw new IllegalArgumentException("Illegal hand.");

        // Calculate index in the flushes/unique table
        final int index = (c1 | c2 | c3 | c4 | c5) >> 16;

        // Flushes, including straight flushes
        if ((c1 & c2 & c3 & c4 & c5 & 0xF000) != 0)
            return Tables.Flushes.TABLE[index];

        // Straight and high card hands
        final int value = Tables.Unique.TABLE[index];
        if (value != 0)
            return value;

        // Remaining cards
        final int product = (c1 & 0xFF) * (c2 & 0xFF) * (c3 & 0xFF) * (c4 & 0xFF) * (c5 & 0xFF);
        return Tables.Hash.Values.TABLE[hash(product)];
    }

    /**
     * Creates a new 5-card hand from the given string.
     * @param string the string to create the hand from, such as "Kd 5s Jc Ah Qc"
     * @return a new hand as an array of cards
     */
    public static Card[] fromString(String string) {
        final String[] parts = string.split(" ");
        final Card[] cards = new Card[parts.length];

        if (parts.length != 5)
            throw new IllegalArgumentException("Exactly 5 cards are required.");

        int index = 0;
        for (String part : parts)
            cards[index++] = Card.fromString(part);

        return cards;
    }

    /**
     * Converts the given hand into concatenation of their string representations
     * @param cards a hand of cards
     * @return a concatenation of the string representations of the given cards
     */
    public static String toString(Card[] cards) {
        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < cards.length; i++) {
            builder.append(cards[i]);
            if (i < cards.length - 1)
                builder.append(" ");
        }

        return builder.toString();
    }

    /**
     * Checks if the given array of values has any duplicates.
     * @param values the values to check
     * @return true if the values contain duplicates, false otherwise
     */
    private static boolean hasDuplicates(int[] values) {
        Arrays.sort(values);
        for (int i = 1; i < values.length; i++) {
            if (values[i] == values[i - 1])
                return true;
        }
        return false;
    }

    private static int hash(int key) {
        key += 0xE91AAA35;
        key ^= key >>> 16;
        key += key << 8;
        key ^= key >>> 4;
        return ((key + (key << 2)) >>> 19) ^ Tables.Hash.Adjust.TABLE[(key >>> 8) & 0x1FF];
    }
}
