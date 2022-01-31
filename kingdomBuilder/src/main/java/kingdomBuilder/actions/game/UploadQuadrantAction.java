package kingdomBuilder.actions.game;

import kingdomBuilder.redux.Action;

public class UploadQuadrantAction extends Action {
    public String quadrant;

    public UploadQuadrantAction(String quadrant) {
        this.quadrant = quadrant;
    }
}
