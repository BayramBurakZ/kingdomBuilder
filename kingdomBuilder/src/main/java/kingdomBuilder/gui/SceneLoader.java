package kingdomBuilder.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;
import kingdomBuilder.KBState;
import kingdomBuilder.gui.controller.*;
import kingdomBuilder.gui.levelEditor.LevelEditorController;
import kingdomBuilder.network.protocol.Scores;
import kingdomBuilder.reducers.ApplicationReducer;
import kingdomBuilder.redux.Store;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class that is used for creating an object that contains every scene.
 * With the construction of it sets all fields to the corresponding scene and controller.
 * (The following underscores stand for the specific View)
 * The load_View() methode is for initial loading of all scenes and for reloading them.
 * With the get_View() you get the specific view.
 */
public class SceneLoader {

    /**
     * Defines how long the animation is.
     */
    public static final double ANIMATION_TIME = 1000;

    //region Nodes for every View

    /**
     * Stores the current menuView.
     */
    private Node menuView;
    /**
     * Stores the current gameSettingsView.
     */
    private Node gameSettingsView;
    /**
     * Stores the current gameView.
     */
    private Node gameView;
    /**
     * Stores the current iAmView.
     */
    private Node iAmView;
    /**
     * Stores the current chatView.
     */
    private Node chatView;
    /**
     * Stores the current gameSelectionView.
     */
    private Node gameSelectionView;
    /**
     * Stores the current settingsView.
     */
    private Node settingsView;
    /**
     * Stores the current serverConnectView.
     */
    private Node serverConnectView;
    /**
     * Stores the current levelEditorView.
     */
    private Node levelEditorView;
    /**
     * Stores the current winView.
     */
    private Node winView;

    //endregion Nodes

    //region Controller for every View

    /**
     * Stores the menuViewController.
     */
    private MenuViewController menuViewController;
    /**
     * Stores the gameSettingsController.
     */
    private GameSettingsViewController gameSettingsViewController;
    /**
     * Stores the gameViewController.
     */
    private GameViewController gameViewController;
    /**
     * Stores the iAmViewController.
     */
    private IAmViewController iAmViewController;
    /**
     * Stores the chatViewController.
     */
    private ChatViewController chatViewController;
    /**
     * Stores the gameSelectionViewController.
     */
    private GameSelectionViewController gameSelectionViewController;
    /**
     * Stores the settingsViewController
     */
    private SettingsController settingsViewController;
    /**
     * Stores the serverConnectViewController.
     */
    private ServerConnectViewController serverConnectViewController;
    /**
     * Stores the levelEditorController.
     */
    private LevelEditorController levelEditorController;
    /**
     * Stores the winViewController.
     */
    private WinViewController winViewController;

    //endregion Controller for every View

    /**
     * Represents the store of the application.
     */
    private final Store<KBState> store;

    /**
     * Represents the main scene of the application.
     */
    private Scene scene;
    /**
     * Represents the main layout of the application.
     */
    private BorderPane borderPane;

    /**
     * Represents the locale for language settings.
     */
    private static Locale locale;

    /**
     * Constructor that initially loads every view.
     * @param store the Store to have access to the state.
     */
    public SceneLoader(Store<KBState> store) {
        this.store = store;
        this.store.dispatch(ApplicationReducer.SET_SCENELOADER, this);

        Locale initialLocale = Locale.ENGLISH;
        loadViews(initialLocale);

        borderPane = new BorderPane();
        borderPane.setCenter(iAmView);
        scene = new Scene(borderPane, 1000, 650);
    }

    /**
     * loads every view with the given locale.
     * @param locale the language in which the views are loaded.
     */
    public void loadViews(Locale locale) {
        SceneLoader.locale = locale;
        loadMenuView(locale);
        loadGameSettingsView(locale);
        loadGameView(locale);
        loadIAmView(locale);
        loadChatView(locale);
        loadGameSelectionView(locale);
        loadSettingsView(locale);
        loadServerConnectView(locale);
        loadWinView(locale);
    }

    /**
     * Returns the main scene for the application.
     * @return main scene.
     */
    public Scene getScene() {
        return scene;
    }

    // show-methods

    /**
     * Loads the MenuView into the center of the main borderPane
     * and loads the chat into the left side of the main borderPane.
     */
    public void showMenuView() {
        // remove Chat
        if (borderPane.getLeft() != null)
            borderPane.setLeft(null);

        // set main menu
        borderPane.setCenter(menuView);
    }

    /**
     * Loads the GameSettingsView into the center of the main borderPane.
     * @param isOnlineGame if the game is online.
     */
    public void showGameSettingsView(boolean isOnlineGame) {
        //reloads the view so its completely empty
        loadGameSettingsView(locale);

        borderPane.setCenter(gameSettingsView);
        gameSettingsViewController.setIsOnlineGame(isOnlineGame);
    }

    /**
     * Loads the gameView into the center of the main borderPane.
     * @param isSpectating whether the user wants to spectate.
     * @param isOnline whether the game is online.
     */
    public void showGameView(boolean isSpectating, boolean isOnline) {
        // set Chat
        if (borderPane.getLeft() == null)
            borderPane.setLeft(chatView);


        // reloads the view so its completely empty
        loadGameView(locale);

        // loads the gameView
        borderPane.setCenter(gameView);
        gameViewController.setSpectating(isSpectating);
        gameViewController.setIsOnline(isOnline);

        gameViewController.getGame_subscene().getRoot().requestFocus();
    }

    /**
     * Loads the iAmView into the center of the main borderPane.
     */
    public void showIAmView() {
        borderPane.setCenter(iAmView);
    }

    /**
     * Loads the gameSelectionView into the center of the main borderPane.
     */
    public void showGameSelectionView() {
        // set Chat
        if (borderPane.getLeft() == null)
            borderPane.setLeft(chatView);

        //reloads the view so its completely empty
        loadGameSelectionView(locale);

        borderPane.setCenter(gameSelectionView);
    }

    /**
     * Loads the SettingsView into the center of the main borderPane.
     */
    public void showSettingsView() {
        //reloads the view so its completely empty
        loadSettingsView(locale);

        borderPane.setCenter(settingsView);
    }

    /**
     * Loads the ServerConnectView into the center of the main borderPane.
     */
    public void showServerConnectView() {
        //reloads the view so its completely empty
        loadServerConnectView(locale);

        borderPane.setCenter(serverConnectView);
    }

    /**
     * Loads the LevelEditorView into the center of the main borderPane.
     */
    public void showLevelEditorView() {
        //reloads the view so its completely empty
        loadLevelEditorView(locale);

        borderPane.setCenter(levelEditorView);
    }

    /**
     * Loads the WinView into the center of the main borderPane.
     * @param scores the message containing the scores of the players.
     */
    public void showWinView(Scores scores) {
        //reloads the view so its completely empty
        loadWinView(locale);
        winViewController.setScoreWithName(scores);

        borderPane.setCenter(winView);
    }

    // load-methods

    /**
     * Loads the View at the specific location of the parameter and put the view on the x-position of the
     * tuple and the corresponding controller at the  y-position of the tuple.
     *
     * @param location string that contains the path to the location of the .fxml file.
     * @return Tuple x-position is the View as Node, y-position is the contoller.
     */
    private Pair<Node, Controller> loadView(String location, Locale locale) {
        Node node = null;
        Controller controller = null;

        FXMLLoader loader = makeLoader(getClass().getResource(location));
        loader.setResources(ResourceBundle.getBundle("kingdomBuilder/gui/gui", locale));

        try {
            node = loader.load();
            controller = loader.getController();
            controller.setSceneLoader(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Pair<>(node, controller);
    }

    /**
     * Creates a new loader, with a custom controller factory, which passes on custom parameters on controller
     * construction.
     * @param location the location to load the FXML file from.
     * @return The newly created loader.
     */
    private FXMLLoader makeLoader(URL location) {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(controllerType -> {
            try {
                for (Constructor<?> ctor: controllerType.getConstructors()) {
                    if(ctor.getParameterCount() == 1
                            && ctor.getParameterTypes()[0] == Store.class)
                        return ctor.newInstance(store);
                }

                return controllerType.getConstructor().newInstance();
            } catch(Exception exc) {
                throw new RuntimeException(exc);
            }
        });

        loader.setLocation(location);

        return loader;
    }

    /**
     * Calls the loadView() method with the path of the MenuView.fxml and
     * sets the menuView and menuViewController fields.
     * Used for reloading this View.
     * @param locale the locale for language support.
     */
    public void loadMenuView(Locale locale) {
        Pair<Node, Controller> pair = loadView("controller/MenuView.fxml", locale);
        menuView = pair.getKey();
        menuViewController = (MenuViewController) pair.getValue();
    }

    /**
     * Calls the loadView() method with the path of the GameSettingsView.fxml and
     * sets the gameSettingsView and gameSettingsViewController fields
     * Used for reloading this View.
     * @param locale the locale for language support.
     */
    public void loadGameSettingsView(Locale locale) {
        Pair<Node, Controller> pair = loadView("controller/GameSettingsView.fxml", locale);
        gameSettingsView = pair.getKey();
        gameSettingsViewController = (GameSettingsViewController) pair.getValue();
    }

    /**
     * Calls the loadView() method with the path of the GameView.fxml and
     * sets the gameView and gameViewController fields
     * Used for reloading this View.
     * @param locale the locale for language support.
     */
    public void loadGameView(Locale locale) {
        Pair<Node, Controller> pair = loadView("controller/GameView.fxml", locale);
        gameView = pair.getKey();
        gameViewController = (GameViewController) pair.getValue();
    }

    /**
     * Calls the loadView() method with the path of the IAmView.fxml and
     * sets the iAmView and iAmViewController fields
     * Used for reloading this View.
     * @param locale the locale for language support.
     */
    public void loadIAmView(Locale locale) {
        Pair<Node, Controller> pair = loadView("controller/IAmView.fxml", locale);
        iAmView = pair.getKey();
        iAmViewController = (IAmViewController) pair.getValue();
    }

    /**
     * Calls the loadView() method with the path of the ChatView.fxml and
     * sets the chatView and chatViewController fields
     * Used for reloading this View.
     * @param locale the locale for language support.
     */
    public void loadChatView(Locale locale) {
        Pair<Node, Controller> pair = loadView("controller/ChatView.fxml", locale);
        chatView = pair.getKey();
        chatViewController = (ChatViewController) pair.getValue();
    }

    /**
     * Calls the loadView() method with the path of the GameSelectionView.fxml and
     * sets the gameSelectionView and gameSelectionViewController fields
     * Used for reloading this View.
     * @param locale the locale for language support.
     */
    public void loadGameSelectionView(Locale locale) {
        Pair<Node, Controller> pair = loadView("controller/GameSelectionView.fxml", locale);
        gameSelectionView = pair.getKey();
        gameSelectionViewController = (GameSelectionViewController) pair.getValue();
    }

    /**
     * Calls the loadView() method with the path of the GameSelectionView.fxml and
     * sets the gameSelectionView and gameSelectionViewController fields
     * Used for reloading this View.
     * @param locale the locale for language support.
     */
    public void loadSettingsView(Locale locale) {
        Pair<Node, Controller> pair = loadView("controller/SettingsView.fxml", locale);
        settingsView = pair.getKey();
        settingsViewController = (SettingsController) pair.getValue();
    }

    /**
     * Calls the loadView() method with the path of the ServerConnect.fxml and
     * sets the serverConnectView and serverConnectViewController fields
     * Used for reloading this View.
     * @param locale the locale for language support.
     */
    public void loadServerConnectView(Locale locale) {
        Pair<Node, Controller> pair = loadView("controller/ServerConnect.fxml", locale);
        serverConnectView = pair.getKey();
        serverConnectViewController = (ServerConnectViewController) pair.getValue();
    }

    /**
     * Calls the loadView() method with the path of the LevelEditor.fxml and
     * sets the LevelEditorView and LevelEditorViewController fields
     * Used for reloading this View.
     * @param locale the locale for language support.
     */
    public void loadLevelEditorView(Locale locale) {
        Pair<Node, Controller> pair = loadView("controller/LevelEditor.fxml", locale);
        levelEditorView = pair.getKey();
        levelEditorController = (LevelEditorController) pair.getValue();
    }

    /**
     * Calls the loadView() method with the path of the LevelEditor.fxml and
     * sets the WinView and WinViewController fields
     * Used for reloading this View.
     * @param locale the locale for language support.
     */
    public void loadWinView(Locale locale) {
        Pair<Node, Controller> pair = loadView("controller/WinView.fxml", locale);
        winView = pair.getKey();
        winViewController = (WinViewController) pair.getValue();
    }

    /**
     * Returns the current IAmViewController.
     * @return The IAmViewController with all functionalities for the IAmView.
     */
    public IAmViewController getIAmViewController() {
        return iAmViewController;
    }

    /**
     * Gets the current locale.
     * @return the locale.
     */
    public static Locale getLocale() {
        return locale;
    }
}