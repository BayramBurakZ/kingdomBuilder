package kingdomBuilder.gui.controller;

import kingdomBuilder.KBState;
import kingdomBuilder.gui.SceneLoader;
import kingdomBuilder.redux.Store;

/**
 * Basic class that every controller inherit so every ViewController is able to cast into this.
 */
public class Controller {
    /**
     * Represents the SceneLoader for access to switch Views-methods.
     */
    protected SceneLoader sceneLoader;

    /**
     * Represents the store of the application.
     */
    protected Store<KBState> store;

    /**
     * Sets the SceneLoader.
     * @param sceneLoader sceneLoader with all functions.
     */
    public void setSceneLoader(SceneLoader sceneLoader) {
        this.sceneLoader = sceneLoader;
    }

    /**
     * Sets the Store.
     * @param store the Store with the current state.
     */
    public void setStore(Store<KBState> store) {
        this.store = store;
    }
}
