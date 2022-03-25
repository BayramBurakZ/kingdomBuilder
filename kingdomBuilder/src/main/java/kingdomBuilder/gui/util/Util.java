package kingdomBuilder.gui.util;

import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.SpotLight;
import javafx.scene.paint.Color;

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
}
