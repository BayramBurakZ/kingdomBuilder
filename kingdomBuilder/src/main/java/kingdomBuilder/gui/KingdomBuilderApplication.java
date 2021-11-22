package kingdomBuilder.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class KingdomBuilderApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO: resources instead of path for .fxml-files
        // TODO: WARNING: Unsupported JavaFX configuration: classes were loaded from 'unnamed module @1933c126'

        // Setup Scene
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/kingdomBuilder.gui/chat_view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 800, 600);

        // Setup stage
        primaryStage.setTitle("KingdomBuilder v0.1 Chat Client");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }
}
