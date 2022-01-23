package kingdomBuilder.actions;

import kingdomBuilder.network.protocol.BoardData;
import kingdomBuilder.redux.Action;

// TODO: please change this

/**
 * Represents the SetQuadrantIDsAction. Used for the {@link kingdomBuilder.redux.Store#dispatch(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he need to run.
 */
public class SetQuadrantIDsAction extends Action {

    /**
     * Represents the Data of a Board.
     */
    public final BoardData boardData;

    /**
     * Creates a new SetQuadrantIDsAction with a BoardData message.
     * @param boardData the BoardData message.
     */
    public SetQuadrantIDsAction(BoardData boardData) {
        this.boardData = boardData;
    }
}
