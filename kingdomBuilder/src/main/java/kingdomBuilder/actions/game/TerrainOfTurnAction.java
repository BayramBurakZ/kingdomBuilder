package kingdomBuilder.actions.game;

import kingdomBuilder.network.protocol.TerrainTypeOfTurn;
import kingdomBuilder.redux.Action;

/**
 * Represents the TerrainOfTurnAction. Used for the {@link kingdomBuilder.redux.Store#dispatch(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he needs to run.
 */
public class TerrainOfTurnAction extends Action {

    /**
     * Represents the network message for the terrainCard.
     */
    public TerrainTypeOfTurn terrainTypeOfTurn;

    /**
     * Creates a new TerrainOfTurnAction with the given network message.
     * @param terrainTypeOfTurn the message.
     */
    public TerrainOfTurnAction(TerrainTypeOfTurn terrainTypeOfTurn) {
        this.terrainTypeOfTurn = terrainTypeOfTurn;
    }
}
