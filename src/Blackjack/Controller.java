package Blackjack;

import Blackjack.Exceptions.BetTooBigException;
import Blackjack.Exceptions.InvalidBetException;
import Blackjack.Exceptions.NegativeMoneyException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

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

    /**
     * FlowPane to contain the images of the dealer's hand.
     */
    @FXML
    private FlowPane dealerFlowPane;

    /**
     * FlowPane to contain the images of the player's hand.
     */
    @FXML
    private FlowPane playerFlowPane;

    /**
     * Array of ImageView objects to store up to 12 images of the dealer's hand of cards.
     */
    private ImageView[] dealerImageView;

    /**
     * Array of ImageView objects to store up to 12 images of the player's hand of cards.
     */
    private ImageView[] playerImageView;

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
     * Sets the starting state of the game when the application is opened. This is called once automatically and whenever 'newGameButton' is pressed.
     */
    public void initialize() {
        // if new game is pressed, reset certain objects

        Hand.forceNewDeck();    // ensures a new deck is created
        dealerFlowPane.getChildren().clear();   // clear flow panes to remove card images
        playerFlowPane.getChildren().clear();   // clear flow panes to remove card images

        // create all member variables

        dealerHand = new Hand();
        playerHand = new Hand();
        dealerWallet = new Wallet(1000);    // dealer starts with extra money
        playerWallet = new Wallet();
        dealerImageView = new ImageView[12];
        playerImageView = new ImageView[12];

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

        // do not show 'new game', 'winnerLabel', and 'next round' buttons until thr round is over
        winnerLabel.setVisible(false);
        newGameButton.setVisible(false);
        nextRoundButton.setVisible(false);

        // make sure the bet area is enabled and focused
        betButton.setDisable(false);
        betTextField.setDisable(false);
        betTextField.requestFocus();
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

            // show dealer and player hands
            updateDealerFlowPane(true);
            updatePlayerFlowPane();
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

            // set winner message
            winnerLabel.setText("Player wins with Blackjack!");
        }

        // if dealer wins
        if (winner.equals("dealer") || winner.equals("dealer blackjack")) {
            // add bet to dealer's wallet
            dealerWallet.addMoney(playerWallet.getBet());

            // process the dealer winning the bet
            playerWallet.hasWonBet(false);

            // set winner message
            if (winner.equals("dealer blackjack")) {
                winnerLabel.setText("Dealer wins with Blackjack!");
            }

            else {
                winnerLabel.setText("Dealer wins!");
            }
        }

        // if player wins
        if (winner.equals("player")) {
            // remove bet amount from dealer
            try {
                dealerWallet.subtractMoney(playerWallet.getBet());
            } catch (NegativeMoneyException e) {
                // TODO: 3/23/18 handle situation when dealer runs out of money and cannot pay the player
            }

            // process the player winning the bet
            playerWallet.hasWonBet(true);

            // set winner message
            winnerLabel.setText("Player wins!");
        }

        // if there is a tie
        if (winner.equals("tie")) {
            // return bet to the player
            playerWallet.resetBet();

            // set winner message
            winnerLabel.setText("Tie!");
        }

        // update money and bet amount labels

        dealerMoneyLabel.setText(String.valueOf(dealerWallet.getMoney()));
        playerMoneyLabel.setText(String.valueOf(playerWallet.getMoney()));
        currentBetLabel.setText(String.valueOf(playerWallet.getBet()));

        // display winner, new game, and next round buttons
        winnerLabel.setVisible(true);
        newGameButton.setVisible(true);
        nextRoundButton.setVisible(true);

        // TODO: 3/24/18 display card images
        // TODO: 3/19/18 test bet handling
        // TODO: 3/19/18 implement a reset button to move to the next round
        // TODO: 3/23/18 track previous bet (might not have to), pull focus to bet box on new round, type in previous bet
        // TODO: 3/19/18 handle running out of money
        // TODO: 3/23/18 finish documentation of methods
        // TODO: 3/23/18 optional: improve efficiency by calculating sum once and storing it in a member variable
    }

    @FXML
    private void nextRoundPressed(ActionEvent event) {
    }

    // methods to handle game logic

    /**
     * Figure out who won and if there's a tie.
     *
     * @return String of the winner. Returns one of the following: 'dealer blackjack', 'player blackjack', 'player', 'dealer', or 'tie'.
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
            return "dealer blackjack";
        }

        // player has Blackjack and dealer does not
        if (playerHasBlackjack && !dealerHasBlackjack) {
            return "player blackjack";
        }

        // player busts
        if (playerSum > 21) {
            return "dealer";
        }

        // dealer busts
        if (dealerSum > 21) {
            return "player";
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

    /**
     * Creates/updates the card images representing the dealer's hand inside the 'dealerFlowPane' container.
     *
     * @param showFirstCard Shows the first card when true, replaces it with a card back when false.
     */
    private void updateDealerFlowPane(boolean showFirstCard) {
        if (showFirstCard) {
            // first card shown is a card back rather than the actual first card
            Image back = new Image("file:cards/back.png");
            dealerImageView[0] = new ImageView();
            dealerImageView[0].setImage(back);
            dealerImageView[0].setPreserveRatio(true);
            dealerImageView[0].setSmooth(true);
            dealerImageView[0].setCache(true);
            dealerImageView[0].setFitHeight(160);
            dealerFlowPane.getChildren().add(dealerImageView[0]);
        }

        // add all cards in the dealer's hand to the FlowPane as images
        for (int i = 0; i < dealerHand.getSize(); i++) {
            // only add card when it hasn't been created yet (more efficient than overwriting the same image every time)
            if (dealerImageView[i] == null) {
                Image card = new Image("file:cards/" + dealerHand.getCard(i).getSymbol() + dealerHand.getCard(i).getSuit() + ".png");
                dealerImageView[i] = new ImageView();
                dealerImageView[i].setImage(card);
                dealerImageView[i].setPreserveRatio(true);
                dealerImageView[i].setSmooth(true);
                dealerImageView[i].setCache(true);
                dealerImageView[i].setFitHeight(160);
                dealerFlowPane.getChildren().add(dealerImageView[i]);
                if (i != 0) {
                    FlowPane.setMargin(dealerImageView[i], new Insets(0, 0, 0, -75));
                }
            }
        }

        // TODO: 3/24/18 template. remove later.
        //Image pic = new Image("file:cards/2clubs.png");
        //ImageView iv1 = new ImageView();
        //iv1.setImage(pic);
        //iv1.setPreserveRatio(true);
        //iv1.setSmooth(true);
        //iv1.setCache(true);
        //iv1.setFitHeight(160);
        //dealerFlowPane.getChildren().add(iv1);
    }

    /**
     * Creates/updates the card images representing the player's hand inside the 'playerFlowPane' container.
     */
    private void updatePlayerFlowPane() {
    }
}
