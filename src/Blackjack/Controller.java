package Blackjack;

import Blackjack.Exceptions.BetTooBigException;
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

    // member variables for players and their wallets

    private Hand dealerHand = new Hand();
    private Hand playerHand = new Hand();
    private Wallet dealerWallet = new Wallet();
    private Wallet playerWallet = new Wallet();

    // set the starting state of the game

    public void initialize() {

        // set all labels to their starting values

        dealerMoneyLabel.setText(String.valueOf(dealerWallet.getMoney()));
        playerMoneyLabel.setText(String.valueOf(playerWallet.getMoney()));
        currentBetLabel.setText(String.valueOf(playerWallet.getBet()));
    }

    // event handlers for GUI elements

    @FXML
    private void betPressed(ActionEvent event) {
        try {
            // set the player bet to the value in the betTextField
            playerWallet.setBet(Integer.parseInt(betTextField.getText()));

            // update currentBetLabel
            currentBetLabel.setText(String.valueOf(playerWallet.getBet()));

            // disable button and text field until next round
            betButton.setDisable(true);
            betTextField.setDisable(true);
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

    }

    @FXML
    private void stayPressed(ActionEvent event) {

    }

}
