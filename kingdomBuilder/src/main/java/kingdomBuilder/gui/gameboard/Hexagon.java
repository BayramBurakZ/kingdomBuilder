package kingdomBuilder.gui.gameboard;

import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import kingdomBuilder.gui.util.PLYLoader;
import kingdomBuilder.model.TileType;

import java.net.URISyntaxException;

/**
 * Class that creates a hexagonal prism.
 */
public class Hexagon extends MeshView {
    /**
     * Represents the TriangleMesh for the hexanoal prism.
     */
    private static final TriangleMesh hexagonMesh;

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
    public static final double HEXAGON_DEPTH = 20;

    /**
     * Creates a hexagon prism with texture.
     * @param tileType Selects a texture based on this type.
     */
    //TODO Tiletype from gamelogic
    public Hexagon(TileType tileType) {
        super(hexagonMesh);
        setScaleX(HEXAGON_WIDTH);
        setScaleY(HEXAGON_HEIGHT);
        setScaleZ(HEXAGON_DEPTH);

        PhongMaterial mat;
        if (tileType != null) {
            // set Texture with given type
            mat = MaterialLoader.getMaterial(tileType);
        } else {
            // set white Texture
            mat = MaterialLoader.WHITE;
        }
        setMaterial(mat);
    }
}
