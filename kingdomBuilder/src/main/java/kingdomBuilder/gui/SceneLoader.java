package kingdomBuilder.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.util.Pair;
import kingdomBuilder.KBState;
import kingdomBuilder.gui.controller.*;
import kingdomBuilder.redux.Store;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;

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
     * Stores the menuViewController.
     */
    private Controller menuViewController;
    /**
     * Stores the gameLobbyController.
     */
    private Controller gameLobbyViewController;
    /**
     * Stores the gameViewController.
     */
    private Controller gameViewController;
    /**
     * Stores the iAmViewController.
     */
    private Controller iAmViewController;
    /**
     * Stores the chatViewController.
     */
    private Controller chatViewController;
    /**
     * Stores the gameSelectionViewController.
     */
    private Controller gameSelectionViewController;

    /**
     * Represents the store of the application.
     */
    private final Store<KBState> store;

    /**
     * Constructor that initially loads every view.
     */
    public SceneLoader(Store<KBState> store) {
        this.store = store;

        loadMenuView();
        loadGameLobbyView();
        loadGameView();
        loadIAmView();
        loadChatView();
        loadGameSelectionView();
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

    /**
     * Loads the View at the specific location of the parameter and put the view on the x-position of the
     * tuple and the corresponding controller at the  y-position of the tuple.
     *
     * @param location String that contains the path to the location of the .fxml file.
     * @return Tuple x-position is the View as Node, y-position is the contoller.
     */
    private Pair<Node, Controller> loadView(String location) {
        Node node = null;
        Controller controller = null;

        FXMLLoader loader = makeLoader(getClass().getResource(location));

        try {
            node = loader.load();
            controller =  loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Pair<>(node, controller);
    }

    /**
     * Calls the loadView() method with the path of the MenuView.fxml and
     * sets the menuView and menuViewController fields.
     * Used for reloading this View.
     */
    public void loadMenuView() {
        Pair<Node, Controller> pair = loadView("controller/MenuView.fxml");
        menuView = pair.getKey();
        menuViewController = pair.getValue();
    }

    /**
     * Calls the loadView() method with the path of the GameLobbyView.fxml and
     * sets the gameLobbyView and gameLobbyViewController fields
     * Used for reloading this View.
     */
    public void loadGameLobbyView() {
        Pair<Node, Controller> pair = loadView("controller/GameLobbyView.fxml");
        gameLobbyView = pair.getKey();
        gameLobbyViewController = pair.getValue();
    }

    /**
     * Calls the loadView() method with the path of the GameView.fxml and
     * sets the gameView and gameViewController fields
     * Used for reloading this View.
     */
    public void loadGameView() {
        Pair<Node, Controller> pair = loadView("controller/GameView.fxml");
        gameView = pair.getKey();
        gameViewController = pair.getValue();
    }

    /**
     * Calls the loadView() method with the path of the IAmView.fxml and
     * sets the iAmView and iAmViewController fields
     * Used for reloading this View.
     */
    public void loadIAmView() {
        Pair<Node, Controller> pair = loadView("controller/IAmView.fxml");
        iAmView = pair.getKey();
        iAmViewController = pair.getValue();
    }

    /**
     * Calls the loadView() method with the path of the ChatView.fxml and
     * sets the chatView and chatViewController fields
     * Used for reloading this View.
     */
    public void loadChatView() {
        Pair<Node, Controller> pair = loadView("controller/ChatView.fxml");
        chatView = pair.getKey();
        chatViewController = pair.getValue();
    }

    /**
     * Calls the loadView() method with the path of the GameSelectionView.fxml and
     * sets the gameSelectionView and gameSelectionViewController fields
     * Used for reloading this View.
     */
    public void loadGameSelectionView() {
        Pair<Node, Controller> pair = loadView("controller/GameSelectionView.fxml");
        gameSelectionView = pair.getKey();
        gameSelectionViewController = pair.getValue();
    }

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
}
