package Blackjack;

import Blackjack.Exceptions.InvalidBetException;
import Blackjack.Exceptions.NegativeMoneyException;

/**
 * Class that simulates a player's wallet for betting.
 */
public class Wallet {
    private int money;
    private int bet;

    /**
     * Constructor that takes input value to set money in wallet.
     *
     * @param money Amount of money to start with in wallet.
     */
    public Wallet(int money) {
        if (money >= 0) {
            this.money = money;
        }

        else {
            throw new NegativeMoneyException();
        }

        // bet always starts at 0
        bet = 0;
    }

    /**
     * Default constructor, sets money in wallet to $100.
     */
    public Wallet() {
        this(100);
    }

    /**
     * Adds to the player's current bet.
     *
     * @param bet Amount to be added to existing bet.
     */
    public void setBet(int bet) {
        if (bet < 1) {
            throw new InvalidBetException();
        }

        this.bet += bet;
    }

    /**
     * Get the amount of money in the wallet.
     *
     * @return How much money is in the wallet.
     */
    public int getMoney() {
        return money;
    }

    /**
     * Get the current bet.
     *
     * @return The amount of money being bet.
     */
    public int getBet() {
        return bet;
    }

    /**
     * Method that adds or removes money from wallet depending on if a bet was won.
     *
     * @param hasWon Wins bet if true, loses bet if false.
     */
    public void hasWonBet(boolean hasWon) {
        if (hasWon) {
            money += bet;
        }

        else {
            money -= bet;
        }

        // reset bet
        bet = 0;
    }
}
