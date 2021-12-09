package kingdomBuilder;

import kingdomBuilder.model.ClientDAO;
import kingdomBuilder.model.GameDAO;
import kingdomBuilder.network.Client;

import java.util.HashMap;
import java.util.Map;

public class KBState {
    // public for testing
    // probably just make these ObservableLists directly

    // map of clientID -> client
    public Map<Integer, ClientDAO> clients = new HashMap<>();
    // map of gameID -> game
    public Map<Integer, GameDAO> games = new HashMap<>();
    // main client
    public Client client;
    // preferred client name that gets passed to the server when we join
    public String clientPreferredName;

    public KBState() { }

    public KBState(KBState other) {
        clients = new HashMap<>(other.clients);
        games = new HashMap<>(other.games);
        client = other.client;
        clientPreferredName = other.clientPreferredName;
    }
}
