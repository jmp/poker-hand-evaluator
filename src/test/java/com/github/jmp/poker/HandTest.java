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
                new Card(Card.KING, Card.HEARTS),
                new Card(Card.QUEEN, Card.CLUBS),
                new Card(Card.JACK, Card.DIAMONDS),
                new Card(Card.TEN, Card.SPADES)
            )
        );
    }

    @Test
    void testTooManyCards() {
        assertThrows(IllegalArgumentException.class, () ->
            new Hand(
                new Card(Card.KING, Card.HEARTS),
                new Card(Card.QUEEN, Card.CLUBS),
                new Card(Card.JACK, Card.DIAMONDS),
                new Card(Card.TEN, Card.SPADES),
                new Card(Card.ACE, Card.HEARTS),
                new Card(Card.EIGHT, Card.CLUBS)
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
                new Card(Card.KING, Card.CLUBS),
                new Card(Card.QUEEN, Card.CLUBS),
                new Card(Card.JACK, Card.CLUBS),
                new Card(Card.TEN, Card.CLUBS),
                new Card(Card.ACE, Card.CLUBS)
            )
        );
    }

    @Test
    void testIllegal() {
        assertThrows(IllegalArgumentException.class, () ->
            new Hand(
                new Card(Card.KING, Card.HEARTS),
                new Card(Card.KING, Card.HEARTS),
                new Card(Card.JACK, Card.DIAMONDS),
                new Card(Card.TEN, Card.SPADES),
                new Card(Card.ACE, Card.HEARTS)
            )
        );
    }

    @Test
    void testEvaluateRoyalFlush() {
        final var suits = new int[]{Card.CLUBS, Card.DIAMONDS, Card.HEARTS, Card.SPADES};
        for (var suit : suits) {
            var hand = new Hand(
                new Card(Card.KING, suit),
                new Card(Card.QUEEN, suit),
                new Card(Card.JACK, suit),
                new Card(Card.TEN, suit),
                new Card(Card.ACE, suit)
            );
            assertEquals(1, hand.evaluate());
        }
    }

    @Test
    void testEvaluateSevenHigh() {
        var hand = new Hand(
            new Card(Card.SEVEN, Card.HEARTS),
            new Card(Card.FIVE, Card.CLUBS),
            new Card(Card.FOUR, Card.DIAMONDS),
            new Card(Card.TREY, Card.SPADES),
            new Card(Card.DEUCE, Card.HEARTS)
        );
        assertEquals(7462, hand.evaluate());
    }

    @Test
    void testEvaluatePair() {
        var hand = new Hand(
            new Card(Card.DEUCE, Card.HEARTS),
            new Card(Card.DEUCE, Card.DIAMONDS),
            new Card(Card.TREY, Card.CLUBS),
            new Card(Card.FOUR, Card.CLUBS),
            new Card(Card.FIVE, Card.CLUBS)
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

        assertEquals(Card.KING, kingOfDiamonds.getRank());
        assertEquals(Card.DIAMONDS, kingOfDiamonds.getSuit());

        assertEquals(Card.FIVE, fiveOfSpades.getRank());
        assertEquals(Card.SPADES, fiveOfSpades.getSuit());

        assertEquals(Card.JACK, jackOfClubs.getRank());
        assertEquals(Card.CLUBS, jackOfClubs.getSuit());

        assertEquals(Card.ACE, aceOfHearts.getRank());
        assertEquals(Card.HEARTS, aceOfHearts.getSuit());

        assertEquals(Card.QUEEN, queenOfClubs.getRank());
        assertEquals(Card.CLUBS, queenOfClubs.getSuit());
    }

    @Test
    void testToString() {
        var hand = new Hand(
            new Card(Card.KING, Card.DIAMONDS),
            new Card(Card.FIVE, Card.SPADES),
            new Card(Card.JACK, Card.CLUBS),
            new Card(Card.ACE, Card.HEARTS),
            new Card(Card.QUEEN, Card.CLUBS)
        );

        assertEquals("Kd 5s Jc Ah Qc", hand.toString());
    }
}