package Blackjack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
 *   **************************************************************************
 *   *                                                                        *
 *   *                            BLACKJACK RULES                             *
 *   *                                                                        *
 *   **************************************************************************
 *
 *
 * 1. Dealer must hit on a soft 17.
 * 2. Natural Blackjack payout is 150% of original bet.
 * 3. One deck of cards is used which all hands draw from. Once emptied, a new
 *    deck is used immediately without interruption to the game.
 * 4. No minimum bet.
 * 5. Bets are returned on a tie.
 */

/**
 * Main method which starts up the Blackjack GUI application.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Blackjack.fxml"));
        primaryStage.setTitle("Blackjack");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
