package kingdomBuilder.gui.gameboard;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.util.Duration;
import kingdomBuilder.gamelogic.Game;
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
    private Settlement settlement = new Settlement();

    /**
     * Creates a new Hexagon Tile at the given position with given Type.
     * @param xPos the x-coordinate of the upper-left corner position.
     * @param yPos the y-coordinate of the upper-left corner position.
     * @param tileType the TileType of the Hexagon.
     * @param resource the ResourceBundle to translate text.
     */
    public HexagonTile(double xPos, double yPos, Game.TileType tileType, ResourceBundle resource) {
        super(xPos, yPos, tileType, resource);

        // add settlement to this group
        getChildren().add(settlement);

        // set up animation for elevating
        setupAnimation();

        if (Game.tokenType.contains(tileType)) {
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
    private void setTokenTooltip(Game.TileType tileType) {
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
            // TODO integrate player color
            settlement.setOpacity(1.0);
            Image img = TextureLoader.generateImage(Math.random(),Math.random(),Math.random());
            PhongMaterial mat = new PhongMaterial(Color.WHITE, img, null, null, null);

            settlement.setMaterial(mat);
        });

        setOnMouseEntered(event -> setColorHighlighted());

        setOnMouseExited(event -> removeColorHighlighted());
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
    public Game.TileType getTileType() {
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
