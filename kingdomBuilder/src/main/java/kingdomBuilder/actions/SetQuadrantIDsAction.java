package kingdomBuilder.actions;

import kingdomBuilder.network.protocol.BoardData;
import kingdomBuilder.redux.Action;

// TODO: please change this
public class SetQuadrantIDsAction extends Action {

    public final BoardData boardData;

    public SetQuadrantIDsAction(BoardData boardData) {
        this.boardData = boardData;
    }
}
