package kingdomBuilder.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kingdomBuilder.gui.controller.GameScreenController;

public class KingdomBuilderApplication extends Application {

    GameScreenController gsc;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO: resources instead of path for .fxml-files
        // TODO: WARNING: Unsupported JavaFX configuration: classes were loaded from 'unnamed module @1933c126'

        /*

        // Setup Scene
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/kingdomBuilder.gui/GameScreen.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 1000, 600);

        // Setup css Stylesheet
        String css = this.getClass().getResource("/kingdomBuilder.gui/StyleSheet.css").toExternalForm();
        scene.getStylesheets().add(css);

         */

        /*
        TODO: achtet darauf, dass die .fxml-Files (und Java-Klassen natürlich) nicht zu "breit" werden, also dass der
         Code durch Zeilenumbrüche innerhalb der Linie bleibt, die IntelliJ  rechts anzeigt. Dann sieht man alles auf
         einmal.
         */

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/kingdomBuilder.gui/AltMainView.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 1000, 600);

        // Setup stage
        primaryStage.setTitle("KingdomBuilder v0.1 Chat Client");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(600);
        primaryStage.show();

        /*

        // init Controller
        gsc = (GameScreenController) fxmlLoader.getController();

        // resize events
        try {
            primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
                gsc.resizePanes(newVal, primaryStage.getHeight());
            });

            primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
                gsc.resizePanes(primaryStage.getWidth(), newVal);
            });
        } catch (Exception e) {
            System.out.println("Failed window adjustment");
        }

         */
    }
}