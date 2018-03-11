package Blackjack;

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

    // event handlers for GUI elements

    @FXML
    private void betPressed(ActionEvent event) {

    }

    @FXML
    private void hitPressed(ActionEvent event) {

    }

    @FXML
    private void stayPressed(ActionEvent event) {

    }

}
