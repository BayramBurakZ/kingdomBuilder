package kingdomBuilder.model;

import kingdomBuilder.network.protocol.GameData;

import java.util.Objects;

/**
 * Stores information about the game.
 */
public final class GameDAO {
    /**
     * Represents the ID of the client who hosted the game.
     */
    private final int clientId;
    /**
     * Represents the type of the game.
     */
    private final String gameType;
    /**
     * Represents the ID of the game.
     */
    private final int gameId;
    /**
     * Represents the name of the game.
     */
    private final String gameName;
    /**
     * Represents the description of the game.
     */
    private final String gameDescription;
    /**
     * Represents the limit for players.
     */
    private final int playerLimit;
    /**
     * Represents the players who already joined.
     */
    private final int playersJoined;

    /**
     * Creates a new GameDAO object.
     * @param clientId The ID of the client who hosted this game.
     * @param gameType The type of the game.
     * @param gameId The ID of the game.
     * @param gameName The name of the game.
     * @param gameDescription The description of the game.
     * @param playerLimit The limit of players.
     * @param playersJoined The current joined players.
     */
    public GameDAO(
            int clientId,
            String gameType,
            int gameId,
            String gameName,
            String gameDescription,
            int playerLimit,
            int playersJoined
    ) {
        this.clientId = clientId;
        this.gameType = gameType;
        this.gameId = gameId;
        this.gameName = gameName;
        this.gameDescription = gameDescription;
        this.playerLimit = playerLimit;
        this.playersJoined = playersJoined;
    }

    /**
     * Creates a new GameDAO object.
     * @param gameData contains data of a game.
     */
    public GameDAO(GameData gameData) {
        this(gameData.clientId(),
                gameData.gameType(),
                gameData.gameId(),
                gameData.gameName(),
                gameData.gameDescription(),
                gameData.playerLimit(),
                gameData.playersJoined());
    }

    /**
     * Gets the ID of the client who hosted the game.
     * @return The ID of the client.
     */
    public int getClientId() {
        return clientId;
    }

    /**
     *{@return the type of a game.}
     */
    public String getGameType() {
        return gameType;
    }

    /**
     * {@return the ID of the game.}
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * {@return the name of the game.}
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * {@return the description of the game.}
     */
    public String getGameDescription() {
        return gameDescription;
    }

    /**
     * {@return the limit for players.}
     */
    public int getPlayerLimit() {
        return playerLimit;
    }

    /**
     * {@return the number of players currently in the game.}
     */
    public int getPlayersJoined() {
        return playersJoined;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (GameDAO) obj;
        return this.clientId == that.clientId &&
                Objects.equals(this.gameType, that.gameType) &&
                this.gameId == that.gameId &&
                Objects.equals(this.gameName, that.gameName) &&
                Objects.equals(this.gameDescription, that.gameDescription) &&
                this.playerLimit == that.playerLimit &&
                this.playersJoined == that.playersJoined;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(clientId, gameType, gameId, gameName, gameDescription, playerLimit, playersJoined);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "GameDAO[" +
                "clientId=" + clientId + ", " +
                "gameType=" + gameType + ", " +
                "gameId=" + gameId + ", " +
                "gameName=" + gameName + ", " +
                "gameDescription=" + gameDescription + ", " +
                "playerLimit=" + playerLimit + ", " +
                "playersJoined=" + playersJoined + ']';
    }
}
