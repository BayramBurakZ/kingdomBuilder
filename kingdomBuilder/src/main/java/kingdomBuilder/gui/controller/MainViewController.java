package kingdomBuilder.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import kingdomBuilder.gui.SceneLoader;

import java.net.URL;
import java.util.ResourceBundle;

/*
Views are stored in the sceneLoader-Object and generated when the sceneLoader object is generated
With sceneLoader.loadx() you reload the specific scene completely

All data will be stored in the states or substates of the store (REDUX)
TODO: Update functions for every scene to update its data from the REDUX subscriber system
 */

public class MainViewController implements Initializable {
    private SceneLoader sceneLoader;

    @FXML
    private BorderPane borderPane;


    /**
     * Creates the sceneLoader-Object that store and load every scene
     * Additional sets the first view to the IAmView
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sceneLoader = new SceneLoader();
        showIAmView();
    }

    /**
     * Loads the MenuView into the center of the main borderPane
     * and loads the chat into the left side of the main borderPane.
     * Additional this methode sets this controller in the chatViewController
     * and in the MainViewController to change the scene on the right side
     * for example via a button.
     */
    public void showMenuView() {
        borderPane.setCenter(sceneLoader.getMenuView());
        MenuViewController menuViewController = (MenuViewController) sceneLoader.getMenuViewController();
        menuViewController.setMainViewController(this);

        //loading chat in
        borderPane.setLeft(sceneLoader.getChatView());
        ChatViewController chatViewController = (ChatViewController) sceneLoader.getChatViewController();
        chatViewController.setMainViewController(this);
    }

    /**
     * Loads the GameLobbyView into the center of the main borderPane.
     * Additional this methode sets this controller in the gameLobbyViewController
     * to change the scene on the right side for example via a button.
     */
    public void showGameLobbyView() {
        borderPane.setCenter(sceneLoader.getGameLobbyView());
        GameLobbyViewController gameLobbyViewController = (GameLobbyViewController)
                sceneLoader.getGameLobbyViewController();
        gameLobbyViewController.setMainViewController(this);
    }

    /**
     * Loads the GameLobbyView into the center of the main borderPane.
     * Additional this methode sets this controller in the gameLobbyViewController
     * to change the scene on the right side for example via a button.
     */
    public void showGameView() {
        //TODO: currently it reloads the gameview to generate a random board
        // fix with an update function and REDUX
        //resets the GameView and generates the board new
        sceneLoader.loadGameView();

        borderPane.setCenter(sceneLoader.getGameView());
        GameViewController gameViewController = (GameViewController) sceneLoader.getGameViewController();
        gameViewController.setMainViewController(this);

        //TODO: Focus-management
        gameViewController.getGame_subscene().getRoot().requestFocus();
    }

    /**
     * Loads the iAmView into the center of the main borderPane.
     * Additional this methode sets this controller in the iAmViewController
     * to change the scene on the right side for example via a button.
     */
    public void showIAmView() {
        borderPane.setCenter(sceneLoader.getIAmView());
        IAmViewController iAmViewController = (IAmViewController) sceneLoader.getIAmViewController();
        iAmViewController.setMainViewController(this);
    }

    /**
     * Loads the gameSelectionView into the center of the main borderPane.
     * Additional this methode sets this controller in the gameSelectionViewController
     * to change the scene on the right side for example via a button.
     */
    public void showGameSelectionView() {
        borderPane.setCenter(sceneLoader.getGameSelectionView());
        GameSelectionViewController gameSelectionViewController = (GameSelectionViewController) sceneLoader.getGameSelectionViewController();
        gameSelectionViewController.setMainViewController(this);
    }
}
