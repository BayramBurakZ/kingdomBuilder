package kingdomBuilder.gui.gameboard;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import kingdomBuilder.gui.controller.GameViewController;
import kingdomBuilder.gui.util.HexagonCalculator;
import kingdomBuilder.model.TileType;

import java.util.ArrayList;

/**
 * Class that is used to display Tokens.
 */
public class Token extends StackPane {

    /**
     * The Label for the number of Tokens.
     */
    private Label countDisplay = new Label();

    /**
     * The hexagon shape with the texture.
     */
    private Hexagon2D hexagon;

    /**
     * Constructs a new Token, with given type, count.
     * @param tileType The type of the Token.
     * @param count The count, how many Tokens the user own.
     * @param gameViewController The gameViewController (to deactivate other Tokens).
     * @param disable If the Token is at the beginning disabled.
     */
    public Token(TileType tileType, int count, GameViewController gameViewController, boolean disable) {
        super();

        // TODO: Subscribers:
        //  - Token used -> tokenUsed
        hexagon = new Hexagon2D(tileType, gameViewController);


        ObservableList<Node> contents = this.getChildren();
        contents.add(countDisplay);
        contents.add(hexagon);
        setAlignment(countDisplay, Pos.BOTTOM_RIGHT);

        if (count == 2) {
            countDisplay.setText("2");

            // TODO use a CSS Style Sheet
            // font green and bigger
            countDisplay.setStyle(
                    "-fx-font: 24 arial; -fx-text-fill: green;");
        }

        if (disable) {
            this.disableToken();
        }
    }

    /**
     * Disables usage of this token.
     */
    public void disableToken() {
        setDisabled(true);
        hexagon.setHexagonDisabled(true);
        countDisplay.setDisable(true);
        // TODO make it little bit of gray or another highlight
    }

    /**
     * Enables usage of this token.
     */
    public void enableToken() {
        setDisabled(false);
        hexagon.setHexagonDisabled(false);
        countDisplay.setDisable(false);
        // TODO remove the little bit of gray or another highlight
    }

    /**
     * Checks if the token is currently activated and ready to use.
     * @return If the Token is ready to use.
     */
    public boolean isTokenActivated() {
        return hexagon.isActivated;
    }

    /**
     * Class to create a 2D hexagon with the Polygon class.
     */
    private class Hexagon2D extends Polygon {
        /**
         * Represents the radius of one hexagon.
         */
        private static final int RADIUS= 40;

        /**
         * Represents the six corners of a hexagon.
         */
        private static ArrayList<Point2D> vertices = HexagonCalculator.calculateCorners(RADIUS);;

        /**
         * Indicates if the token is ready to use (true).
         */
        private boolean isActivated = false;

        /**
         * Indicates if the hexagon is disabled. If so no action will be triggered.
         */
        private boolean isDisabled = false;

        /**
         * Represents the GameViewController.
         */
        private GameViewController gameViewController;


        /**
         * Constructs a new 2D hexagon with the given type.
         * @param tileType The type for the texture.
         * @param gameViewController The gameViewController.
         */
        public Hexagon2D(TileType tileType, GameViewController gameViewController) {
            this.gameViewController = gameViewController;

            setStrokeWidth(2.0);
            setStrokeType(StrokeType.INSIDE);

            // add all corners of to the hexagon shape
            for (int i = 0; i < HexagonCalculator.NUMBER_OF_CORNERS; i++) {
                getPoints().add(vertices.get(i).getX());
                getPoints().add(vertices.get(i).getY());
            }

            addListener(tileType);
            Image texture = TextureLoader.getTileTexture(tileType);
            setFill(new ImagePattern(texture, 0.0f, 0.0f, 1.0f, 1.0f, true));
        }

        /**
         * Adds the Mouse listener for the hexagon.
         * @param tileType The type defines which action is triggered.
         */
        private void addListener(TileType tileType) {
            setOnMouseClicked(event -> {
                // if the tile is disabled: do nothing
                if (isDisabled) {
                    return;
                }

                // with click activate the token
                activateToken();

                // if this token is activated then run the gamelogic method ...
                if (isActivated) {
                    // ... and disable all other tokens first
                    gameViewController.disableTokens(true);

                    // TODO: Gamelogic Enums
                    switch (tileType) {
                        case BARN -> {
                            //TODO: activate Token in Gamelogic for highlight
                            placeholder(tileType);
                        }
                        case FARM -> {
                            //TODO: activate Token in Gamelogic for highlight
                            placeholder(tileType);
                        }
                        case HARBOR -> {
                            //TODO: activate Token in Gamelogic for highlight
                            placeholder(tileType);
                        }
                        case OASIS -> {
                            //TODO: activate Token in Gamelogic for highlight
                            placeholder(tileType);
                        }
                        case ORACLE -> {
                            //TODO: activate Token in Gamelogic for highlight
                            placeholder(tileType);
                        }
                        case PADDOCK -> {
                            //TODO: activate Token in Gamelogic for highlight
                            placeholder(tileType);
                        }
                        case TAVERN -> {
                            //TODO: activate Token in Gamelogic for highlight
                            placeholder(tileType);
                        }
                        case TOWER -> {
                            //TODO: activate Token in Gamelogic for highlight
                            placeholder(tileType);
                        }
                    }
                } else {
                    gameViewController.disableTokens(false);
                }
            });
        }

        // TODO: remove
        private void placeholder(TileType tileType) {
            if (isActivated) {
                System.out.println("You activated your " + tileType + " -Token!");
            }
        }

        /**
         * Sets the hexagon disable value.
         * @param value Set to true if no Action should be triggered.
         */
        private void setHexagonDisabled(boolean value) {
            isDisabled = value;
        }

        /**
         * Activates the Token and highlights it.
         */
        private void activateToken() {
            isActivated = !isActivated;

            if (isActivated) {
                setStroke(Color.RED);
            } else {
                setStroke(null);
            }
        }
    }
}