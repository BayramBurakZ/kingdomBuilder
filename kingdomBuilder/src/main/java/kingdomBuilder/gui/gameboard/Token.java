package kingdomBuilder.gui.gameboard;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import kingdomBuilder.KBState;
import kingdomBuilder.actions.game.ActivateToken;
import kingdomBuilder.gamelogic.Game;
import kingdomBuilder.gamelogic.Game.TileType;
import kingdomBuilder.gui.controller.GameViewController;
import kingdomBuilder.gui.util.HexagonCalculator;
import kingdomBuilder.redux.Store;

import java.util.ArrayList;
import java.util.ResourceBundle;

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
     * Represents the resourceBundle that used for language support.
     */
    protected static ResourceBundle resource;

    /**
     * Constructs a new Token, with given type, count.
     * @param tileType the type of the Token.
     * @param count the count, how many Tokens the user own.
     * @param gameViewController the gameViewController (to deactivate other Tokens).
     * @param disable if the Token is at the beginning disabled.
     * @param resource the language support.
     */
    public Token(TileType tileType, int count, GameViewController gameViewController, boolean disable,
                 ResourceBundle resource, Store<KBState> store) {
        super();

        // TODO: Subscribers:
        //  - Token used -> tokenUsed
        hexagon = new Hexagon2D(tileType, gameViewController, store);

        // set resource
        this.resource = resource;

        ObservableList<Node> contents = this.getChildren();
        contents.add(hexagon);
        contents.add(countDisplay);
        setAlignment(countDisplay, Pos.BOTTOM_RIGHT);

        countDisplay.setText(String.valueOf(count));

        // TODO use a CSS Style Sheet
        // font green and bigger
        countDisplay.setStyle(
                "-fx-font: 24 arial; -fx-text-fill: green;");

        if (disable) {
            this.disableToken();
        }

        setTokenTooltip(tileType);
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
     * @return if the Token is ready to use.
     */
    public boolean isTokenActivated() {
        return hexagon.isActivated;
    }

    /**
     * Set the rule for every generated special place to their rule.
     * @param tileType the type for recognizing the special place.
     */
    private void setTokenTooltip(TileType tileType) {

        // if not a token, return
        if (Game.regularTileTypes.contains(tileType)) {
            return;
        }

        Tooltip tokenTooltip = new Tooltip();
        switch (tileType) {
            case BARN -> tokenTooltip.setText(resource.getString("tokenBarnRule"));
            case FARM -> tokenTooltip.setText(resource.getString("tokenFarmRule"));
            case OASIS -> tokenTooltip.setText(resource.getString("tokenOasisRule"));
            case TOWER -> tokenTooltip.setText(resource.getString("tokenTowerRule"));
            case HARBOR -> tokenTooltip.setText(resource.getString("tokenHarborRule"));
            case ORACLE -> tokenTooltip.setText(resource.getString("tokenOracleRule"));
            case TAVERN -> tokenTooltip.setText(resource.getString("tokenTavernRule"));
            case PADDOCK -> tokenTooltip.setText(resource.getString("tokenPaddockRule"));
        }
        Tooltip.install(this, tokenTooltip);
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
         * Represents the store.
         */
        private Store<KBState> store;

        /**
         * Constructs a new 2D hexagon with the given type.
         * @param tileType the type for the texture.
         * @param gameViewController the gameViewController.
         */
        public Hexagon2D(TileType tileType, GameViewController gameViewController, Store<KBState> store) {
            this.store = store;
            this.gameViewController = gameViewController;

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
         * @param tileType the type defines which action is triggered.
         */
        private void addListener(TileType tileType) {
            setOnMouseClicked(event -> {
                // if the tile is disabled: do nothing
                if (isDisabled) {
                    return;
                }
                // with click activate the token
                activateToken();

                if (isActivated) {
                    store.dispatch(new ActivateToken(tileType));

                } else {
                    store.dispatch(new ActivateToken(null));
                }
            });
        }

        /**
         * Sets the hexagon disable value.
         * @param value set to true if no Action should be triggered.
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
                setStrokeWidth(2.0);
                setStrokeType(StrokeType.INSIDE);
            } else {
                setStroke(null);
            }
        }
    }
}