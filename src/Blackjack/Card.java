package Blackjack;

import Blackjack.Exceptions.InvalidCardValueException;
import Blackjack.Exceptions.InvalidSuitException;

/**
 * Class that simulates a playing card.
 */
public class Card {

    /**
     * The value of the card (using Blackjack rules).
     */
    private int value;

    /**
     * The symbol of the card (3, 10, J, K, etc).
     */
    private String symbol;

    /**
     * The suit of the card (spades, hearts, clubs, or diamonds).
     */
    private String suit;

    /**
     * Construct a card by its value.
     *
     * @param value Integer ranging from 1 to 14. 1 and 14 are both aces.
     * @param suit  String of the desired suit of the card.
     * @throws InvalidCardValueException When the value parameter isn't between 1 and 14 (inclusive).
     * @throws InvalidSuitException      When the suit parameter isn't any of the following: spades, hearts, clubs, diamonds.
     */
    public Card(int value, String suit) {
        // check for valid input
        if (value < 1 || value > 14) {
            throw new InvalidCardValueException();
        }

        // set member variables

        setSymbol(value);
        setValue(getSymbol());
        setSuit(suit);
    }

    /**
     * Get the value of the card.
     *
     * @return The card value.
     */
    public int getValue() {
        return value;
    }

    /**
     * Set the value of the card by its symbol (using Blackjack rules (aces are always 11, face cards are always 10)).
     *
     * @param symbol The symbol of the card (3, 10, J, K, etc).
     */
    private void setValue(String symbol) {
        // assign value based on given symbol
        switch (symbol) {
            case "A":
                this.value = 11;
                break;
            case "J":
            case "Q":
            case "K":
                this.value = 10;
                break;
            default:
                this.value = Integer.parseInt(symbol);
        }
    }

    /**
     * Get the symbol of the card.
     *
     * @return The card's symbol.
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Sets the symbol of the card based on its value.
     *
     * @param value Integer ranging from 1 to 14. 1 and 14 are both aces.
     * @throws InvalidCardValueException When the value parameter isn't between 1 and 14 (inclusive).
     */
    private void setSymbol(int value) {
        // check for valid input
        if (value < 1 || value > 14) {
            throw new InvalidCardValueException();
        }

        // assign symbol based on value
        switch (value) {
            case 11:
                symbol = "J";
                break;
            case 12:
                symbol = "Q";
                break;
            case 13:
                symbol = "K";
                break;
            case 1:
            case 14:
                symbol = "A";
                break;
            default:
                symbol = String.valueOf(value);
        }
    }

    /**
     * Get the suit of the card.
     *
     * @return The suit of the card.
     */
    public String getSuit() {
        return suit;
    }

    /**
     * Set the suit of the card.
     *
     * @param suit The suit to assign the card. Suit must be spades, hearts, clubs, or diamonds.
     * @throws InvalidSuitException When the suit isn't one of the following: spades, hearts, clubs, or diamonds.
     */
    private void setSuit(String suit) {
        // check for valid input
        if (suit.equals("spades") || suit.equals("hearts") || suit.equals("clubs") || suit.equals("diamonds")) {
            this.suit = suit;
        }

        // input was not valid, throw exception
        else {
            throw new InvalidSuitException();
        }
    }
}
