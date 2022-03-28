package kingdomBuilder.gui.util;

import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.SpotLight;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;
import kingdomBuilder.gui.SceneLoader;

import java.util.ResourceBundle;

/**
 * Class containing miscellaneous utility functions used by the GUI.
 */
public class Util {

    /**
     * Sets the initial light for a board.
     * @param boardGroup the group where the light is added to.
     * @param boardCenter the center of the board where the spotlight is directed to.
     */
    public static void setupLight(Group boardGroup, Point3D boardCenter) {
        AmbientLight al = new AmbientLight(Color.gray(0.4));
        boardGroup.getChildren().add(al);

        // for some reason JavaFX doesn't support vector light/sunlight
        SpotLight sl = new SpotLight(Color.gray(0.6));
        boardGroup.getChildren().add(sl);

        sl.setTranslateX(boardCenter.getX());
        sl.setTranslateY(boardCenter.getY());
        sl.setTranslateZ(-7000);
    }

    /**
     * Creates a popup warning message with the provided text content.
     * @param message the text content of the popup message.
     * @return the new popup message.
     */
    private static Popup createPopup(final String message) {
        final Popup popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);
        Label exclamation = new Label("!");
        Label label = new Label(message);
        label.setOnMouseReleased(e -> popup.hide());

        //label.getStylesheets().add("/css/styles.css");
        //label.getStyleClass().add("popup");

        exclamation.setStyle("""
        -fx-padding: 5;
        -fx-font-size: 32;
        -fx-text-fill: red;
        """);

        label.setStyle("""
        -fx-background-color: rgba(255,100,100,0.95);
        -fx-padding: 5;
        -fx-border-color: #ff0000;\s
        -fx-border-width: 1;
        -fx-font-size: 12;
        """);

        HBox hbox = new HBox(exclamation, label);
        hbox.setAlignment(Pos.CENTER);
        popup.getContent().add(hbox);
        return popup;
    }

    /**
     * Creates a popup warning message on the specified stage with the provided text content.
     * @param message the text content of the popup message.
     * @param stage the stage to display the popup on.
     */
    public static void showPopupMessage(final String message, final Stage stage) {
        final Popup popup = createPopup(message);
        popup.setOnShown(e -> {
            popup.setX(stage.getX() + stage.getWidth()/2 - popup.getWidth()/2);
            popup.setY(stage.getY() + stage.getHeight()/2 - popup.getHeight()/2);
        });
        popup.show(stage);
    }

    /**
     * Creates a popup warning message on the specified stage with the localized text content corresponding to the
     * provided key as determined in the resource bundle 'gui'.
     * @param key the key specifying the localized text content as determined in the resource bundle 'gui'.
     * @param stage the stage to display the popup on.
     */
    public static void showLocalizedPopupMessage(final String key, final Stage stage) {
        Util.showPopupMessage(
                ResourceBundle.getBundle("kingdomBuilder/gui/gui", SceneLoader.getLocale())
                        .getString(key), stage);
    }
}
