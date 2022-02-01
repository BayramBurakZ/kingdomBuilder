package kingdomBuilder.actions;

import kingdomBuilder.gui.SceneLoader;
import kingdomBuilder.redux.Action;

/**
 * Action to set the SceneLoader in the {@link kingdomBuilder.KBState state} of the store. So every View is
 * modifiable and all their methods are callable.
 */
public class SetSceneLoaderAction extends Action {
    /**
     * field for the SceneLoader so the reducer has access to it
     */
    public SceneLoader sceneLoader;

    /**
     * Constructor that creates a new SetSceneLoaderAction so the reducer can modify the controller inside the state
     * @param sceneLoader the SceneLoader that should be set inside the state
     */
    public SetSceneLoaderAction(SceneLoader sceneLoader) { this.sceneLoader = sceneLoader; }
}
