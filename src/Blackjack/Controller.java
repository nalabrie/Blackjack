package Blackjack;

import Blackjack.Exceptions.BetTooBigException;
import Blackjack.Exceptions.InvalidBetException;
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
     * Title above 'dealerFlowPane'.
     */
    @FXML
    private Label dealerFlowPaneTitle;

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
     * Label that appears when the game is over (someone is out of money).
     */
    @FXML
    private Label gameOverLabel;

    /**
     * FlowPane to contain the images of the dealer's hand.
     */
    @FXML
    private FlowPane dealerFlowPane;

    /**
     * Title above 'playerFlowPane'.
     */
    @FXML
    private Label playerFlowPaneTitle;

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
        // if new game is pressed, reset certain objects that may be messed up
        Hand.forceNewDeck();                    // ensures a new deck is created
        dealerFlowPane.getChildren().clear();   // clear flow panes to remove card images
        playerFlowPane.getChildren().clear();   // clear flow panes to remove card images
        playerFlowPaneTitle.setVisible(true);   // needs reset if pressing 'new game' after 'gameOver' runs
        dealerFlowPaneTitle.setVisible(true);   // needs reset if pressing 'new game' after 'gameOver' runs
        dealerFlowPane.setVisible(true);        // needs reset if pressing 'new game' after 'gameOver' runs
        playerFlowPane.setVisible(true);        // needs reset if pressing 'new game' after 'gameOver' runs
        nextRoundButton.setDisable(false);      // needs reset if pressing 'new game' after 'gameOver' runs

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

        // do not show certain buttons and labels until they are needed
        winnerLabel.setVisible(false);
        newGameButton.setVisible(false);
        nextRoundButton.setVisible(false);
        gameOverLabel.setVisible(false);

        // make sure the bet area is enabled and focused
        betButton.setDisable(false);
        betTextField.setDisable(false);
        betTextField.requestFocus();
    }

    // methods to handle GUI elements

    /**
     * Runs when the bet button is pressed. Gets the bet that was entered in the bet text field and deals the dealer and player cards.
     */
    @FXML
    private void betPressed() {
        try {
            // set the player bet to the value in the betTextField
            playerWallet.setBet(Integer.parseInt(betTextField.getText()));

            // make sure the bet isn't bigger than the amount of money the dealer has
            if (playerWallet.getBet() > dealerWallet.getMoney()) {
                throw new BetTooBigException();
            }

            // update currentBetLabel
            currentBetLabel.setText("$" + String.valueOf(playerWallet.getBet()));

            // disable bet button and text field until next round
            betButton.setDisable(true);
            betTextField.setDisable(true);

            // enable hit and stay buttons
            hitButton.setDisable(false);
            stayButton.setDisable(false);

            // update dealerTotalLabel to show the value of their second card

            // if the dealer's second card is an ace
            if (dealerHand.getCard(1).getSymbol().equals("A")) {
                // set label to 11/1 to signify that the ace could be either 11 or 1
                dealerTotalLabel.setText("11/1 + ??");
            }

            else {
                // set label to the card value
                dealerTotalLabel.setText(String.valueOf(dealerHand.getCard(1).getValue()) + " + ??");
            }

            // call 'Sum' methods now (more efficient than calling multiple times)
            int playerSum = playerHand.sum();
            int dealerSum = dealerHand.sum();

            // update playerTotalLabel
            playerTotalLabel.setText(String.valueOf(playerSum));

            // if the dealer or player were dealt Blackjack
            if (dealerSum == 21 || playerSum == 21) {
                stayButton.fire();
            }

            else {
                // show dealer and player hands
                // note: this is inside an 'else' because it would be redundant to run otherwise
                updateDealerFlowPane(true);
                updatePlayerFlowPane();
            }
        } catch (NumberFormatException | InvalidBetException e) {
            // invalid input (bet is not an integer or <= 0) so focus on the input error and try again
            betTextField.setText("Invalid bet");
            betTextField.selectAll();
            betTextField.requestFocus();
        } catch (BetTooBigException e) {
            // invalid input (bet is > player or dealer money) so focus on the input error and try again
            betTextField.setText("Bet too big");
            betTextField.selectAll();
            betTextField.requestFocus();
        }
    }

    /**
     * Runs when hit button is pressed. Hits the player with a card.
     */
    @FXML
    private void hitPressed() {
        // hit player
        playerHand.hit();

        // call 'Sum' method now (more efficient than calling multiple times)
        int playerSum = playerHand.sum();

        // update player's total
        playerTotalLabel.setText(String.valueOf(playerSum));

        // update images for player's hand
        updatePlayerFlowPane();

        // check if the player has busted or has Blackjack and 'stay'
        if (playerSum >= 21) {
            stayButton.fire();
        }

        // disable the hit button once the player has 12 cards
        if (playerHand.getSize() >= 12) {
            hitButton.setDisable(true);
        }
    }

    /**
     * Runs when the stay button is pressed. 'Stays', processes the dealer's turn, figures out who won, and settles bets.
     */
    @FXML
    private void stayPressed() {
        // disable all buttons and text fields until the round restarts
        betTextField.setDisable(true);
        betButton.setDisable(true);
        hitButton.setDisable(true);
        stayButton.setDisable(true);

        // to avoid calling 'sum' method many times, store sums as local variables (more efficient)
        int playerSum = playerHand.sum();

        // check if the player has a natural Blackjack (2 cards valued at 21)
        boolean playerHasBlackjack = playerHand.getSize() == 2 && playerSum == 21;

        // dealer only hits if the player hasn't busted yet and doesn't have Blackjack
        if (playerSum <= 21 && !playerHasBlackjack) {
            // dealer hits repeatedly, stands on hard a 17
            while (dealerHand.sum() < 17) {
                dealerHand.hit();

                // if dealer has a soft 17, hit again
                if (dealerHand.sum() == 17 && dealerHand.hasAce()) {
                    dealerHand.hit();
                }
            }
        }

        // update dealerTotalLabel
        dealerTotalLabel.setText(String.valueOf(dealerHand.sum()));

        // update images for both hands
        updateDealerFlowPane(false);
        updatePlayerFlowPane();

        // check for winner
        String winner = getWinner();

        // handle bets

        // if player wins with Blackjack
        if (winner.equals("player blackjack")) {
            // change bet to 150% of the bet since player has Blackjack
            playerWallet.adjustBetForBlackjack();

            // transfer bet from dealer to player
            playerWallet.modifyMoney(dealerWallet.transferMoney(playerWallet.getBet()));

            // reset player's bet
            playerWallet.resetBet();

            // set winner message
            winnerLabel.setText("Player wins with Blackjack!");
        }

        // if dealer wins
        if (winner.equals("dealer") || winner.equals("dealer blackjack")) {
            // transfer bet from player to dealer
            dealerWallet.modifyMoney(playerWallet.transferMoney(playerWallet.getBet()));

            // reset player's bet
            playerWallet.resetBet();

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
            // transfer bet from dealer to player
            playerWallet.modifyMoney(dealerWallet.transferMoney(playerWallet.getBet()));

            // reset player's bet
            playerWallet.resetBet();

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

        dealerMoneyLabel.setText("$" + String.valueOf(dealerWallet.getMoney()));
        playerMoneyLabel.setText("$" + String.valueOf(playerWallet.getMoney()));
        currentBetLabel.setText("$" + String.valueOf(playerWallet.getBet()));

        // display winner, new game, and next round buttons
        winnerLabel.setVisible(true);
        newGameButton.setVisible(true);
        nextRoundButton.setVisible(true);

        // if the anyone is out of money, end the game
        if (playerWallet.getMoney() <= 0 || dealerWallet.getMoney() <= 0) {
            gameOver();
        }
    }

    /**
     * Proceeds to the next round when next round button is pressed.
     */
    @FXML
    private void nextRoundPressed() {
        // reset certain objects that may be messed up
        dealerFlowPane.getChildren().clear();   // clear flow panes to remove card images
        playerFlowPane.getChildren().clear();   // clear flow panes to remove card images
        dealerImageView = new ImageView[12];    // make new ImageView arrays so new cards can be added
        playerImageView = new ImageView[12];    // make new ImageView arrays so new cards can be added

        // deal new hands
        dealerHand = new Hand();
        playerHand = new Hand();

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

        // do not show certain buttons and labels until they are needed
        winnerLabel.setVisible(false);
        newGameButton.setVisible(false);
        nextRoundButton.setVisible(false);
        gameOverLabel.setVisible(false);

        // make sure the bet area is enabled and focused
        betButton.setDisable(false);
        betTextField.setDisable(false);
        betTextField.requestFocus();
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

        // should never get this far
        return "ERROR";
    }

    /**
     * Creates/updates the card images representing the dealer's hand inside the 'dealerFlowPane' container.
     *
     * @param showFirstCard Shows the first card when true, replaces it with a card back when false.
     */
    private void updateDealerFlowPane(boolean showFirstCard) {
        // if 'showFirstCard' is true: first card shown is a card back rather than the actual first card
        if (showFirstCard) {
            Image back = new Image("file:cards/back.png");
            dealerImageView[0] = new ImageView();
            dealerImageView[0].setImage(back);
            dealerImageView[0].setPreserveRatio(true);
            dealerImageView[0].setSmooth(true);
            dealerImageView[0].setCache(true);
            dealerImageView[0].setFitHeight(160);
            dealerFlowPane.getChildren().add(dealerImageView[0]);
        }

        // replace flipped over card with real card by clearing variables to force the next loop to run entirely
        else if (dealerImageView[0] != null) {
            dealerFlowPane.getChildren().clear();
            dealerImageView = new ImageView[12];
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
    }

    /**
     * Creates/updates the card images representing the player's hand inside the 'playerFlowPane' container.
     */
    private void updatePlayerFlowPane() {
        // add all cards in the player's hand to the FlowPane as images
        for (int i = 0; i < playerHand.getSize(); i++) {
            // only add card when it hasn't been created yet (more efficient than overwriting the same image every time)
            if (playerImageView[i] == null) {
                Image card = new Image("file:cards/" + playerHand.getCard(i).getSymbol() + playerHand.getCard(i).getSuit() + ".png");
                playerImageView[i] = new ImageView();
                playerImageView[i].setImage(card);
                playerImageView[i].setPreserveRatio(true);
                playerImageView[i].setSmooth(true);
                playerImageView[i].setCache(true);
                playerImageView[i].setFitHeight(160);
                playerFlowPane.getChildren().add(playerImageView[i]);
                if (i != 0) {
                    FlowPane.setMargin(playerImageView[i], new Insets(0, 0, 0, -75));
                }
            }
        }
    }

    /**
     * When one person is out of money, end the game.
     */
    private void gameOver() {
        // make room in the scene by making unnecessary items invisible
        playerFlowPaneTitle.setVisible(false);
        dealerFlowPaneTitle.setVisible(false);
        dealerFlowPane.setVisible(false);
        playerFlowPane.setVisible(false);

        // set hand totals to 'N/A' since the images are gone and it doesn't make sense to keep the totals too
        dealerTotalLabel.setText("N/A");
        playerTotalLabel.setText("N/A");

        // disable next round button
        nextRoundButton.setDisable(true);

        // display game over message

        // if dealer is out of money
        if (dealerWallet.getMoney() == 0) {
            gameOverLabel.setText("GAME OVER\n\ndealer is out of money!");
        }

        // if player is out of money
        if (playerWallet.getMoney() == 0) {
            gameOverLabel.setText("GAME OVER\n\nplayer is out of money!");
        }

        // show game over message
        gameOverLabel.setVisible(true);
    }
}
