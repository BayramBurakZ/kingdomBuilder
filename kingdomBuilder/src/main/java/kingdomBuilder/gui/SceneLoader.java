package kingdomBuilder.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;
import kingdomBuilder.KBState;
import kingdomBuilder.actions.SetSceneLoaderAction;
import kingdomBuilder.gui.controller.*;
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
     * Stores the current menuView.
     */
    private Node menuView;
    /**
     * Stores the current gameLobbyView.
     */
    private Node gameLobbyView;
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
     * Stores the menuViewController.
     */
    private MenuViewController menuViewController;
    /**
     * Stores the gameLobbyController.
     */
    private GameLobbyViewController gameLobbyViewController;
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
     * Constructor that initially loads every view.
     */
    public SceneLoader(Store<KBState> store) {
        this.store = store;
        this.store.dispatch(new SetSceneLoaderAction(this));

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
        loadMenuView(locale);
        loadGameLobbyView(locale);
        loadGameView(locale);
        loadIAmView(locale);
        loadChatView(locale);
        loadGameSelectionView(locale);
        loadSettingsView(locale);
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
        borderPane.setCenter(menuView);

        borderPane.setLeft(chatView);
    }

    /**
     * Loads the GameLobbyView into the center of the main borderPane.
     */
    public void showGameLobbyView() {
        borderPane.setCenter(gameLobbyView);
    }

    /**
     * Loads the GameLobbyView into the center of the main borderPane.
     */
    public void showGameView() {
        //TODO: currently it reloads the gameview to generate a random board
        // fix with an update function and REDUX
        //resets the GameView and generates the board new
        loadGameView(Locale.ENGLISH);

        borderPane.setCenter(gameView);

        //TODO: Focus-management
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
        borderPane.setCenter(gameSelectionView);
    }

    /**
     * Loads the SettingsView into the center of the main borderPane.
     */
    public void showSettingsView() {
        borderPane.setCenter(settingsView);
    }

    // load-methods

    /**
     * Loads the View at the specific location of the parameter and put the view on the x-position of the
     * tuple and the corresponding controller at the  y-position of the tuple.
     *
     * @param location String that contains the path to the location of the .fxml file.
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
     * Calls the loadView() method with the path of the MenuView.fxml and
     * sets the menuView and menuViewController fields.
     * Used for reloading this View.
     * @param locale The locale for language support.
     */
    public void loadMenuView(Locale locale) {
        Pair<Node, Controller> pair = loadView("controller/MenuView.fxml", locale);
        menuView = pair.getKey();
        menuViewController = (MenuViewController) pair.getValue();
    }

    /**
     * Calls the loadView() method with the path of the GameLobbyView.fxml and
     * sets the gameLobbyView and gameLobbyViewController fields
     * Used for reloading this View.
     * @param locale The locale for language support.
     */
    public void loadGameLobbyView(Locale locale) {
        Pair<Node, Controller> pair = loadView("controller/GameLobbyView.fxml", locale);
        gameLobbyView = pair.getKey();
        gameLobbyViewController = (GameLobbyViewController) pair.getValue();
    }

    /**
     * Calls the loadView() method with the path of the GameView.fxml and
     * sets the gameView and gameViewController fields
     * Used for reloading this View.
     * @param locale The locale for language support.
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
     * @param locale The locale for language support.
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
     * @param locale The locale for language support.
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
     * @param locale The locale for language support.
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
     * @param locale The locale for language support.
     */
    public void loadSettingsView(Locale locale) {
        Pair<Node, Controller> pair = loadView("controller/SettingsView.fxml", locale);
        settingsView = pair.getKey();
        settingsViewController = (SettingsController) pair.getValue();
    }

    // getter

    /**
     * Returns the current parent node of the MenuView.
     * @return The current parent node of the MenuView.
     */
    public Node getMenuView() {
        return menuView;
    }
    /**
     * Returns the current parent node of the GameLobbyView.
     * @return The current parent node of the GameLobbyView.
     */
    public Node getGameLobbyView() {
        return gameLobbyView;
    }
    /**
     * Returns the current parent node of the GameView.
     * @return The current parent node of the GameView.
     */
    public Node getGameView() {
        return gameView;
    }
    /**
     * Returns the current parent node of the IAmView.
     * @return The current parent node of the IAmView.
     */
    public Node getIAmView() {
        return iAmView;
    }
    /**
     * Returns the current parent node of the ChatView.
     * @return The current parent node of the ChatView.
     */
    public Node getChatView() {
        return chatView;
    }
    /**
     * Returns the current parent node of the GameSelectionView.
     * @return The current parent node of the GameSelectionView.
     */
    public Node getGameSelectionView() {
        return gameSelectionView;
    }
    /**
     * Returns the current parent node of the SettingsView.
     * @return The current parent node of the SettingsView.
     */
    public Node getSettingsView() {
        return settingsView;
    }

    /**
     * Returns the MenuViewController.
     * @return The MenuViewController with all functionalities for the MenuView.
     */
    public MenuViewController getMenuViewController() {
        return (MenuViewController) menuViewController;
    }
    /**
     * Returns the GameLobbyViewController.
     * @return The GameLobbyViewController with all functionalities for the GameLobbyView.
     */
    public GameLobbyViewController getGameLobbyViewController() {
        return (GameLobbyViewController) gameLobbyViewController;
    }
    /**
     * Returns the GameViewController.
     * @return The GameViewController with all functionalities for the GameView.
     */
    public GameViewController getGameViewController() {
        return (GameViewController) gameViewController;
    }
    /**
     * Returns the IAmViewController.
     * @return The IAmViewController with all functionalities for the IAmView.
     */
    public IAmViewController getIAmViewController() {
        return (IAmViewController) iAmViewController;
    }
    /**
     * Returns the ChatViewController.
     * @return The ChatViewController with all functionalities for the ChatView.
     */
    public ChatViewController getChatViewController() {
        return (ChatViewController) chatViewController;
    }
    /**
     * Returns the GameSelectionViewController.
     * @return The GameSelectionViewController with all functionalities for the GameSelectionView.
     */
    public GameSelectionViewController getGameSelectionViewController() {
        return (GameSelectionViewController) gameSelectionViewController;
    }

    /**
     * Creates a new loader, with a custom controller factory, which passes on custom parameters on controller
     * construction.
     * @param location The location to load the FXML file from.
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
}