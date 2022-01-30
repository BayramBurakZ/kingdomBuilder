package kingdomBuilder.actions.game;

import kingdomBuilder.network.protocol.Scores;
import kingdomBuilder.redux.Action;

/**
 * Class that represents the score action whenever the server sends us the score.
 *
 * Represents the ScoreAction. Used for the {@link kingdomBuilder.redux.Store#dispatch(kingdomBuilder.redux.Action)
 * dispatch()}-methode in the {@link kingdomBuilder.redux.Store Store} so the reducer
 * knows what type of action he needs to run.
 */
public class ScoreAction extends Action {

    /**
     * Represents the score message from the network.
     */
    public Scores scores;

    /**
     * Creates a new ScoreAction.
     * @param scores the score message from the network.
     */
    public ScoreAction(Scores scores) {
        this.scores = scores;
    }
}
