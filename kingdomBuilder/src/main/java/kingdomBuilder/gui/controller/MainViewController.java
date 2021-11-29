package kingdomBuilder.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/*
Falls Daten von einer View an die nächste übergeben werden sollen, bspw. beim Erstellen eines neuen Spiels
die Anzahl der Spieler oder die Schwierigkeitsstufe der AI, müsste man sich überlegen, wie das geregelt werden soll.
Die Views werden immer wieder zerstört und neu erschaffen (!), also verschwinden auch die Daten. REDUX?
 */

public class MainViewController implements Initializable {

    private FXMLLoader fxmlLoader;

    private MenuViewController menuViewController;
    private GameLobbyViewController gameLobbyViewController;
    private GameViewController gameViewController;

    @FXML
    private BorderPane borderPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showMenuView();
    }

    public void showMenuView() {
        try {
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/kingdomBuilder.gui/MenuView.fxml"));
            borderPane.setCenter(fxmlLoader.load());
            menuViewController = fxmlLoader.getController();
            menuViewController.setMainViewController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showGameLobbyView() {
        try {
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/kingdomBuilder.gui/GameLobbyView.fxml"));
            borderPane.setCenter(fxmlLoader.load());
            gameLobbyViewController = fxmlLoader.getController();
            gameLobbyViewController.setMainViewController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showGameView(String playerName) {
        try {
            // new FXMLLoder, due to 'root already specified' error
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/kingdomBuilder.gui/GameView.fxml"));
            borderPane.setCenter(fxmlLoader.load());
            gameViewController = fxmlLoader.getController();
            gameViewController.setMainViewController(this);

            // Möglicherweise überflüssig (siehe GameViewController)
            gameViewController.setPlayer1Name(playerName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // mögliche andere Views:
    // public void showLobbyView()
    // public void showGameResumeView()
    // public void showGameExplorerView()
}
