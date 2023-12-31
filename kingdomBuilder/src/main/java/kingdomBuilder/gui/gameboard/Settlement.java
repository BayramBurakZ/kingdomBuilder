package kingdomBuilder.gui.gameboard;

import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import kingdomBuilder.gui.util.PLYLoader;

import java.io.InputStream;
import java.net.URISyntaxException;

/**
 * Class that creates a 3D settlement.
 */
public class Settlement extends MeshView {
    /**
     * Represents the TriangleMesh for the Settlement.
     */
    private static final TriangleMesh settlementMesh;

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
    public static final double SETTLEMENT_DEPTH = 15;

    static {
        InputStream stream = HexagonTile
                .class
                .getResourceAsStream("/kingdomBuilder/gui/meshes/settlement.ply");

        settlementMesh = PLYLoader.readfromPLYStream(stream);
    }

    /**
     * Creates a new 3D settlement.
     */
    public Settlement() {
        super(settlementMesh);
        setScaleX(SETTLEMENT_WIDTH);
        setScaleY(SETTLEMENT_HEIGHT);
        setScaleZ(SETTLEMENT_DEPTH);

        setTranslateZ(- Hexagon.HEXAGON_DEPTH - Settlement.SETTLEMENT_DEPTH);

        setOpacity(0.0);
    }

}
