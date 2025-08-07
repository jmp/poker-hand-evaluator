package com.github.jmp.poker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CardTest {
    @Test
    void testSmoke() {
        new Card(Rank.KING, Suit.DIAMONDS);
    }

    @Test
    void testGetRank() {
        assertEquals(Rank.KING, new Card(Rank.KING, Suit.DIAMONDS).getRank());
        assertEquals(Rank.FIVE, new Card(Rank.FIVE, Suit.SPADES).getRank());
        assertEquals(Rank.JACK, new Card(Rank.JACK, Suit.CLUBS).getRank());
        assertEquals(Rank.SIX, new Card(Rank.SIX, Suit.HEARTS).getRank());
        assertEquals(Rank.NINE, new Card(Rank.NINE, Suit.DIAMONDS).getRank());
    }

    @Test
    void testGetSuit() {
        assertEquals(Suit.DIAMONDS, new Card(Rank.KING, Suit.DIAMONDS).getSuit());
        assertEquals(Suit.SPADES, new Card(Rank.FIVE, Suit.SPADES).getSuit());
        assertEquals(Suit.CLUBS, new Card(Rank.JACK, Suit.CLUBS).getSuit());
        assertEquals(Suit.HEARTS, new Card(Rank.SIX, Suit.HEARTS).getSuit());
    }

    @Test
    void testGetValue() {
        assertEquals(0b00001000000000000100101100100101, new Card(Rank.KING, Suit.DIAMONDS).getValue());
        assertEquals(0b00000000000010000001001100000111, new Card(Rank.FIVE, Suit.SPADES).getValue());
        assertEquals(0b00000010000000001000100100011101, new Card(Rank.JACK, Suit.CLUBS).getValue());
    }

    @Test
    void testFromString() {
        final Card kingOfDiamonds = Card.fromString("Kd");
        assertEquals(Rank.KING, kingOfDiamonds.getRank());
        assertEquals(Suit.DIAMONDS, kingOfDiamonds.getSuit());

        final Card fiveOfSpades = Card.fromString("5s");
        assertEquals(Rank.FIVE, fiveOfSpades.getRank());
        assertEquals(Suit.SPADES, fiveOfSpades.getSuit());

        final Card jackOfClubs = Card.fromString("Jc");
        assertEquals(Rank.JACK, jackOfClubs.getRank());
        assertEquals(Suit.CLUBS, jackOfClubs.getSuit());
    }

    @Test
    void testFromStringInvalid() {
        assertThrows(IllegalArgumentException.class, () -> Card.fromString("Kx"));
        assertThrows(IllegalArgumentException.class, () -> Card.fromString("Xd"));
        assertThrows(IllegalArgumentException.class, () -> Card.fromString("Xx"));
    }

    @Test
    void testFromStringNull() {
        assertThrows(IllegalArgumentException.class, () -> Card.fromString(null));
    }

    @Test
    void testFromStringEmpty() {
        assertThrows(IllegalArgumentException.class, () -> Card.fromString(""));
    }

    @Test
    void testFromStringTooShort() {
        assertThrows(IllegalArgumentException.class, () -> Card.fromString("K"));
    }

    @Test
    void testFromStringTooLong() {
        assertThrows(IllegalArgumentException.class, () -> Card.fromString("Kd Qs"));
    }

    @Test
    void testToString() {
        assertEquals("Kd", new Card(Rank.KING, Suit.DIAMONDS).toString());
        assertEquals("5s", new Card(Rank.FIVE, Suit.SPADES).toString());
        assertEquals("Jc", new Card(Rank.JACK, Suit.CLUBS).toString());
    }
}