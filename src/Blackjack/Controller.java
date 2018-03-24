package Blackjack;

import Blackjack.Exceptions.BetTooBigException;
import Blackjack.Exceptions.InvalidBetException;
import Blackjack.Exceptions.NegativeMoneyException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controller to handle the state of the game and the GUI.
 */
public class Controller {

    // member variables for GUI elements

    /**
     * Text field for entering bets. Pressing 'Enter' or pressing the 'betButton' will submit the bet.
     */
    @FXML
    private TextField betTextField;

    /**
     * Button to submit an entered bet.
     */
    @FXML
    private Button betButton;

    /**
     * Button to hit the player with another card.
     */
    @FXML
    private Button hitButton;

    /**
     * Button to 'stay' and allow the dealer to process their turn.
     */
    @FXML
    private Button stayButton;

    /**
     * Label that displays the dealer's money.
     */
    @FXML
    private Label dealerMoneyLabel;

    /**
     * Label that displays the player's money.
     */
    @FXML
    private Label playerMoneyLabel;

    /**
     * Label that displays the current entered bet.
     */
    @FXML
    private Label currentBetLabel;

    /**
     * Label that displays the dealer's current total of their hand.
     */
    @FXML
    private Label dealerTotalLabel;

    /**
     * Label that displays the player's current total of their hand.
     */
    @FXML
    private Label playerTotalLabel;

    /**
     * Button that starts a totally new game when pressed.
     */
    @FXML
    private Button newGameButton;

    /**
     * Button that moves the game to the next round.
     */
    @FXML
    private Button nextRoundButton;

    /**
     * Label that displays the winner at the end of each round.
     */
    @FXML
    private Label winnerLabel;

    // member variables for players and their wallets

    /**
     * The dealer's hand of cards.
     */
    private Hand dealerHand;

    /**
     * The player's hand of cards.
     */
    private Hand playerHand;

    /**
     * The dealer's wallet.
     */
    private Wallet dealerWallet;

    /**
     * The player's wallet.
     */
    private Wallet playerWallet;

    // initialize the state of the game

    /**
     * Sets the starting state of the game when the application is opened. This is called once automatically.
     */
    public void initialize() {

        // create all member variables

        dealerHand = new Hand();
        playerHand = new Hand();
        dealerWallet = new Wallet(1000);    // dealer starts with extra money
        playerWallet = new Wallet();

        // set all labels to their starting values

        dealerMoneyLabel.setText("$" + String.valueOf(dealerWallet.getMoney()));
        playerMoneyLabel.setText("$" + String.valueOf(playerWallet.getMoney()));
        currentBetLabel.setText("$" + String.valueOf(playerWallet.getBet()));

        // do not show either dealer or player totals until a bet is made
        dealerTotalLabel.setText("N/A");
        playerTotalLabel.setText("N/A");

        // disable hit and stay buttons until a bet is made
        hitButton.setDisable(true);
        stayButton.setDisable(true);

        // do not show 'new game', 'winnerLabel', and 'deal' buttons until thr round is over
        winnerLabel.setVisible(false);
        newGameButton.setVisible(false);
        nextRoundButton.setVisible(false);
    }

    // event handlers for GUI elements

    @FXML
    private void betPressed(ActionEvent event) {
        try {
            // set the player bet to the value in the betTextField
            playerWallet.setBet(Integer.parseInt(betTextField.getText()));

            // update currentBetLabel
            currentBetLabel.setText("$" + String.valueOf(playerWallet.getBet()));

            // disable bet button and text field until next round
            betButton.setDisable(true);
            betTextField.setDisable(true);

            // enable hit and stay buttons
            hitButton.setDisable(false);
            stayButton.setDisable(false);

            // update dealerTotalLabel to show the value of their first card

            // if the dealer's first card is an ace
            if (dealerHand.getCard(0).getSymbol().equals("A")) {
                // set label to 11/1 to signify that the ace could be either 11 or 1
                dealerTotalLabel.setText("11/1 + ??");
            }

            else {
                // set label to the card value
                dealerTotalLabel.setText(String.valueOf(dealerHand.getCard(0).getValue()) + " + ??");
            }

            // update playerTotalLabel
            playerTotalLabel.setText(String.valueOf(playerHand.sum()));

            // if the dealer or player were dealt Blackjack
            if (dealerHand.sum() == 21 || playerHand.sum() == 21) {
                stayButton.fire();
            }
        } catch (NumberFormatException | InvalidBetException e) {
            // invalid input (bet is not an integer or <= 0) so focus on the input error and try again
            betTextField.setText("Invalid bet");
            betTextField.selectAll();
            betTextField.requestFocus();
        } catch (BetTooBigException e) {
            // invalid input (bet is > player money) so focus on the input error and try again
            betTextField.setText("Bet too big");
            betTextField.selectAll();
            betTextField.requestFocus();
        }
    }

    @FXML
    private void hitPressed(ActionEvent event) {
        // hit player
        playerHand.hit();

        // update player's total
        playerTotalLabel.setText(String.valueOf(playerHand.sum()));

        // check if the player has busted or has Blackjack and 'stay'
        if (playerHand.sum() >= 21) {
            stayButton.fire();
        }

        // disable the hit button once the player has 12 cards
        if (playerHand.getSize() >= 12) {
            hitButton.setDisable(true);
        }
    }

    @FXML
    private void stayPressed(ActionEvent event) {
        // disable all buttons and text fields until the round restarts
        betTextField.setDisable(true);
        betButton.setDisable(true);
        hitButton.setDisable(true);
        stayButton.setDisable(true);

        // dealer hits repeatedly, stands on hard a 17
        while (dealerHand.sum() < 17) {
            dealerHand.hit();

            // if dealer has a soft 17, hit again
            if (dealerHand.sum() == 17 && dealerHand.hasAce()) {
                dealerHand.hit();
            }
        }

        // update dealerTotalLabel
        dealerTotalLabel.setText(String.valueOf(dealerHand.sum()));

        // check for winner
        String winner = getWinner();

        // handle bets

        // if player wins with Blackjack
        if (winner.equals("player blackjack")) {
            // change bet to 150% of the bet since player has Blackjack
            playerWallet.adjustBetForBlackjack();

            // remove bet amount from dealer
            try {
                dealerWallet.subtractMoney(playerWallet.getBet());
            } catch (NegativeMoneyException e) {
                // TODO: 3/23/18 handle situation when dealer runs out of money and cannot pay the player
            }

            // process the player winning the bet
            playerWallet.hasWonBet(true);
        }

        // if dealer wins
        if (winner.equals("dealer")) {
        }

        // if player wins
        if (winner.equals("player")) {
        }

        // if there is a tie
        if (winner.equals("tie")) {
        }

        // update money amount labels

        // TODO: 3/19/18 handle bets
        // TODO: 3/19/18 implement a reset button to move to the next round
        // TODO: 3/19/18 implement a new game button that possibly calls the initialize() method (don't forget to force a new deck)
        // TODO: 3/23/18 track previous bet (might not have to), pull focus to bet box on new round, type in previous bet
        // TODO: 3/19/18 handle running out of money
        // TODO: 3/23/18 finish documentation of methods
        // TODO: 3/23/18 optional: improve efficiency by calculating sum once and storing it in a member variable
    }

    @FXML
    private void newGamePressed(ActionEvent event) {
    }

    @FXML
    private void nextRoundPressed(ActionEvent event) {
    }

    // methods to handle game logic

    /**
     * Figure out who won and if there's a tie.
     *
     * @return String of the winner. Returns one of the following: 'player blackjack', 'player', 'dealer', or 'tie'.
     */
    private String getWinner() {
        // to avoid calling 'sum' method many times, store sums as local variables (more efficient)
        int dealerSum = dealerHand.sum();
        int playerSum = playerHand.sum();

        // check if dealer and player have Blackjack (2 cards valued at 21)
        boolean dealerHasBlackjack = dealerHand.getSize() == 2 && dealerSum == 21;
        boolean playerHasBlackjack = playerHand.getSize() == 2 && playerSum == 21;

        // dealer has Blackjack and player does not
        if (dealerHasBlackjack && !playerHasBlackjack) {
            return "dealer";
        }

        // player has Blackjack and dealer does not
        if (playerHasBlackjack && !dealerHasBlackjack) {
            return "player blackjack";
        }

        // dealer busts
        if (dealerSum > 21) {
            return "player";
        }

        // player busts
        if (playerSum > 21) {
            return "dealer";
        }

        // dealer has bigger hand
        if (dealerSum > playerSum) {
            return "dealer";
        }

        // player has bigger hand
        if (playerSum > dealerSum) {
            return "player";
        }

        if (playerSum == dealerSum) {
            return "tie";
        }

        // TODO: 3/23/18 handle differently because this is bad design
        // should never get this far
        return "ERROR";
    }
}
