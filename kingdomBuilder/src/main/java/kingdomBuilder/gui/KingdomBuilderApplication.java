package kingdomBuilder.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kingdomBuilder.KBState;
import kingdomBuilder.actions.ApplicationExitAction;
import kingdomBuilder.network.ClientSelector;
import kingdomBuilder.reducers.KBReducer;
import kingdomBuilder.redux.Store;

import java.lang.reflect.Constructor;
import java.net.URL;

/**
 * Represents the specified JavaFX application for the KingdomBuilder.
 */
public class KingdomBuilderApplication extends Application {

    /**
     * Stores the whole state of the application.
     */
    private Store<KBState> store;


    /**
     * Creates a new loader, with a custom controller factory, which passes on custom parameters on controller
     * construction.
     * @param resource location of the MainView XML document.
     * @return the FXMLLoader of the application.
     */
    private FXMLLoader makeLoader(URL resource) {
        FXMLLoader loader = new FXMLLoader(resource);
        loader.setControllerFactory(controllerType -> {
            try {
                for(Constructor<?> ctor: controllerType.getConstructors()) {
                    if(ctor.getParameterCount() == 1 && ctor.getParameterTypes()[0] == Store.class)
                        return ctor.newInstance(store);
                }

                return controllerType.getConstructor().newInstance();
            } catch(Exception exc) {
                throw new RuntimeException(exc);
            }
        });

        return loader;
    }

    /**
     * Creates Scene object with given Stage object as root Node.
     * Attempts to show the stage/window.
     * @param primaryStage primary stage for this application onto which application scene can be set.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        // TODO: resources instead of path for .fxml-files
        // TODO: WARNING: Unsupported JavaFX configuration: classes were loaded from 'unnamed module @1933c126'

        /*
        TODO: achtet darauf, dass die .fxml-Files (und Java-Klassen natürlich) nicht zu "breit" werden, also dass der
         Code durch Zeilenumbrüche innerhalb der Linie bleibt, die IntelliJ  rechts anzeigt. Dann sieht man alles auf
         einmal.
         */

        store = new Store<>(new KBState(), new KBReducer());

        URL resource = getClass().getResource("controller/MainView.fxml");
        FXMLLoader fxmlLoader = makeLoader(resource);

        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 1000, 650);

        primaryStage.setTitle("KingdomBuilder v0.1 Chat Client");
        primaryStage.setScene(scene);
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