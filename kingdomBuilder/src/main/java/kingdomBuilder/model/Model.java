package kingdomBuilder.model;

//TODO remove all

public class Model {
    private TileType[][] gameBoardData = new TileType[20][20];

    public Model() {
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 20; x++) {
                /*
                generates random textures instead of tile type based on given layout
                because of using the HexagonTile class that is concepted for the gui
                */
                int random = (int) ((Math.random() * 7) + 1);
                gameBoardData[x][y] = TileType.valueOf(random);
            }
        }

        //generates at random places on the map random special places
        for (int i = 0; i < 12; i++) {
            int x = (int) ((Math.random() * 20));
            int y = (int) ((Math.random() * 20));

            int random = (int) ((Math.random() * 9) + 7);
            gameBoardData[x][y] = TileType.valueOf(random);
        }
    }

    public TileType[][] getGameBoardData() {
        return gameBoardData;
    }
}

