package kingdomBuilder.gui.gameboard;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.util.Duration;
import kingdomBuilder.gui.util.PLYLoader;
import kingdomBuilder.model.TileType;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Class that is used to display the hexagon tiles in the UI.
 */
public class HexagonTile extends MeshView {
    /**
     * Represents the angle in degree to turn the corners around the center;
     */
    private static final int TURN_ANGLE_DEGREE = 60;

    /**
     * Represents the angle in radian to turn the corners around the center;
     */
    private static final double TURN_ANGLE_RADIAN = Math.toRadians(TURN_ANGLE_DEGREE);

    /**
     * Represents the number of corners of a hexagon.
     */
    private static final int NUMBER_OF_CORNERS = 6;

    /**
     * Represents the texture loader which all hexagons share.
     */
    private static final TextureLoader textureLoader = new TextureLoader();

    /**
     * Represents the resourceBundle that used for language support.
     */
    private static ResourceBundle resourceBundle;

    /**
     * Represents the six corners of a hexagon.
     */
    private static ArrayList<Point> vertices;

    /**
     * Represents, if the tile is highlighted.
     */
    private boolean highlight = false;

    /**
     * Represents the tileType of the hexagon.
     */
    private TileType tileType;

    /**
     * Represents the TriangleMesh for the hexanoal prism.
     */
    private static final TriangleMesh hexagonMesh;

    // TODO: check if javadoc is necessary
    static {
        TriangleMesh loadedMesh = null;
        try {
            loadedMesh = PLYLoader.readFromPlyFile(
                    HexagonTile.class.getResource("/kingdomBuilder/gui/meshes/hexagon.ply").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        hexagonMesh = loadedMesh;
    }

    /**
     * Represents the TriangleMesh for the Settlement.
     */
    private static final TriangleMesh settlementMesh;

    // TODO: check if javadoc is necessary
    static {
        TriangleMesh loadedMesh = null;
        try {
            loadedMesh = PLYLoader.readFromPlyFile(
                    HexagonTile.class.getResource("/kingdomBuilder/gui/meshes/settlement.ply").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        settlementMesh = loadedMesh;
    }

    //private static final PhongMaterial hexagonMaterial = new PhongMaterial();

    /**
     * Represents the MeshView for the settlement.
     */
    private final MeshView settlement = new MeshView(settlementMesh);

    /**
     * Represents the Group that contains the hexagonal prism and the settlement.
     */
    public Group root = new Group(this, settlement);

    /**
     * Represents the height for the hexagonal prism.
     */
    private static final double HEXAGON_HEIGHT = 40;

    /**
     * Represents the width for the hexagonal prism.
     */
    private static final double HEXAGON_WIDTH = 40;

    /**
     * Represents the depth for the hexagonal prism.
     */
    private static final double HEXAGON_DEPTH = 20;

    /**
     * Represents the height for a settlement.
     */
    private static final double SETTLEMENT_HEIGHT = 15;

    /**
     * Represents the width for a settlement.
     */
    private static final double SETTLEMENT_WIDTH = 15;

    /**
     * Represents the depth for a settlement.
     */
    private static final double SETTLEMENT_DEPTH = 15;

    /**
     * Represents the distance that the group moves for highlighting.
     */
    private static final double HIGHLIGHT_DISTANCE = HEXAGON_DEPTH;

    /**
     * Represents the time for the animation to highlighting.
     */
    private static final Duration HIGHLIGHT_DURATION = Duration.millis(200);

    /**
     * Represents the animation for the highlight.
     */
    private final TranslateTransition highlightAnimation = new TranslateTransition(HIGHLIGHT_DURATION);

    /**
     * Creates a new Hexagon Tile at the given position with given Type.
     * @param xPos The x-coordinate of the upper-left corner position.
     * @param yPos The y-coordinate of the upper-left corner position.
     * @param tileType The TileType of the Hexagon.
     * @param resource The ResourceBundle to translate text.
     */
    public HexagonTile(double xPos, double yPos, TileType tileType, ResourceBundle resource) {
        super(hexagonMesh);
        setScaleX(HEXAGON_WIDTH);
        setScaleY(HEXAGON_HEIGHT);
        setScaleZ(HEXAGON_DEPTH);

        settlement.setScaleX(SETTLEMENT_WIDTH);
        settlement.setScaleY(SETTLEMENT_HEIGHT);
        settlement.setScaleZ(SETTLEMENT_DEPTH);
        settlement.setTranslateZ(-HEXAGON_DEPTH-SETTLEMENT_DEPTH);

        root.setTranslateX(xPos);
        root.setTranslateY(yPos);

        if (resourceBundle == null || !resourceBundle.equals(resource) || resourceBundle != resource) {
            resourceBundle = resource;
        }

        this.tileType = tileType;

        highlightAnimation.setFromZ(0);
        highlightAnimation.setToZ(-HIGHLIGHT_DISTANCE);
        highlightAnimation.setInterpolator(Interpolator.EASE_BOTH);
        highlightAnimation.setNode(root);

        // TODO: execute only for Tokens
        setTokenTooltip(tileType);
        setMouseHandler(tileType);
    }

    /**
     * Calculates the corners of a hexagon with the given radius.
     * @param radius The radius from the center of a hexagon to one of its corners.
     * @return A list with six points of a hexagon.
     */
    private static ArrayList<Point> calculateCorners(int radius) {
        ArrayList<Point> corners = new ArrayList<>();

        int x = 0, my = 0;
        int y = my + radius;

        corners.add(new Point(x, y));

        // Rotation matrix to rotate the corners 60 degree around the center
        for (int i = 0; i < NUMBER_OF_CORNERS - 1; i++) {
            int movedX = (int) Math.round( (x * Math.cos(TURN_ANGLE_RADIAN)) - (y * Math.sin(TURN_ANGLE_RADIAN)) );
            int movedY = (int) Math.round( (x * Math.sin(TURN_ANGLE_RADIAN)) + (y * Math.cos(TURN_ANGLE_RADIAN)) );

            corners.add(new Point(movedX, movedY));
            x = movedX;
            y = movedY;
        }

        // search the distance from the center to the far left side
        int minX = Integer.MAX_VALUE;
        for (Point e : corners) {
            if (e.getX() < minX) {
                minX = e.getX();
            }
        }

        //translate the hexagon corners to positive values
        for (int i = 0; i < corners.size(); i++) {
            corners.get(i).translateX(-minX);
            corners.get(i).translateY(radius);
        }
        return corners;
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
     * Sets the MouseHandler of a hexagon based on its type.
     * @param tileType The type of the hexagon.
     */
    private void setMouseHandler(TileType tileType) {
        setOnMouseEntered( event -> {
            if (highlight) {

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
