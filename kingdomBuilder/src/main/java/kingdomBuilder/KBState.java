package kingdomBuilder;

import com.sun.javafx.scene.SceneEventDispatcher;
import kingdomBuilder.gui.SceneLoader;
import kingdomBuilder.gui.controller.MainViewController;
import kingdomBuilder.model.ClientDAO;
import kingdomBuilder.model.GameDAO;
import kingdomBuilder.network.Client;
import kingdomBuilder.network.protocol.server.ClientJoined;
import kingdomBuilder.network.protocol.server.ClientLeft;
import kingdomBuilder.network.protocol.server.Message;
import kingdomBuilder.network.util.Event;

import java.util.HashMap;
import java.util.Map;

public class KBState {
    // public for testing
    // probably just make these ObservableLists directly

    public MainViewController controller;
    // map of clientID -> client
    public Map<Integer, ClientDAO> clients = new HashMap<>();
    // map of gameID -> game
    public Map<Integer, GameDAO> games = new HashMap<>();
    // main client
    public Client client;
    public Thread clientThread;
    // preferred client name that gets passed to the server when we join
    public String clientPreferredName;
    // Client connection to server socket
    public boolean isConnected = false;
    // Address not found flag
    public boolean failedToConnect = false;

    public KBState() { }

    public KBState(KBState other) {
        controller = other.controller;
        clients = other.clients;
        games = other.games;
        client = other.client;
        clientThread = other.clientThread;
        clientPreferredName = other.clientPreferredName;
        isConnected = other.isConnected;
        failedToConnect = other.failedToConnect;
    }
}
