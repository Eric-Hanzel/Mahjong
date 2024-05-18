package com.mahjong.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class GameRulesController extends Client {

    public VBox rulesVBox;
    public TextArea rulesTextArea;
    @FXML
    private Button returnButton;

    // Constructor for GameRulesController
    public GameRulesController() throws IOException {
    }
    @FXML
    public void initialize() {
        Image backgroundImage = new Image(getClass().getResource("/images/backGround.png").toExternalForm());
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, false, true)); // 调整BackgroundSize的参数以覆盖整个VBox
        rulesVBox.setBackground(new Background(background));

        String rules = "Winning Rules:\n" +
                "1) Basic Requirements:\n" +
                "   A winning hand must include at least one set of triples and a sequence and must have a pair.\n\n" +
                "2) Ways to Win:\n" +
                "   Players can win by either discarding a tile that allows another player to win or by drawing a winning tile themselves.\n\n" +
                "3) Special Winning Conditions:\n" +
                "   - Seven Pairs:\n" +
                "     The hand consists of seven different pairs, without any triples or sequences.\n" +
                "   - Dragon Seven Pairs:\n" +
                "     Seven pairs where at least one of the pairs consists of four identical tiles, with the rest being pairs.\n" +
                "   - Pure Hand:\n" +
                "     All tiles in the hand are of the same suit.\n" +
                "   - All Pongs:\n" +
                "     The hand consists of four sets of triples and one pair.\n" +
                "   - Winning from a Kong:\n" +
                "     Winning by drawing a tile after declaring a kong.\n" +
                "   - Last Tile Win:\n" +
                "     Winning by drawing the last tile from the wall.\n" +
                "   - Heavenly Hand:\n" +
                "     The dealer wins with the initial hand before discarding.\n" +
                "   - Earthly Hand:\n" +
                "     A non-dealer player wins off the first tile discarded by the dealer.";

        rulesTextArea.setText(rules);

    }

    @FXML
    private void handleReturn() throws IOException {
        // Handle returning to the previous page logic
        // Load and display the previous page
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("menu.fxml"));
        Scene menuScene = new Scene(fxmlLoader.load(), 1000, 1000);

        // Get the current window and set the new scene
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.setScene(menuScene);
    }
}

