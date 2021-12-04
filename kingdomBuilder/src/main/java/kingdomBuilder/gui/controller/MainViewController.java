package kingdomBuilder.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import kingdomBuilder.gui.SceneLoader;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/*
Falls Daten von einer View an die nächste übergeben werden sollen, bspw. beim Erstellen eines neuen Spiels
die Anzahl der Spieler oder die Schwierigkeitsstufe der AI, müsste man sich überlegen, wie das geregelt werden soll.
Die Views werden immer wieder zerstört und neu erschaffen (!), also verschwinden auch die Daten. REDUX?
 */

public class MainViewController implements Initializable {
    private SceneLoader sceneLoader;

    @FXML
    private BorderPane borderPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sceneLoader = new SceneLoader();
        showIAmView();
    }

    public void showMenuView() {
        borderPane.setCenter(sceneLoader.getMenuView());
        MenuViewController menuViewController = (MenuViewController) sceneLoader.getMenuViewController();
        menuViewController.setMainViewController(this);

        //loading chat in
        borderPane.setLeft(sceneLoader.getChatView());
        ChatViewController chatViewController = (ChatViewController) sceneLoader.getChatViewController();
        chatViewController.setMainViewController(this);
    }

    public void showGameLobbyView() {
        borderPane.setCenter(sceneLoader.getGameLobbyView());
        GameLobbyViewController gameLobbyViewController = (GameLobbyViewController)
                sceneLoader.getGameLobbyViewController();
        gameLobbyViewController.setMainViewController(this);
    }

    public void showGameView(String playerName) {
        //resets the GameView and generates the board new
        sceneLoader.loadGameView();
        //loads the GameView
        borderPane.setCenter(sceneLoader.getGameView());
        GameViewController gameViewController = (GameViewController) sceneLoader.getGameViewController();
        gameViewController.setMainViewController(this);

        // TODO: Focus-management
        gameViewController.getGame_subscene().getRoot().requestFocus();

        // TODO: Möglicherweise überflüssig (siehe GameViewController)
        gameViewController.setPlayer1Name(playerName);
    }

    public void showIAmView() {
        borderPane.setCenter(sceneLoader.getIAmView());
        IAmViewController iAmViewController = (IAmViewController) sceneLoader.getIAmViewController();
        iAmViewController.setIAmViewController(this);
    }

    // mögliche andere Views:
    // public void showLobbyView()
    // public void showGameResumeView()
    // public void showGameExplorerView()
}
