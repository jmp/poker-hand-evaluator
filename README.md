# Poker hand evaluator

This is a small poker hand evaluator I wrote for fun. It allows
you to work with poker cards and hands, especially to determine
the values of hands.

Note that there are much faster and more versatile evaluators
for different sizes of hands. This is just a small project of
mine, written for my own recreational purposes.

## How to use

Documentation: https://jmp.github.io/poker-hand-evaluator/

There are a couple of useful classes: `Card` and `Hand`.

## `Card` class

The `Card` class self-explanatory:

```java
// Create a king of clubs
Card kingOfClubs = new Card(Card.KING, Card.CLUBS);
```

A card can also be constructed from a string:

```java
// Create a king of clubs from a string
Card kingOfClubs = new Card("Kc");
```

Hands are arrays of cards:

```java
// A hand containing five cards
Card[] hand = {
    new Card(Card.KING, Card.CLUBS),
    new Card(Card.QUEEN, Card.HEARTS),
    new Card(Card.JACK, Card.DIAMONDS),
    new Card(Card.TEN, Card.SPADES),
    new Card(Card.NINE, Card.CLUBS),
};
```

## `Hand` class

The `Hand` class is an abstract class containing only static methods.

To evaluate a hand, use the `evaluate` method which takes an array
of cards as its only argument:

```java
// Evaluate a hand
int value = Hand.evaluate(hand);
```

The `evaluate` method returns the value of a hand as an integer
between 1 and 7462. The higher the value, the more valuable the hand.

The `fromString` method can be used to create a hand from a string:

```java
// Create a hand from a string
Card[] hand = Hand.fromString("Kd 5s Jc Ah Qc");
```

## Credits

The evaluator implements Kevin Suffecool's 5-card hand evaluator,
with the perfect hash optimization by Paul Senzee.
