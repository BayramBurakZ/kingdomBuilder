package kingdomBuilder.gui.gameboard;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.util.Duration;
import kingdomBuilder.gamelogic.PlayerColor;
import kingdomBuilder.gamelogic.TileType;
import kingdomBuilder.gui.base.Tile;

import java.util.ResourceBundle;

/**
 * Class that is used to display the hexagon tiles in the UI.
 */
public class HexagonTile extends Tile {
    /**
     * Represents if the tile's color has been changed for highlighting.
     */
    private boolean isColorHighlighted = false;

    /**
     * Represents if the tile is translated in the z direction for highlighting.
     */
    private boolean isElevated = false;

    /**
     * Represents the distance that the group moves for highlighting.
     */
    private static final double HIGHLIGHT_DISTANCE = Hexagon.HEXAGON_DEPTH;

    /**
     * Represents the time for the animation to highlighting.
     */
    private static final Duration HIGHLIGHT_DURATION = Duration.millis(200);

    /**
     * Represents the animation for the highlight.
     */
    private final TranslateTransition highlightAnimation = new TranslateTransition(HIGHLIGHT_DURATION);

    /**
     * Represents the settlement on the hexagon.
     */
    private final Settlement settlement = new Settlement();

    /**
     * Represents the gameBoard with all 3D tiles.
     */
    private final GameBoard gameBoard;

    /**
     * Creates a new Hexagon Tile at the given position with given Type.
     * @param xPos the x-coordinate of the center position.
     * @param yPos the y-coordinate of the center position.
     * @param x the x-coordinate of the tile (0-19)
     * @param y the y-coordinate of the tile (0-19)
     * @param tileType the TileType of the Hexagon.
     * @param resource the ResourceBundle to translate text.
     * @param gameBoard the board which the tile is in.
     */
    public HexagonTile(double xPos, double yPos, int x, int y, TileType tileType, ResourceBundle resource,
                       GameBoard gameBoard) {
        super(x, y, xPos, yPos, tileType, resource);

        this.gameBoard = gameBoard;

        // add settlement to this group
        getChildren().add(settlement);

        // set up animation for elevating
        setupAnimation();

        if (TileType.tokenType.contains(tileType)) {
            setTokenTooltip(tileType);
        }
    }

    /**
     * Sets th animation used for this HexagonTile.
     */
    private void setupAnimation() {
        highlightAnimation.setFromZ(0);
        highlightAnimation.setToZ(-HIGHLIGHT_DISTANCE);
        highlightAnimation.setInterpolator(Interpolator.EASE_BOTH);
        highlightAnimation.setNode(this);
    }

    /**
     * Set the rule for every generated special place to their rule.
     * @param tileType type for recognizing the special place.
     */
    private void setTokenTooltip(TileType tileType) {
        Tooltip tokenTooltip = new Tooltip();
        switch (tileType) {
            case BARN -> tokenTooltip.setText(resourceBundle.getString("tokenBarnRule"));
            case FARM -> tokenTooltip.setText(resourceBundle.getString("tokenFarmRule"));
            case OASIS -> tokenTooltip.setText(resourceBundle.getString("tokenOasisRule"));
            case TOWER -> tokenTooltip.setText(resourceBundle.getString("tokenTowerRule"));
            case HARBOR -> tokenTooltip.setText(resourceBundle.getString("tokenHarborRule"));
            case ORACLE -> tokenTooltip.setText(resourceBundle.getString("tokenOracleRule"));
            case TAVERN -> tokenTooltip.setText(resourceBundle.getString("tokenTavernRule"));
            case PADDOCK -> tokenTooltip.setText(resourceBundle.getString("tokenPaddockRule"));
        }
        Tooltip.install(this, tokenTooltip);
    }

    /**
     * Sets the MouseHandler of a hexagon based on its type.
     */
    @Override
    protected void setMouseHandler() {
        setOnMousePressed(event -> {
            // place settlement only if the tile is elevated/highlighted
            if (event.isPrimaryButtonDown() && isElevated) {
                gameBoard.hexagonClicked(x, y);
            }
        });

        setOnMouseEntered(event -> setColorHighlighted());
        setOnMouseExited(event -> removeColorHighlighted());
    }

    /**
     * Sets the color of this hexagon to dark blue to show it is marked for moving.
     */
    public void setMarker() {
        hexagon.setMaterial(MaterialLoader.MIDNIGHTBLUE);
    }

    /**
     * Removes the blue highlight fpr the marker.
     */
    public void removeMarker() {
        resetMaterial();
    }

    /**
     * Places a house with the given color.
     *
     * @param color the color.
     * @param betterColors whether better colors should be used or not.
     */
    public void placeSettlement(PlayerColor color, boolean betterColors) {
        switch (color) {
            case RED -> {
                Image img;
                if (betterColors) {
                    img = TextureLoader.generateImage(1, 0.54, 0);
                } else {
                    img = TextureLoader.generateImage(1, 0, 0);
                }
                PhongMaterial mat = new PhongMaterial(Color.WHITE, img, null, null, null);

                settlement.setMaterial(mat);
            }

            case BLUE -> {
                Image img;
                if (betterColors) {
                    img = TextureLoader.generateImage(0.612, 0.52, 0.816);
                } else {
                    img = TextureLoader.generateImage(0, 0, 1);
                }
                PhongMaterial mat = new PhongMaterial(Color.WHITE, img, null, null, null);

                settlement.setMaterial(mat);
            }

            case BLACK -> {
                Image img;
                if (betterColors) {
                    img = TextureLoader.generateImage(0.616, 0.82, 0.2);
                } else {
                    img = TextureLoader.generateImage(0, 0, 0);
                }
                PhongMaterial mat = new PhongMaterial(Color.WHITE, img, null, null, null);

                settlement.setMaterial(mat);
            }

            case WHITE -> {
                Image img;
                if (betterColors) {
                    img = TextureLoader.generateImage(0.28, 0.52, 0.72);
                } else {
                    img = TextureLoader.generateImage(1, 1, 1);
                }
                PhongMaterial mat = new PhongMaterial(Color.WHITE, img, null, null, null);

                settlement.setMaterial(mat);
            }
        }
        settlement.setOpacity(1.0);
    }

    /**
     * Moves a settlement from one Tile to another one.
     * @param toX the x position where the settlement is moved to.
     * @param toY the y position where the settlement is moved to.
     * @param toXCoord the x-coordinate for the new placed settlement.
     * @param toYCoord the y-coordinate for the new placed settlememt.
     * @param color the player color for the new settlement.
     */
    public void moveAnimation(double toX, double toY, int toXCoord, int toYCoord, PlayerColor color) {

        // calculate the delta between the positions for the movement path
        double deltaX =  toX - xPos;
        double deltaY =  toY - yPos;

        // set up the move animation
        Duration moveDuration = new Duration(1000);
        TranslateTransition moveAnimation = new TranslateTransition(moveDuration);

        moveAnimation.setFromX(0);
        moveAnimation.setFromZ(settlement.getTranslateZ()-HIGHLIGHT_DISTANCE);
        moveAnimation.setFromY(0);

        moveAnimation.setToX(deltaX);
        moveAnimation.setToY(deltaY);

        moveAnimation.setInterpolator(Interpolator.EASE_BOTH);
        moveAnimation.setNode(settlement);

        moveAnimation.setRate(1);
        moveAnimation.play();

        // wait for the movement to be finished
        moveAnimation.setOnFinished(event -> {
            settlement.setOpacity(0.0);
            settlement.setTranslateX(0);
            settlement.setTranslateY(0);
            settlement.setTranslateZ(- Hexagon.HEXAGON_DEPTH - Settlement.SETTLEMENT_DEPTH);
            System.out.println("Finished Moving");

            gameBoard.placeSettlement(toXCoord, toYCoord, color);
        });
    }

    /**
     * Activates the color highlighting of a tile.
     */
    private void setColorHighlighted() {
        if (!isColorHighlighted && isElevated) {
            isColorHighlighted = true;
            hexagon.setMaterial(MaterialLoader.RED);
        }
    }

    /**
     * Removes the color highlighting of the tile.
     */
    private void removeColorHighlighted() {
        if (isColorHighlighted) {
            isColorHighlighted = false;
            resetMaterial();
        }
    }

    /**
     * Activates the elevation highlighting of the tile.
     */
    public void setElevated() {
        if (!isElevated) {
            isElevated = true;
            if (isHover()) {
                setColorHighlighted();
            }
            highlightAnimation.setRate(1);
            highlightAnimation.play();
        }
    }

    /**
     * Removes the elevation highlighting of the tile.
     */
    public void removeElevated() {
        if (isElevated) {
            isElevated = false;
            if (isColorHighlighted) {
                removeColorHighlighted();
            }
            highlightAnimation.setRate(-1);
            highlightAnimation.play();
        }
    }

    /**
     * Gets the boolean value if the tile is currently elevated for highlighting.
     * @return If the tile is elevated for highlighting.
     */
    public boolean isElevated() {
        return isElevated;
    }

    /**
     * Gets the type of the hexagon.
     * @return The Type of the hexagon.
     */
    public TileType getTileType() {
        return tileType;
    }

    /**
     * Sets the material to the tile type's material.
     */
    public void resetMaterial() {
        PhongMaterial mat = MaterialLoader.getMaterial(tileType);
        hexagon.setMaterial(mat);
    }
}
