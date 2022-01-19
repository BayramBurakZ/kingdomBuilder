package kingdomBuilder.gui;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import kingdomBuilder.KBState;
import kingdomBuilder.actions.ApplicationExitAction;
import kingdomBuilder.network.ClientSelector;
import kingdomBuilder.reducers.KBReducer;
import kingdomBuilder.redux.Store;

/**
 * Represents the specified JavaFX application for the KingdomBuilder.
 */
public class KingdomBuilderApplication extends Application {

    /**
     * Stores the whole state of the application.
     */
    private Store<KBState> store;


    /**
     * Represents the SceneLoader that contains all Views and Controllers.
     */
    private SceneLoader sceneLoader;

    /**
     * Creates Scene object with given Stage object as root Node.
     * Attempts to show the stage/window.
     * @param primaryStage primary stage for this application onto which application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        // TODO: resources instead of path for .fxml-files
        // TODO: WARNING: Unsupported JavaFX configuration: classes were loaded from 'unnamed module @1933c126'

        sceneLoader = new SceneLoader(store);

        primaryStage.getIcons().add(new Image("kingdomBuilder/gui/textures/icons/StageIcon.png"));

        primaryStage.setTitle("KingdomBuilder");
        primaryStage.setScene(sceneLoader.getScene());
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(650);
        primaryStage.show();
    }

    /**
     * Prepares for application exit and destroys resources.
     * @throws Exception
     */
    // TODO: general closing mechanism
    @Override
    public void stop() throws Exception {
        store.dispatch(new ApplicationExitAction());

        super.stop();
    }
}