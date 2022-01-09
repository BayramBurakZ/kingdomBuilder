package kingdomBuilder.gui.gameboard;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.util.Duration;
import kingdomBuilder.model.TileType;

import java.util.ResourceBundle;

/**
 * Class that is used to display the hexagon tiles in the UI.
 */
public class HexagonTile extends Group {
    /**
     * Represents the resourceBundle that used for language support.
     */
    private static ResourceBundle resourceBundle;

    /**
     * Represents, if the tile is highlighted.
     */
    private boolean highlight = false;

    /**
     * Represents the tileType of the hexagon.
     */
    private TileType tileType;

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
     * Represents the hexagon prism.
     */
    private Hexagon hexagon;

    /**
     * Creates a new Hexagon Tile at the given position with given Type.
     * @param xPos The x-coordinate of the upper-left corner position.
     * @param yPos The y-coordinate of the upper-left corner position.
     * @param tileType The TileType of the Hexagon.
     * @param resource The ResourceBundle to translate text.
     */
    public HexagonTile(double xPos, double yPos, TileType tileType, ResourceBundle resource) {
        if (resourceBundle == null || !resourceBundle.equals(resource) || resourceBundle != resource) {
            resourceBundle = resource;
        }
        // create hexagon
        this.hexagon = new Hexagon(tileType);

        // add hexagon prism and settlement to this group
        getChildren().addAll(hexagon, settlement);

        // move this group to new position
        setTranslateX(xPos);
        setTranslateY(yPos);

        // set tileType
        // TODO use Datalogic enum
        this.tileType = tileType;

        setupAnimation();

        // TODO: execute only for Tokens
        setTokenTooltip(tileType);
        setMouseHandler();
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
        // TODO: Adjust to gameLogic enums
        Tooltip tokenTooltip = new Tooltip();
        switch (tileType) {
            case BARN -> {
                tokenTooltip.setText(resourceBundle.getString("tokenBarnRule"));
                Tooltip.install(this, tokenTooltip);
            }
            case FARM -> {
                tokenTooltip.setText(resourceBundle.getString("tokenFarmRule"));
                Tooltip.install(this, tokenTooltip);
            }
            case OASIS -> {
                tokenTooltip.setText(resourceBundle.getString("tokenOasisRule"));
                Tooltip.install(this, tokenTooltip);
            }
            case TOWER -> {
                tokenTooltip.setText(resourceBundle.getString("tokenTowerRule"));
                Tooltip.install(this, tokenTooltip);
            }
            case HARBOR -> {
                tokenTooltip.setText(resourceBundle.getString("tokenHarborRule"));
                Tooltip.install(this, tokenTooltip);
            }
            case ORACLE -> {
                tokenTooltip.setText(resourceBundle.getString("tokenOracleRule"));
                Tooltip.install(this, tokenTooltip);
            }
            case TAVERN -> {
                tokenTooltip.setText(resourceBundle.getString("tokenTavernRule"));
                Tooltip.install(this, tokenTooltip);
            }
            case PADDOCK -> {
                tokenTooltip.setText(resourceBundle.getString("tokenPaddockRule"));
                Tooltip.install(this, tokenTooltip);
            }
        }
    }

    /**
     * Creates an image with only one color.
     * @param red The red value.
     * @param green The green value.
     * @param blue The blue value.
     * @return Image with the given color.
     */
    public Image generateImage(double red, double green, double blue) {
        WritableImage img = new WritableImage(1, 1);
        PixelWriter pixelWriter = img.getPixelWriter();

        Color color = Color.color(red, green, blue);
        pixelWriter.setColor(0, 0, color);
        return img ;
    }

    /**
     * Sets the MouseHandler of a hexagon based on its type.
     */
    private void setMouseHandler() {
        setOnMouseClicked(event -> {
            // TODO integrate player color
            settlement.setOpacity(1.0);
            Image img = generateImage(Math.random(),Math.random(),Math.random());
            PhongMaterial mat = new PhongMaterial(Color.WHITE, img, null, null, null);

            settlement.setMaterial(mat);
        });

        setOnMouseEntered( event -> {
            if (highlight) {
                // TODO do it properly
                ((PhongMaterial) hexagon.getMaterial()).setDiffuseColor(Color.RED);

            } else {

            }
        });

        setOnMouseMoved(event -> {
            if (highlight) {

            } else {

            }
        });

        setOnMouseExited(event -> {
            if (highlight) {
                // TODO do it properly
                ((PhongMaterial) hexagon.getMaterial()).setDiffuseColor(Color.WHITE);
            } else {

            }
        });

    }

    /**
     * Activates the highlight of a Tile.
     */
    public void setHighlight() {
        if (!highlight) {
            highlightAnimation.setRate(1);
            highlightAnimation.play();
            highlight = true;
        }
    }

    /**
     * Removes the highlight from the tile.
     */
    public void removeHighlight() {
        if (highlight) {
            highlightAnimation.setRate(-1);
            highlightAnimation.play();
            highlight = false;
        }
    }

    /**
     * Gets the boolean value, if the tile is currently highlighted.
     * @return If the tile is highlighted.
     */
    public boolean isHighlighted() {
        return highlight;
    }

    /**
     * Gets the type of the hexagon.
     * @return The Type of the hexagon.
     */
    public TileType getTileType() {
        return tileType;
    }
}
