package kingdomBuilder.gui.gameboard;


import javafx.geometry.Point2D;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import kingdomBuilder.gui.util.HexagonCalculator;

import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Class to create a hexagon, which displays the win condition as Tooltip.
 */
public class Wincondition extends Polygon {
    //TODO: Remove and use GameLogic enum (this is copied from gamelogic branch)
    public enum WinCondition {
        FISHER,
        LORDS,
        MINER,
        ANCHORITE,
        FARMER,
        MERCHANT,
        KNIGHT,
        EXPLORER,
        CITIZEN,
        WORKER
    }

    /**
     * Represents the radius of one hexagon.
     */
    private static final int RADIUS= 15;

    /**
     * Represents the six corners of a hexagon.
     */
    private static ArrayList<Point2D> vertices = HexagonCalculator.calculateCorners(RADIUS);;


    /**
     * Creates a new hexagon with an icon and tooltip which matches the wincondition.
     * @param winCondition The wincondtion.
     * @param resourceBundle The language support.
     */
    public Wincondition(WinCondition winCondition, ResourceBundle resourceBundle) {
        // add all corners of to the hexagon shape
        for (int i = 0; i < HexagonCalculator.NUMBER_OF_CORNERS; i++) {
            getPoints().add(vertices.get(i).getX());
            getPoints().add(vertices.get(i).getY());
        }

        // set Icon
        Image texture = TextureLoader.getWinConditionTexture(winCondition);
        setFill(new ImagePattern(texture, 0.0f, 0.0f, 1.0f, 1.0f, true));

        // declare Tooltip
        Tooltip winConditionTooltip = new Tooltip();

        // set Tooltip
        switch (winCondition) {
            // TODO: write win condition
            case ANCHORITE -> winConditionTooltip.setText(resourceBundle.getString("winCondAnchorite"));
            case CITIZEN -> winConditionTooltip.setText(resourceBundle.getString("winCondCitizen"));
            case EXPLORER -> winConditionTooltip.setText(resourceBundle.getString("winCondExplorer"));
            case FARMER -> winConditionTooltip.setText(resourceBundle.getString("winCondFarmer"));
            case FISHER -> winConditionTooltip.setText(resourceBundle.getString("winCondFisher"));
            case KNIGHT -> winConditionTooltip.setText(resourceBundle.getString("winCondKnight"));
            case LORDS -> winConditionTooltip.setText(resourceBundle.getString("winCondLords"));
            case MERCHANT -> winConditionTooltip.setText(resourceBundle.getString("winCondMerchant"));
            case MINER -> winConditionTooltip.setText(resourceBundle.getString("winCondMiner"));
            case WORKER -> winConditionTooltip.setText(resourceBundle.getString("winCondWorker"));
        }

        Tooltip.install(this, winConditionTooltip);
    }
}
