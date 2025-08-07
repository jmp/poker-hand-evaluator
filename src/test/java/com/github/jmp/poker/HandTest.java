package com.github.jmp.poker;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HandTest {
    @Test
    void testTooFewCards() {
        assertThrows(IllegalArgumentException.class, () ->
            new Hand(
                new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.QUEEN, Suit.CLUBS),
                new Card(Rank.JACK, Suit.DIAMONDS),
                new Card(Rank.TEN, Suit.SPADES)
            )
        );
    }

    @Test
    void testTooManyCards() {
        assertThrows(IllegalArgumentException.class, () ->
            new Hand(
                new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.QUEEN, Suit.CLUBS),
                new Card(Rank.JACK, Suit.DIAMONDS),
                new Card(Rank.TEN, Suit.SPADES),
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.EIGHT, Suit.CLUBS)
            )
        );
    }

    @Test
    void testNull() {
        assertThrows(IllegalArgumentException.class, () ->
            new Hand((Card[]) null)
        );
    }

    @Test
    void testLegal() {
        assertDoesNotThrow(() ->
            new Hand(
                new Card(Rank.KING, Suit.CLUBS),
                new Card(Rank.QUEEN, Suit.CLUBS),
                new Card(Rank.JACK, Suit.CLUBS),
                new Card(Rank.TEN, Suit.CLUBS),
                new Card(Rank.ACE, Suit.CLUBS)
            )
        );
    }

    @Test
    void testIllegal() {
        assertThrows(IllegalArgumentException.class, () ->
            new Hand(
                new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.JACK, Suit.DIAMONDS),
                new Card(Rank.TEN, Suit.SPADES),
                new Card(Rank.ACE, Suit.HEARTS)
            )
        );
    }

    @Test
    void testEvaluateRoyalFlush() {
        for (var suit : Suit.values()) {
            var hand = new Hand(
                new Card(Rank.KING, suit),
                new Card(Rank.QUEEN, suit),
                new Card(Rank.JACK, suit),
                new Card(Rank.TEN, suit),
                new Card(Rank.ACE, suit)
            );
            assertEquals(1, hand.evaluate());
        }
    }

    @Test
    void testEvaluateSevenHigh() {
        var hand = new Hand(
            new Card(Rank.SEVEN, Suit.HEARTS),
            new Card(Rank.FIVE, Suit.CLUBS),
            new Card(Rank.FOUR, Suit.DIAMONDS),
            new Card(Rank.THREE, Suit.SPADES),
            new Card(Rank.TWO, Suit.HEARTS)
        );
        assertEquals(7462, hand.evaluate());
    }

    @Test
    void testEvaluatePair() {
        var hand = new Hand(
            new Card(Rank.TWO, Suit.HEARTS),
            new Card(Rank.TWO, Suit.DIAMONDS),
            new Card(Rank.THREE, Suit.CLUBS),
            new Card(Rank.FOUR, Suit.CLUBS),
            new Card(Rank.FIVE, Suit.CLUBS)
        );

        assertEquals(6185, hand.evaluate());
    }

    @Test
    void testEvaluateAllHands() throws IOException, URISyntaxException {
        final var expectedHandCount = 2598960;

        var count = 0;
        var path = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("HandTestValues.txt")).toURI());
        try (var reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                var cardsString = line.substring(0, line.lastIndexOf(" "));
                var valueString = line.substring(line.lastIndexOf(" ") + 1);

                var hand = Hand.fromString(cardsString);
                var expectedValue = Integer.parseInt(valueString);
                var evaluatedValue = hand.evaluate();

                assertEquals(expectedValue, evaluatedValue);
                ++count;
            }
        }

        assertEquals(expectedHandCount, count);
    }

    @Test
    void testTooFewFromString() {
        assertThrows(IllegalArgumentException.class, () -> Hand.fromString("Kd 5s Jc Ah"));
    }

    @Test
    void testTooManyFromString() {
        assertThrows(IllegalArgumentException.class, () -> Hand.fromString("Kd 5s Jc Ah Qc Th"));
    }

    @Test
    void testInvalidFromString() {
        assertThrows(IllegalArgumentException.class, () -> Hand.fromString("Kd 5s Jc Ah Qx"));
    }

    @Test
    void testValidFromString() {
        final var cards = Hand.fromString("Kd 5s Jc Ah Qc").cards();

        final var kingOfDiamonds = cards[0];
        final var fiveOfSpades = cards[1];
        final var jackOfClubs = cards[2];
        final var aceOfHearts = cards[3];
        final var queenOfClubs = cards[4];

        assertEquals(Rank.KING, kingOfDiamonds.getRank());
        assertEquals(Suit.DIAMONDS, kingOfDiamonds.getSuit());

        assertEquals(Rank.FIVE, fiveOfSpades.getRank());
        assertEquals(Suit.SPADES, fiveOfSpades.getSuit());

        assertEquals(Rank.JACK, jackOfClubs.getRank());
        assertEquals(Suit.CLUBS, jackOfClubs.getSuit());

        assertEquals(Rank.ACE, aceOfHearts.getRank());
        assertEquals(Suit.HEARTS, aceOfHearts.getSuit());

        assertEquals(Rank.QUEEN, queenOfClubs.getRank());
        assertEquals(Suit.CLUBS, queenOfClubs.getSuit());
    }

    @Test
    void testToString() {
        var hand = new Hand(
            new Card(Rank.KING, Suit.DIAMONDS),
            new Card(Rank.FIVE, Suit.SPADES),
            new Card(Rank.JACK, Suit.CLUBS),
            new Card(Rank.ACE, Suit.HEARTS),
            new Card(Rank.QUEEN, Suit.CLUBS)
        );

        assertEquals("Kd 5s Jc Ah Qc", hand.toString());
    }
}