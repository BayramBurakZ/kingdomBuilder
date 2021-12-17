package kingdomBuilder.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import kingdomBuilder.KBState;
import kingdomBuilder.actions.SetMainControllerAction;
import kingdomBuilder.gui.SceneLoader;
import kingdomBuilder.redux.Store;

import java.net.URL;
import java.util.ResourceBundle;

/*
Views are stored in the sceneLoader-Object and generated when the sceneLoader object is generated
With sceneLoader.loadx() you reload the specific scene completely

All data will be stored in the states or substates of the store (REDUX)
TODO: Update functions for every scene to update its data from the REDUX subscriber system
 */

/**
 * This class controls which View is shown.
 */
public class MainViewController implements Initializable {
    /**
     * Represents the store of the application.
     */
    private final Store<KBState> store;
    /**
     * Represents the SceneLoader that contains all Views and Controllers.
     */
    private final SceneLoader sceneLoader;

    /**
     * Represents the BorderPane layout of the MainView.
     */
    @FXML
    private BorderPane borderPane;

    /**
     * Constructs the MainViewController.
     * @param store  The Application's store to set the field.
     */
    public MainViewController(Store<KBState> store) {
        this.store = store;
        this.sceneLoader = new SceneLoader(this.store);
    }

    /**
     * Called to initialize this controller after its root element has been completely processed.
     * @param location The location used to resolve relative paths for the root object,
     *                 or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        store.dispatch(new SetMainControllerAction(this));
        showIAmView();
    }

    /**
     * Loads the MenuView into the center of the main borderPane
     * and loads the chat into the left side of the main borderPane.
     * Additionally, this method sets this controller in the chatViewController
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
     * Additionally, this method sets this controller in the gameLobbyViewController
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
     * Additionally, this method sets this controller in the gameLobbyViewController
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
     * Additionally, this method sets this controller in the iAmViewController
     * to change the scene on the right side for example via a button.
     */
    public void showIAmView() {
        borderPane.setCenter(sceneLoader.getIAmView());
        IAmViewController iAmViewController = (IAmViewController) sceneLoader.getIAmViewController();
        iAmViewController.setMainViewController(this);
    }

    /**
     * Loads the gameSelectionView into the center of the main borderPane.
     * Additionally, this method sets this controller in the gameSelectionViewController
     * to change the scene on the right side for example via a button.
     */
    public void showGameSelectionView() {
        borderPane.setCenter(sceneLoader.getGameSelectionView());
        GameSelectionViewController gameSelectionViewController = (GameSelectionViewController) sceneLoader.getGameSelectionViewController();
        gameSelectionViewController.setMainViewController(this);
    }

    /**
     * Gets the SceneLoader
     * @return SceneLoader with all Views
     */
    public SceneLoader getSceneLoader(){
        return sceneLoader;
    }
}
