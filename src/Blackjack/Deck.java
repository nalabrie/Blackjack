package Blackjack;

import Blackjack.Exceptions.DeckEmptyException;

import java.util.Arrays;
import java.util.Collections;

/**
 * Class that simulates a deck of playing cards.
 */
public class Deck {
    /**
     * Array of Card objects that make up the deck.
     */
    private Card[] deck = new Card[52];

    /**
     * Current position in the deck to draw from.
     */
    private int currentPos;

    /**
     * Constructor that creates a shuffled deck of 52 cards.
     */
    public Deck() {
        // populate deck array with an non-shuffled deck of cards
        for (int i = 0; i < deck.length; i++) {
            if (i < 13) {
                deck[i] = new Card(i + 1, "spades");
            }

            else if (i < 26) {
                deck[i] = new Card(i + 1, "hearts");
            }

            else if (i < 39) {
                deck[i] = new Card(i + 1, "clubs");
            }

            else {
                deck[i] = new Card(i + 1, "diamonds");
            }
        }

        // shuffle the deck
        Collections.shuffle(Arrays.asList(deck));

        // set the current position in the deck to the beginning
        currentPos = 0;
    }

    public Card draw() {
        // check if the deck is empty
        if (currentPos >= deck.length) {
            throw new DeckEmptyException();
        }

        // return the next card THEN increment the current position
        return deck[currentPos++];
    }

    // TODO: 3/20/18 test class, remove when finished
    @Override
    public String toString() {
        return "Deck{" +
                "deck=" + Arrays.toString(deck) +
                ", currentPos=" + currentPos +
                '}';
    }
}