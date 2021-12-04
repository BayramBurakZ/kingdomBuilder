package kingdomBuilder.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import kingdomBuilder.gui.controller.Controller;

import java.io.IOException;

public class SceneLoader {
    private FXMLLoader fxmlLoader;

    private Node menuView;
    private Node gameLobbyView;
    private Node gameView;
    private Node iAmView;
    private Node chatView;

    private Controller menuViewController;
    private Controller gameLobbyViewController;
    private Controller gameViewController;
    private Controller iAmViewController;
    private Controller chatViewController;


    private class Tuple<X, Y> {
        public final X x;
        public final Y y;
        public Tuple(X x, Y y) {
            this.x = x;
            this.y = y;
        }
    }

    public SceneLoader() {
        loadMenuView();
        loadGameLobbyView();
        loadGameView();
        loadIAmView();
        loadChatView();
    }

    public Tuple<Node, Controller> loadView(String location) {
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

        Tuple<Node, Controller> tuple = new Tuple<>(node, controller);
        return tuple;
    }

    public void loadMenuView() {
        Tuple<Node, Controller> tuple = loadView("/kingdomBuilder.gui/controller/MenuView.fxml");
        menuView = tuple.x;
        menuViewController = tuple.y;
    }

    public void loadGameLobbyView() {
        Tuple<Node, Controller> tuple = loadView("/kingdomBuilder.gui/controller/GameLobbyView.fxml");
        gameLobbyView = tuple.x;
        gameLobbyViewController = tuple.y;
    }

    public void loadGameView() {
        Tuple<Node, Controller> tuple = loadView("/kingdomBuilder.gui/controller/GameView.fxml");
        gameView = tuple.x;
        gameViewController = tuple.y;
    }

    public void loadIAmView() {
        Tuple<Node, Controller> tuple = loadView("/kingdomBuilder.gui/controller/IAmView.fxml");
        iAmView = tuple.x;
        iAmViewController = tuple.y;
    }

    public void loadChatView() {
        Tuple<Node, Controller> tuple = loadView("/kingdomBuilder.gui/controller/ChatView.fxml");
        chatView = tuple.x;
        chatViewController = tuple.y;
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
}
