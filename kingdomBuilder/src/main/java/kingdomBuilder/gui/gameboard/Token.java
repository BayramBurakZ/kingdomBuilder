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
import kingdomBuilder.model.TileType;

import java.util.ArrayList;

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
         * Represents the six corners of a hexagon.
         */
        private static ArrayList<Point2D> vertices;

        /**
         * Represents the angle in degree to turn the corners around the center;
         */
        private static final int TURN_ANGLE_DEGREE = 60;

        /**
         * Represents the angle in radian to turn the corners around the center;
         */
        private static final double TURN_ANGLE_RADIAN = Math.toRadians(TURN_ANGLE_DEGREE);

        /**
         * Represents the radius of one hexagon.
         */
        private static final int RADIUS= 40;

        /**
         * Represents the number of corners of a hexagon.
         */
        private static final int NUMBER_OF_CORNERS = 6;

        /**
         * Represents the texture loader which all hexagons share.
         */
        private static final TextureLoader textureLoader = new TextureLoader();

        /**
         * Indicates if the token is ready to use (true).
         */
        private boolean isActivated = false;

        /**
         * Represents the GameViewController.
         */
        private GameViewController gameViewController;

        /**
         * Calculates the corners of a hexagon with the given radius.
         *
         * @param radius The radius from the center of a hexagon to one of its corners.
         * @return A list with six points of a hexagon.
         */
        private static ArrayList<Point2D> calculateCorners(int radius) {
            ArrayList<Point2D> corners = new ArrayList<>();

            int x = 0, my = 0;
            int y = my + radius;

            corners.add(new Point2D(x, y));

            // Rotation matrix to rotate the corners 60 degree around the center
            for (int i = 0; i < NUMBER_OF_CORNERS - 1; i++) {
                int movedX = (int) Math.round((x * Math.cos(TURN_ANGLE_RADIAN)) - (y * Math.sin(TURN_ANGLE_RADIAN)));
                int movedY = (int) Math.round((x * Math.sin(TURN_ANGLE_RADIAN)) + (y * Math.cos(TURN_ANGLE_RADIAN)));

                corners.add(new Point2D(movedX, movedY));
                x = movedX;
                y = movedY;
            }

            // search the distance from the center to the far left side
            int minX = Integer.MAX_VALUE;
            for (Point2D e : corners) {
                if (e.getX() < minX) {
                    minX = (int) e.getX();
                }
            }


            //translate the hexagon corners to positive values
            for (int i = 0; i < corners.size(); i++) {
                corners.set(i, corners.get(i).add(-minX, radius));
            }
            return corners;
        }

        //TODO javadoc
        public Hexagon2D(TileType tileType, GameViewController gameViewController) {
            this.gameViewController = gameViewController;
            vertices = calculateCorners(RADIUS);

            setStrokeWidth(2.0);
            setStrokeType(StrokeType.INSIDE);

            // calculate all corners of a hexagon
            for (int i = 0; i < NUMBER_OF_CORNERS; i++) {
                getPoints().add(vertices.get(i).getX());
                getPoints().add(vertices.get(i).getY());
            }

            addListener(tileType);
            Image texture = textureLoader.getTexture(tileType);
            setFill(new ImagePattern(texture, 0.0f, 0.0f, 1.0f, 1.0f, true));
        }

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


        private void placeholder(TileType tileType) {
            if (isActivated) {
                System.out.println("You activated your " + tileType + " -Token!");
            }
        }

        private boolean isDisabled = false;

        private void setHexagonDisabled(boolean value) {
            isDisabled = value;
        }

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