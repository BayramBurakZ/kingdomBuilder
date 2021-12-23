package kingdomBuilder.gui.controller;

import kingdomBuilder.gui.SceneLoader;

/**
 * Basic class that every controller inherit so every ViewController is able to cast into this.
 */
public class Controller {
    /**
     * Represents the SceneLoader for access to switch Views-methods.
     */
    protected SceneLoader sceneLoader;

    /**
     * Sets the SceneLoader.
     * @param sceneLoader SceneLoader with all functions.
     */
    public void setSceneLoader(SceneLoader sceneLoader) {
        this.sceneLoader = sceneLoader;
    }
}
