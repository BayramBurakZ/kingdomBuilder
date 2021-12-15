package kingdomBuilder.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kingdomBuilder.KBState;
import kingdomBuilder.reducers.KBReducer;
import kingdomBuilder.redux.Store;

import java.lang.reflect.Constructor;
import java.net.URL;

public class KingdomBuilderApplication extends Application {
    private final Store<KBState> store = new Store<>(new KBState(), new KBReducer());

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

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO: resources instead of path for .fxml-files
        // TODO: WARNING: Unsupported JavaFX configuration: classes were loaded from 'unnamed module @1933c126'

        /*

        // Setup css Stylesheet
        String css = this.getClass().getResource("/kingdomBuilder.gui/StyleSheet.css").toExternalForm();
        scene.getStylesheets().add(css);

         */

        /*
        TODO: achtet darauf, dass die .fxml-Files (und Java-Klassen natürlich) nicht zu "breit" werden, also dass der
         Code durch Zeilenumbrüche innerhalb der Linie bleibt, die IntelliJ  rechts anzeigt. Dann sieht man alles auf
         einmal.
         */

        // FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/kingdomBuilder/gui/controller/MainView.fxml"));

        URL resource = getClass().getResource("controller/MainView.fxml");
        FXMLLoader fxmlLoader = makeLoader(resource);

        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 1000, 650);

        // Setup stage
        primaryStage.setTitle("KingdomBuilder v0.1 Chat Client");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(650);
        primaryStage.show();
    }

    // TODO: general closing mechanism
    @Override
    public void stop() throws Exception {
        super.stop();
    }
}