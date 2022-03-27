package kingdomBuilder.gui.util;

import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.SpotLight;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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

    private static Popup createPopup(final String message) {
        final Popup popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);
        Label exclamation = new Label("!");
        Label label = new Label(message);
        label.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                popup.hide();
            }
        });
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

    public static void showPopupMessage(final String message, final Stage stage) {
        final Popup popup = createPopup(message);
        popup.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                popup.setX(stage.getX() + stage.getWidth()/2 - popup.getWidth()/2);
                popup.setY(stage.getY() + stage.getHeight()/2 - popup.getHeight()/2);
            }
        });
        popup.show(stage);
    }

    public static void showLocalizedPopupMessage(final String key, final Stage stage) {
        Util.showPopupMessage(
                ResourceBundle.getBundle("kingdomBuilder/gui/gui", SceneLoader.getLocale())
                        .getString(key), stage);
    }
}
