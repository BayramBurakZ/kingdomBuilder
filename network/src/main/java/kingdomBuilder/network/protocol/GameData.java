package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents the game data.
 */
@Protocol(format = "[#{clientId};#{gameType};#{gameId};#{gameName};#{gameDescription};#{playerLimit};#{playersJoined}]", isComponent = true)
public record GameData(
        int clientId,
        String gameType,
        int gameId,
        String gameName,
        String gameDescription,
        int playerLimit,
        int playersJoined
) {
    /**
     * Gets the id of the game.
     * @return id of the game.
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * Gets name of the game.
     * @return name of the game.
     */
    public String getGameName() {
        return gameName;
    }
}
