package Blackjack;

import Blackjack.Exceptions.*;

/**
 * Class that simulates a player's hand of cards during a card game.
 */
public class Hand {
    /**
     * Shuffled deck which all hands draw from.
     */
    private static Deck deck;

    /**
     * Array which stores the hand of cards.
     */
    private Card[] hand;

    /**
     * How many cards are in the hand.
     */
    private int size;

    /**
     * Constructor that takes a hand size and draws that many cards into the hand.
     *
     * @param size The size of the hand to start with.
     * @throws HandBiggerThanDeckException When the amount of cards in the hand is bigger than the whole deck.
     * @throws InvalidHandSizeException    When the size parameter isn't at least 1.
     */
    public Hand(int size) {
        // create a new shuffled deck if one isn't created yet
        if (deck == null) {
            deck = new Deck();
        }

        // hand cannot be bigger than deck
        if (size > deck.getSize()) {
            throw new HandBiggerThanDeckException();
        }

        // hand must have at least 1 card
        if (size < 1) {
            throw new InvalidHandSizeException();
        }

        // hand can be as big as the deck
        hand = new Card[deck.getSize()];

        // draw cards into hand
        for (int i = 0; i < size; i++) {
            try {
                hand[i] = deck.draw();
            } catch (DeckEmptyException e) {
                // deck is empty, create new deck
                deck = new Deck();

                // finish drawing
                hand[i] = deck.draw();
            }
        }

        // save initial size of hand
        this.size = size;
    }

    /**
     * Default constructor which draws 2 cards (default starting Blackjack hand).
     */
    public Hand() {
        this(2);
    }

    /**
     * Sums up the hand using Blackjack rules for counting.
     *
     * @return The sum of the hand.
     */
    public int sum() {
        // track total of cards in hand
        int result = 0;

        // keep track of aces for subtracting later if sum goes above 21
        int aceCount = 0;

        // sum hand and count aces in hand
        for (int i = 0; i < size; i++) {
            result += hand[i].getValue();
            if (hand[i].getSymbol().equals("A")) {
                aceCount++;
            }
        }

        // Change ace values from 11 to 1 if sum is currently above 21
        while (result > 21 && aceCount != 0) {
            result -= 10;
            aceCount--;
        }

        return result;
    }

    /**
     * Add a single card to the hand from the deck.
     *
     * @throws HandFullException When the hand is already full and a hit cannot be completed.
     */
    public void hit() {
        // check if there's room in the hand to hit
        if (size >= deck.getSize()) {
            throw new HandFullException();
        }

        // draw a card
        try {
            hand[size] = deck.draw();
        } catch (DeckEmptyException e) {
            // deck is empty, create new deck
            deck = new Deck();

            // finish drawing
            hand[size] = deck.draw();
        }

        // increment size
        size++;
    }

    /**
     * Get the size of the hand.
     *
     * @return How many cards are in the hand.
     */
    public int getSize() {
        return size;
    }

    /**
     * Get a card from the hand by its index.
     *
     * @param index The index of the card to be returned, starting at 0.
     * @return The card object at the given index.
     * @throws HandIndexOutOfBoundsException When the index is below 0 or greater than hand size - 1.
     */
    public Card getCard(int index) {
        // check the index is in the bounds of the hand
        if (index < 0 || index >= size) {
            throw new HandIndexOutOfBoundsException();
        }

        return hand[index];
    }

    /**
     * Check if there's an Ace in the hand.
     *
     * @return True if there's an ace, false if there aren't any aces.
     */
    public boolean hasAce() {
        for (int i = 0; i < size; i++) {
            if (hand[i].getSymbol().equals("A")) {
                return true;
            }
        }

        return false;
    }

    /**
     * Force a new deck to be created.
     */
    public static void forceNewDeck() {
        deck = new Deck();
    }
}
