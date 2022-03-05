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
     * @param xPos the x-coordinate of the upper-left corner position.
     * @param yPos the y-coordinate of the upper-left corner position.
     * @param tileType the TileType of the Hexagon.
     * @param resource the ResourceBundle to translate text.
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
        setOnMouseClicked(event -> {
            // place settlement only if the tile is elevated/highlighted
            if (isElevated) {
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
     * @param color the color.
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
     * Removes the settlement from this Tile.
     */
    public void removeSettlement() {
        settlement.setOpacity(0.0);
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
