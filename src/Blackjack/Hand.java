package Blackjack;

import Blackjack.Exceptions.DeckEmptyException;
import Blackjack.Exceptions.HandBiggerThanDeckException;

import java.util.Arrays;

/**
 * Class that simulates a player's hand of cards during a card game.
 */
public class Hand {
    /**
     * Current position to draw from in the deck. Increments with each draw.
     */
    private static int deckPos = 0;

    /**
     * Shuffled deck which all hands draw from.
     */
    private static Deck deck;

    /**
     * Array which stores the hand of cards.
     */
    private String[] hand;

    /**
     * How many cards are in the hand.
     */
    private int size;

    /**
     * Constructor that takes a hand size and draws that many cards.
     *
     * @param size The size of the hand to start with.
     * @throws HandBiggerThanDeckException When the amount of cards in the hand is bigger than the whole deck,
     */
    public Hand(int size) {
        // TODO: 3/4/18 Consider generating deck before constructor is called
        if (deck == null) {
            deck = new Deck();
        }

        // hand can be as big as the deck
        hand = new String[deck.getSize()];

        if (size > hand.length) {
            throw new HandBiggerThanDeckException();
        }

        for (int i = 0; i < size; i++) {
            hand[i] = draw();
        }

        this.size = size;
    }

    /**
     * Default constructor which draws 2 cards (default starting Blackjack hand).
     */
    public Hand() {
        this(2);
    }

    /**
     * Draws card and safely increments deckPos.
     *
     * @return The card to be drawn.
     * @throws DeckEmptyException When the deck is empty and cannot draw anymore.
     */
    private String draw() {
        if (deckPos < deck.getSize()) {
            return deck.getDeck()[deckPos++];
        }

        else {
            // TODO: 3/4/18 Consider handling automatically rather than throwing an exception
            throw new DeckEmptyException();
        }
    }

    /**
     * Gets the value of a single card in the hand, aces are always 11, face cards are always 10.
     *
     * @param index Index of the card in the hand array to get the value of.
     * @return The value of the card.
     */
    public int cardValue(int index) {
        switch (hand[index]) {
            case "A":
                return 11;
            case "J":
            case "Q":
            case "K":
                return 10;
            default:
                return Integer.parseInt(hand[index]);
        }
    }

    /**
     * Sums up the hand using Blackjack rules for counting.
     *
     * @return The sum of the hand.
     */
    public int sum() {
        int result = 0;

        // keep track of aces for subtracting later if sum goes above 21
        int aceCount = 0;
        for (int i = 0; i < size; i++) {
            result += cardValue(i);
            if (cardValue(i) == 11) {
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
     * Add a single card to the hand.
     */
    public void hit() {
        hand[size++] = draw();
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
     * Check if there's an Ace in the hand.
     *
     * @return True if there's an ace, false if there aren't any aces.
     */
    public boolean hasAce() {
        for (int i = 0; i < size; i++) {
            if (hand[i].equals("A")) {
                return true;
            }
        }

        return false;
    }

    // TODO: 2/15/18 remove when finished debugging
    @Override
    public String toString() {
        return "Hand{" +
                "hand=" + Arrays.toString(hand) +
                '}';
    }
}
