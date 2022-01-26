package kingdomBuilder.gamelogic;

import kingdomBuilder.network.protocol.MyGameReply;
import kingdomBuilder.network.protocol.PlayerData;
import kingdomBuilder.network.protocol.TerrainTypeOfTurn;

import java.util.LinkedHashSet;

/**
 * Class for storing all relevant information for a game before it starts.
 * Includes the WinConditions.
 */
public record GameInfo(LinkedHashSet<PlayerData> playersOfGame,
                       MyGameReply gameInformation,
                       MapReadOnly map,
                       Game.WinCondition[] winConditions,
                       TerrainTypeOfTurn terrainTypeOfTurn) {

}
