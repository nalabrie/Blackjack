package Blackjack;

import java.util.Arrays;
import java.util.Collections;

/**
 * Class that simulates a deck of cards.
 */
public class Deck {
    /**
     * The deck of cards, initially not shuffled.
     */
    private String[] deck = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A", "2", "3", "4",
            "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J",
            "Q", "K", "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

    /**
     * Constructor that creates a shuffled deck of cards.
     */
    public Deck() {
        shuffleDeck();
    }

    /**
     * Randomly shuffles the deck.
     */
    private void shuffleDeck() {
        Collections.shuffle(Arrays.asList(deck));
    }

    /**
     * Get the entire deck as an array.
     *
     * @return An array of the deck.
     */
    public String[] getDeck() {
        return deck;
    }

    /**
     * Gets the entire deck as a String.
     *
     * @return A String of the deck.
     */
    public String printDeck() {
        return Arrays.toString(deck);
    }

    /**
     * Get the size of the deck.
     *
     * @return How many cards are in the deck.
     */
    public int getSize() {
        return deck.length;
    }
}
