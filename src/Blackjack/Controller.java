package Blackjack;

import Blackjack.Exceptions.BetTooBigException;
import Blackjack.Exceptions.InvalidBetException;
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
     * Button that moves the game to the next round and deals new hands.
     */
    @FXML
    private Button dealButton;

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

    /**
     * Sets the starting state of the game when the application is opened. This is called once automatically.
     */
    public void initialize() {

        // create all member variables

        dealerHand = new Hand();
        playerHand = new Hand();
        dealerWallet = new Wallet();
        playerWallet = new Wallet();

        // set all labels to their starting values

        dealerMoneyLabel.setText("$" + String.valueOf(dealerWallet.getMoney()));
        playerMoneyLabel.setText("$" + String.valueOf(playerWallet.getMoney()));
        currentBetLabel.setText("$" + String.valueOf(playerWallet.getBet()));

        // if the dealer's first card is an ace
        if (dealerHand.cardValue(0) == 11) {
            // set label to 11/1 to signify that the ace could be either 11 or 1
            dealerTotalLabel.setText("11/1 + ??");
        }
        else {
            // set label to the card value
            dealerTotalLabel.setText(String.valueOf(dealerHand.cardValue(0)) + " + ??");
        }

        // do not show playerTotalLabel until a bet is made
        playerTotalLabel.setText("N/A");

        // disable hit and stay buttons until a bet is made
        hitButton.setDisable(true);
        stayButton.setDisable(true);
    }

    // event handlers for GUI elements

    @FXML
    private void betPressed(ActionEvent event) {
        try {
            // set the player bet to the value in the betTextField
            playerWallet.setBet(Integer.parseInt(betTextField.getText()));

            // update currentBetLabel
            currentBetLabel.setText("$" + String.valueOf(playerWallet.getBet()));

            // disable button and text field until next round
            betButton.setDisable(true);
            betTextField.setDisable(true);

            // enable hit and stay buttons
            hitButton.setDisable(false);
            stayButton.setDisable(false);

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
        // only allow hitting if the player's hand is 9 cards or less (this shouldn't happen anyway, but just in case)
        // TODO: 3/19/18 consider removing this check if another method doesn't make this unsafe
        if (playerHand.getSize() <= 9) {
            playerHand.hit();
            playerTotalLabel.setText(String.valueOf(playerHand.sum()));

            // check if the player has busted or has Blackjack and 'stay'
            if (playerHand.sum() >= 21) {
                stayButton.fire();
            }

            // disable the hit button once the player has 9 cards
            if (playerHand.getSize() >= 9) {
                hitButton.setDisable(true);
            }
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

        // TODO: 3/19/18 handle bets
        // TODO: 3/19/18 implement a reset button to move to the next round
        // TODO: 3/19/18 implement a new game button that possibly calls the initialize() method (don't forget to force a new deck)
        // TODO: 3/19/18 handle running out of money
    }

}
