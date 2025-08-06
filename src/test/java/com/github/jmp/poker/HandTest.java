package com.github.jmp.poker;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
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
    void testEvaluateTooFewCards() {
        assertThrows(IllegalArgumentException.class, () ->
            Hand.evaluate(new Card[]{
                new Card(Card.KING, Card.HEARTS),
                new Card(Card.QUEEN, Card.CLUBS),
                new Card(Card.JACK, Card.DIAMONDS),
                new Card(Card.TEN, Card.SPADES),
            })
        );
    }

    @Test
    void testEvaluateTooManyCards() {
        assertThrows(IllegalArgumentException.class, () ->
            Hand.evaluate(new Card[]{
                new Card(Card.KING, Card.HEARTS),
                new Card(Card.QUEEN, Card.CLUBS),
                new Card(Card.JACK, Card.DIAMONDS),
                new Card(Card.TEN, Card.SPADES),
                new Card(Card.ACE, Card.HEARTS),
                new Card(Card.EIGHT, Card.CLUBS),
            })
        );
    }

    @Test
    void testEvaluateIllegalHand() {
        assertThrows(IllegalArgumentException.class, () ->
            Hand.evaluate(new Card[]{
                new Card(Card.KING, Card.HEARTS),
                new Card(Card.KING, Card.HEARTS),
                new Card(Card.JACK, Card.DIAMONDS),
                new Card(Card.TEN, Card.SPADES),
                new Card(Card.ACE, Card.HEARTS),
            })
        );
    }

    @Test
    void testEvaluateRoyalFlush() {
        final int[] suits = {
            Card.CLUBS,
            Card.DIAMONDS,
            Card.HEARTS,
            Card.SPADES,
        };
        for (int suit : suits) {
            assertEquals(1, Hand.evaluate(new Card[]{
                new Card(Card.KING, suit),
                new Card(Card.QUEEN, suit),
                new Card(Card.JACK, suit),
                new Card(Card.TEN, suit),
                new Card(Card.ACE, suit),
            }));
        }
    }

    @Test
    void testEvaluateSevenHigh() {
        assertEquals(7462, Hand.evaluate(new Card[]{
            new Card(Card.SEVEN, Card.HEARTS),
            new Card(Card.FIVE, Card.CLUBS),
            new Card(Card.FOUR, Card.DIAMONDS),
            new Card(Card.TREY, Card.SPADES),
            new Card(Card.DEUCE, Card.HEARTS),
        }));
    }

    @Test
    void testEvaluatePair() {
        assertEquals(6185, Hand.evaluate(new Card[]{
            new Card(Card.DEUCE, Card.HEARTS),
            new Card(Card.DEUCE, Card.DIAMONDS),
            new Card(Card.TREY, Card.CLUBS),
            new Card(Card.FOUR, Card.CLUBS),
            new Card(Card.FIVE, Card.CLUBS),
        }));
    }

    @Test
    void testEvaluateAllHands() throws IOException, URISyntaxException {
        final int expectedHandCount = 2598960;

        int count = 0;
        Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("HandTestValues.txt")).toURI());
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String cardsString = line.substring(0, line.lastIndexOf(" "));
                String valueString = line.substring(line.lastIndexOf(" ") + 1);

                Card[] cards = Hand.fromString(cardsString);
                int expectedValue = Integer.parseInt(valueString);
                int evaluatedValue = Hand.evaluate(cards);

                assertEquals(expectedValue, evaluatedValue, "Evaluation of hand '" + Hand.toString(cards) + "' (parsed from '" + cardsString + "') failed.");
                ++count;
            }
        }

        assertEquals(count, expectedHandCount);
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
        final Card[] cards = Hand.fromString("Kd 5s Jc Ah Qc");

        final Card kingOfDiamonds = cards[0];
        final Card fiveOfSpades = cards[1];
        final Card jackOfClubs = cards[2];
        final Card aceOfHearts = cards[3];
        final Card queenOfClubs = cards[4];

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
        assertEquals("Kd 5s Jc Ah Qc", Hand.toString(new Card[]{
            new Card(Card.KING, Card.DIAMONDS),
            new Card(Card.FIVE, Card.SPADES),
            new Card(Card.JACK, Card.CLUBS),
            new Card(Card.ACE, Card.HEARTS),
            new Card(Card.QUEEN, Card.CLUBS),
        }));
    }
}