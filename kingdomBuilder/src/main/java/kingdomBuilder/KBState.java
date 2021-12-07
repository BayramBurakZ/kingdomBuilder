package kingdomBuilder;

import kingdomBuilder.model.ClientDAO;
import kingdomBuilder.model.GameDAO;

import java.util.HashMap;
import java.util.Map;

public class KBState {
    // public for testing
    // probably just make these ObservableLists directly
    public Map<Integer, ClientDAO> clients = new HashMap<>();
    public Map<Integer, GameDAO> games = new HashMap<>();
    public int clientID;
    public String clientName;


    public KBState() { }

    public KBState(KBState other) {
        clients = new HashMap<>(other.clients);
        games = new HashMap<>(other.games);
        clientID = other.clientID;
    }
}
