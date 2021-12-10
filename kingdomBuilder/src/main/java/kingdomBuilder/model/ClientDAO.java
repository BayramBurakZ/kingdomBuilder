package kingdomBuilder.model;

public class ClientDAO {

    int id;
    String name;
    int gameId;

    public ClientDAO() {
    }

    public ClientDAO(int id, String name, int gameId) {
        this.id = id;
        this.name = name;
        this.gameId = gameId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
}