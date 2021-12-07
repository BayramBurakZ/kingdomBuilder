package kingdomBuilder.model;

import kingdomBuilder.gui.gameboard.HexagonTile;
import kingdomBuilder.gui.gameboard.TextureLoader;

public class Model {
    //TODO this class should be part of Model and only here to test setting the textures
    private TextureLoader textureLoader = new TextureLoader();
    private HexagonTile[][] gameboard_model = new HexagonTile[20][20];

    public Model() {
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 20; x++) {
                /*
                generates random textures instead of tile type based on given layout
                because of using the HexagonTile class that is concepted for the gui
                */
                int random = (int) ((Math.random() * (7 - 1)) + 1);
                gameboard_model[x][y] = new HexagonTile(x, y);
                gameboard_model[x][y].setTexture(textureLoader.getTexture(TileType.valueOf(random)));
            }
        }
    }

    public HexagonTile[][] getGameboard_model() {
        return gameboard_model;
    }
}
