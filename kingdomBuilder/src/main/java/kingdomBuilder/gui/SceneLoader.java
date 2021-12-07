package kingdomBuilder.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import kingdomBuilder.gui.controller.Controller;

import java.io.IOException;

/**
 * Class that is used for creating an object that contains every scene.
 * With the construction of it sets all fields to the corresponding scene and controller.
 * (The following underscores stand for the specific View)
 * The load_View() methode is for initial loading of all scenes and for reloading them.
 * With the get_View() you get the specific view.
 */
public class SceneLoader {
    private FXMLLoader fxmlLoader;

    private Node menuView;
    private Node gameLobbyView;
    private Node gameView;
    private Node iAmView;
    private Node chatView;
    private Node gameSelectionView;

    private Controller menuViewController;
    private Controller gameLobbyViewController;
    private Controller gameViewController;
    private Controller iAmViewController;
    private Controller chatViewController;
    private Controller gameSelectionViewController;

    /**
     * This method is used for generating a datastructure for returning two objects at the same time.
     * Currently only used for returning a node and the controller at the same time
     * @param <X> first object to save in the x-position of the tuple
     * @param <Y> second object to save in the y-position of the tuple
     */
    private class Tuple<X, Y> {
        public final X x;
        public final Y y;
        public Tuple(X x, Y y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Constructor that initially loads every view
     */
    public SceneLoader() {
        loadMenuView();
        loadGameLobbyView();
        loadGameView();
        loadIAmView();
        loadChatView();
        loadGameSelectionView();
    }

    /**
     * Loads the View at the specific location of the parameter and put the view on the x-position of the
     * tuple and the corresponding controller at the  y-position of the tuple
     *
     * @param location String that contains the path to the location of the .fxml file
     * @return Tuple x-position is the View as Node, y-position is the contoller
     */
    private Tuple<Node, Controller> loadView(String location) {
        Node node = null;
        Controller controller = null;

        fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(location));
        try {
            node = fxmlLoader.load();
            controller =  fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Tuple<>(node, controller);
    }

    /**
     * Calls the loadView() method with the path of the MenuView.fxml and
     * sets the menuView and menuViewController fields.
     * Used for reloading this View.
     */
    public void loadMenuView() {
        Tuple<Node, Controller> tuple = loadView("/kingdomBuilder.gui/controller/MenuView.fxml");
        menuView = tuple.x;
        menuViewController = tuple.y;
    }

    /**
     * Calls the loadView() method with the path of the GameLobbyView.fxml and
     * sets the gameLobbyView and gameLobbyViewController fields
     * Used for reloading this View.
     */
    public void loadGameLobbyView() {
        Tuple<Node, Controller> tuple = loadView("/kingdomBuilder.gui/controller/GameLobbyView.fxml");
        gameLobbyView = tuple.x;
        gameLobbyViewController = tuple.y;
    }

    /**
     * Calls the loadView() method with the path of the GameView.fxml and
     * sets the gameView and gameViewController fields
     * Used for reloading this View.
     */
    public void loadGameView() {
        Tuple<Node, Controller> tuple = loadView("/kingdomBuilder.gui/controller/GameView.fxml");
        gameView = tuple.x;
        gameViewController = tuple.y;
    }

    /**
     * Calls the loadView() method with the path of the IAmView.fxml and
     * sets the iAmView and iAmViewController fields
     * Used for reloading this View.
     */
    public void loadIAmView() {
        Tuple<Node, Controller> tuple = loadView("/kingdomBuilder.gui/controller/IAmView.fxml");
        iAmView = tuple.x;
        iAmViewController = tuple.y;
    }

    /**
     * Calls the loadView() method with the path of the ChatView.fxml and
     * sets the chatView and chatViewController fields
     * Used for reloading this View.
     */
    public void loadChatView() {
        Tuple<Node, Controller> tuple = loadView("/kingdomBuilder.gui/controller/ChatView.fxml");
        chatView = tuple.x;
        chatViewController = tuple.y;
    }

    /**
     * Calls the loadView() method with the path of the GameSelectionView.fxml and
     * sets the gameSelectionView and gameSelectionViewController fields
     * Used for reloading this View.
     */
    public void loadGameSelectionView() {
        Tuple<Node, Controller> tuple = loadView("/kingdomBuilder.gui/controller/GameSelectionView.fxml");
        gameSelectionView = tuple.x;
        gameSelectionViewController = tuple.y;
    }

    public Node getMenuView() {
        return menuView;
    }
    public Node getGameLobbyView() {
        return gameLobbyView;
    }
    public Node getGameView() {
        return gameView;
    }
    public Node getIAmView() {
        return iAmView;
    }
    public Node getChatView() {
        return chatView;
    }
    public Node getGameSelectionView() { return gameSelectionView; }

    public Controller getMenuViewController() {
        return menuViewController;
    }
    public Controller getGameLobbyViewController() {
        return gameLobbyViewController;
    }
    public Controller getGameViewController() {
        return gameViewController;
    }
    public Controller getIAmViewController() {
        return iAmViewController;
    }
    public Controller getChatViewController() {
        return chatViewController;
    }
    public Controller getGameSelectionViewController() { return gameSelectionViewController; }
}
