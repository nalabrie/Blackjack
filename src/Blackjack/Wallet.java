package Blackjack;

import Blackjack.Exceptions.BetTooBigException;
import Blackjack.Exceptions.InvalidBetException;
import Blackjack.Exceptions.NegativeAmountException;
import Blackjack.Exceptions.NegativeMoneyException;

/**
 * Class that simulates a player's wallet for betting.
 */
public class Wallet {
    /**
     * The amount of money in the wallet.
     */
    private int money;

    /**
     * The current amount set as a bet. This amount is added to 'money' if a bet is won or taken away otherwise.
     */
    private int bet;

    /**
     * Constructor that takes input value to set money in wallet.
     *
     * @param money Amount of money to start with in wallet.
     * @throws NegativeMoneyException When starting money is less than 0.
     */
    public Wallet(int money) {
        // check for valid input
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
     * Sets the bet.
     *
     * @param bet Amount to set the bet to.
     * @throws InvalidBetException When bet is less than 1.
     * @throws BetTooBigException  When the bet exceeds amount of money available.
     */
    public void setBet(int bet) {
        // bet cannot be below 2
        if (bet < 1) {
            throw new InvalidBetException();
        }

        // bet cannot be bigger than the amount of money available
        if (bet > money) {
            throw new BetTooBigException();
        }

        this.bet = bet;
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
     * Method that adjusts bet to 150% of the original bet. Used when a player wins with Blackjack (2 cards valued at 21).
     */
    public void adjustBetForBlackjack() {
        // change 'bet' to 150% of the original 'bet', using normal rounding
        bet = (int) Math.round(bet * 1.5);
    }

    /**
     * Add or remove money from the wallet.
     *
     * @param amount Amount to be added/removed. Can not be bigger than the amount of money available.
     * @throws NegativeMoneyException When amount to subtract exceeds the amount available.
     */
    public void modifyMoney(int amount) {
        // cannot remove more money than there is available
        if (money + amount < 0) {
            throw new NegativeMoneyException();
        }

        // add or remove 'amount' from the wallet
        money += amount;
    }

    /**
     * Transfer money out of a wallet.
     *
     * @param amount Amount to attempt to remove. If the amount is too big, the rest of the money in the wallet will be taken.
     * @return Amount taken from the wallet.
     * @throws NegativeAmountException When the 'amount' parameter is negative.
     */
    public int transferMoney(int amount) {
        // amount cannot be negative
        if (amount < 0) {
            throw new NegativeAmountException();
        }

        // if amount is more than the money available, change amount to equal money
        if (amount > money) {
            amount = money;
        }

        // subtract amount from money
        money -= amount;

        return amount;
    }

    /**
     * Reset the bet to 0.
     */
    public void resetBet() {
        bet = 0;
    }
}
