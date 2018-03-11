package Blackjack;

import Blackjack.Exceptions.BetTooBigException;
import Blackjack.Exceptions.DeckEmptyException;
import Blackjack.Exceptions.InvalidBetException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {

    // member variables for GUI elements

    @FXML
    private TextField betTextField;

    @FXML
    private Button betButton;

    @FXML
    private Button hitButton;

    @FXML
    private Button stayButton;

    @FXML
    private Label dealerMoneyLabel;

    @FXML
    private Label playerMoneyLabel;

    @FXML
    private Label currentBetLabel;

    @FXML
    private Label dealerTotalLabel;

    @FXML
    private Label playerTotalLabel;

    // member variables for players and their wallets

    private Hand dealerHand = new Hand();
    private Hand playerHand = new Hand();
    private Wallet dealerWallet = new Wallet();
    private Wallet playerWallet = new Wallet();

    // set the starting state of the game

    public void initialize() {

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
        try {
            playerHand.hit();
            playerTotalLabel.setText(String.valueOf(playerHand.sum()));
            if (playerHand.sum() >= 21) {
                stayButton.fire();
            }
        } catch (DeckEmptyException e) {
            // TODO: 3/11/18 Handle this somehow
            System.out.println("DECK EMPTY");
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
    }

}
