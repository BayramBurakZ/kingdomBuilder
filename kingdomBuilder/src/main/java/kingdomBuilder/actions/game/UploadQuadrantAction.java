package kingdomBuilder.actions.game;

import kingdomBuilder.redux.Action;

/**
 * Represents the UploadQuadrantAction. Only triggered if another client left the server.
 * Used for the {@link kingdomBuilder.redux.Store#dispatchOld(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he needs to run.
 */
public class UploadQuadrantAction extends Action {
    /**
     * Represents the quadrant.
     */
    public String quadrant;

    /**
     * Creates a new UploadQuadrantAction.
     * @param quadrant the quadrant.
     */
    public UploadQuadrantAction(String quadrant) {
        this.quadrant = quadrant;
    }
}
