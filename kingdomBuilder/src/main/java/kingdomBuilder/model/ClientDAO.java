package kingdomBuilder.model;

/**
 * Stores information about the client.
 */
public class ClientDAO {
    /**
     * Represents the id of the client.
     */
    int id;
    /**
     * Represents the name of the client.
     */
    String name;
    /**
     * Represents the id of the game in which the client participates.
     */
    int gameId;

    /**
     * Represents the default constructor.
     */
    public ClientDAO() {
    }

    /**
     * Creates client object.
     * @param id id of the client.
     * @param name name of the client.
     * @param gameId id of the game in which the client participates.
     */
    public ClientDAO(int id, String name, int gameId) {
        this.id = id;
        this.name = name;
        this.gameId = gameId;
    }

    /**
     * Gets the id of the client.
     * @return the id of the client.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the client.
     * @param id id of the client.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the client.
     * @return the name of the client.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the client.
     * @param name name of the client.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the id of the game in which the client participates.
     * @return id of the game.
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * Sets the id of the game in which the client participates.
     * @param gameId id of the game.
     */
    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
}