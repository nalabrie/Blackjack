package Blackjack;

import Blackjack.Exceptions.BetTooBigException;
import Blackjack.Exceptions.InvalidBetException;
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
     * Method that adds or removes the bet from wallet depending on if a bet was won.
     *
     * @param winner Wins bet if true, loses bet if false.
     */
    public void hasWonBet(boolean winner) {
        if (winner) {
            money += bet;
        }

        else {
            // TODO: 3/23/18 add exception handling (maybe not needed?)
            money -= bet;
        }

        // reset bet
        bet = 0;
    }

    /**
     * Method that adjusts bet to 150% of the original bet. Used when a player wins with Blackjack (2 cards valued at 21).
     */
    public void adjustBetForBlackjack() {
        // change 'bet' to 150% of the original 'bet', using normal rounding
        bet = (int) Math.round(bet * 1.5);
    }

    /**
     * Add money to the wallet.
     *
     * @param amount How much money to add to the wallet.
     */
    public void addMoney(int amount) {
        money += amount;
    }

    /**
     * Subtract money from the wallet.
     *
     * @param amount How much money to subtract from the wallet.
     * @throws NegativeMoneyException When the amount to subtract exceeds the amount available.
     */
    public void subtractMoney(int amount) {
        // cannot remove more money than there is available
        if (amount > money) {
            throw new NegativeMoneyException();
        }

        money -= amount;
    }

    /**
     * Reset the bet to 0. Usually used when there is a tie in Blackjack and the bet is returned.
     */
    public void resetBet() {
        bet = 0;
    }
}
