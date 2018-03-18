import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CardTest {
    @Test
    void testSmoke() {
        new Card(Card.KING, Card.DIAMONDS);
    }

    @Test
    void testIllegalRank() {
        assertThrows(IllegalArgumentException.class, () -> new Card(-1, Card.DIAMONDS));
    }

    @Test
    void testIllegalSuit() {
        assertThrows(IllegalArgumentException.class, () -> new Card(Card.KING, -1));
    }

    @Test
    void testGetRank() {
        assertEquals(Card.KING, new Card(Card.KING, Card.DIAMONDS).getRank());
        assertEquals(Card.FIVE, new Card(Card.FIVE, Card.SPADES).getRank());
        assertEquals(Card.JACK, new Card(Card.JACK, Card.CLUBS).getRank());
        assertEquals(Card.SIX, new Card(Card.SIX, Card.HEARTS).getRank());
        assertEquals(Card.NINE, new Card(Card.NINE, Card.DIAMONDS).getRank());
    }

    @Test
    void testGetSuit() {
        assertEquals(Card.DIAMONDS, new Card(Card.KING, Card.DIAMONDS).getSuit());
        assertEquals(Card.SPADES, new Card(Card.FIVE, Card.SPADES).getSuit());
        assertEquals(Card.CLUBS, new Card(Card.JACK, Card.CLUBS).getSuit());
        assertEquals(Card.HEARTS, new Card(Card.SIX, Card.HEARTS).getSuit());
    }

    @Test
    void testGetValue() {
        assertEquals(0b00001000000000000100101100100101, new Card(Card.KING, Card.DIAMONDS).getValue());
        assertEquals(0b00000000000010000001001100000111, new Card(Card.FIVE, Card.SPADES).getValue());
        assertEquals(0b00000010000000001000100100011101, new Card(Card.JACK, Card.CLUBS).getValue());
    }

    @Test
    void testFromString() {
        final Card kingOfDiamonds = Card.fromString("Kd");
        assertEquals(Card.KING, kingOfDiamonds.getRank());
        assertEquals(Card.DIAMONDS, kingOfDiamonds.getSuit());

        final Card fiveOfSpades = Card.fromString("5s");
        assertEquals(Card.FIVE, fiveOfSpades.getRank());
        assertEquals(Card.SPADES, fiveOfSpades.getSuit());

        final Card jackOfClubs = Card.fromString("Jc");
        assertEquals(Card.JACK, jackOfClubs.getRank());
        assertEquals(Card.CLUBS, jackOfClubs.getSuit());
    }

    @Test
    void testTooShortFromString() {
        assertThrows(IllegalArgumentException.class, () -> Card.fromString("K"));
    }

    @Test
    void testTooLongFromString() {
        assertThrows(IllegalArgumentException.class, () -> Card.fromString("Kd Qs"));
    }

    @Test
    void testInvalidFromString() {
        assertThrows(IllegalArgumentException.class, () -> Card.fromString("Kx"));
        assertThrows(IllegalArgumentException.class, () -> Card.fromString("Xd"));
        assertThrows(IllegalArgumentException.class, () -> Card.fromString("Xx"));
    }

    @Test
    void testToString() {
        assertEquals("Kd", new Card(Card.KING, Card.DIAMONDS).toString());
        assertEquals("5s", new Card(Card.FIVE, Card.SPADES).toString());
        assertEquals("Jc", new Card(Card.JACK, Card.CLUBS).toString());
    }
}