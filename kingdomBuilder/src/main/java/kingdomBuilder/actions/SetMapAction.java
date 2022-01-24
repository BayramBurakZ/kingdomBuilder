package kingdomBuilder.actions;

import kingdomBuilder.network.protocol.BoardData;
import kingdomBuilder.redux.Action;

/**
 * Represents the SetMapAction. Used for the {@link kingdomBuilder.redux.Store#dispatch(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he needs to run.
 */
public class SetMapAction extends Action {

    /**
     * Represents the Data of a Board.
     */
    public final BoardData boardData;

    /**
     * Creates a new SetMapAction with a BoardData message.
     * @param boardData the BoardData message.
     */
    public SetMapAction(BoardData boardData) {
        this.boardData = boardData;
    }
}
