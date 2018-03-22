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
     * Adds to the player's current bet.
     *
     * @param bet Amount to be added to existing bet.
     * @throws InvalidBetException When bet is less than 1.
     * @throws BetTooBigException  When the bet exceeds amount of money available.
     */
    public void setBet(int bet) {
        if (bet < 1) {
            throw new InvalidBetException();
        }

        if (bet > money) {
            throw new BetTooBigException();
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
     * @param winner Wins bet if true, loses bet if false.
     */
    public void hasWonBet(boolean winner) {
        if (winner) {
            money += bet;
        }

        else {
            money -= bet;
        }

        // reset bet
        bet = 0;
    }

    /**
     * Add money to the wallet.
     *
     * @param amount How much money to add to the wallet.
     */
    public void addMoney(int amount) {
        money += amount;
    }
}
