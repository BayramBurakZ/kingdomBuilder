package kingdomBuilder.gamelogic;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Contains the data of a tile.
 */
public class Tile {

    /**
     * The x and y coordinate of the tile.
     */
    public final int x, y;

    /**
     * The tile type of the tile.
     */
    public final TileType tileType;

    /**
     * The player whose settlement is occupying the tile.
     */
    protected Player occupiedBy;

    /**
     * The amount of tokens that players can still receive from this tile.
     */
    protected int remainingTokens;

    /**
     * Creates a copy of a tile.
     *
     * @param tile The tile to copy.
     */
    public Tile(Tile tile) {

        this.x = tile.x;
        this.y = tile.y;
        this.tileType = tile.tileType;

        this.occupiedBy = tile.occupiedBy;
        this.remainingTokens = tile.remainingTokens;
    }

    /**
     * Constructs a new TileReadOnly object with the given coordinate, the TileType and the amount of tokens.
     *
     * @param x               the x-coordinate.
     * @param y               the y-coordinate.
     * @param tileType        the tileType.
     * @param remainingTokens the amount of tokens that remains on that tile.
     */
    public Tile(int x, int y, TileType tileType, int remainingTokens) {

        this.x = x;
        this.y = y;
        this.tileType = tileType;

        this.occupiedBy = null;
        this.remainingTokens = remainingTokens;
    }

    /**
     * Checks whether the tile is occupied by any player or obstructed due to terrain.
     *
     * @return True if it is occupied. False otherwise.
     */
    public boolean isBlocked() {
        return !TileType.placeableTileTypes.contains(tileType) || (occupiedBy != null);
    }

    /**
     * Gets the player that is occupying the tile.
     *
     * @return The player that is occupying the tile.
     */
    public Player occupiedBy() {
        return occupiedBy;
    }

    /**
     * Check whether the tile at given index is at the border of the map.
     *
     * @param gameMap the map containing the tile.
     * @return Whether the tile is at the border.
     */
    public boolean isAtBorder(GameMap gameMap) {
        return (x == 0 || y == 0 || x == gameMap.mapWidth - 1 || y == gameMap.mapWidth - 1);
    }

    /**
     * Checks if the tile has a token left.
     *
     * @return True if it has tokens left. False otherwise.
     */
    public boolean hasTokens() {
        if (!TileType.tokenType.contains(tileType))
            throw new RuntimeException("The tile is not a special place!");

        return (remainingTokens > 0);
    }

    /**
     * Checks if two tiles are neighbours in the hexagon map.
     *
     * @param other the second tile.
     * @return Whether both tiles are neighbours.
     */
    public boolean areNeighbouringTiles(Tile other) {
        // Compare Y
        if (Math.abs(y - other.y) > 1)
            return false;
        // Neighbour in same row
        if (y == other.y && Math.abs(x - other.x) > 1)
            return false;
        if (y % 2 == 0) {
            // even row
            // top/bottom left or top/bottom right
            return x == other.x - 1 || x == other.x;
        } else {
            // odd row
            // top/bottom right or top/bottom left
            return x == other.x + 1 || x == other.x;
        }
    }

    /**
     * Check if a tile is at the front or back part of a chain of settlements that are owned by the specified player.
     *
     * @param gameMap the map containing the tile.
     * @param player  the player to check for.
     * @return True if tile is a part of a chain. False otherwise.
     */
    public boolean isAtEndOfAChain(GameMap gameMap, Player player) {

        if (isBlocked())
            return false;

        // Check if chain is on right side
        if (gameMap.isWithinBounds(x + 3, y)
                && (gameMap.at(x + 1, y).occupiedBy() == player)
                && (gameMap.at(x + 2, y).occupiedBy() == player)
                && (gameMap.at(x + 3, y).occupiedBy() == player))
            return true;

        // Check if chain is on left side
        if (gameMap.isWithinBounds(x - 3, y)
                && (gameMap.at(x - 1, y).occupiedBy() == player)
                && (gameMap.at(x - 2, y).occupiedBy() == player)
                && (gameMap.at(x - 3, y).occupiedBy() == player))
            return true;

        // top right
        if (gameMap.isWithinBounds(GameMap.topRightX(x, y, 3), y - 3)) {
            boolean chainFound = true;
            for (int i = 1; i < 4; i++)
                if (gameMap.at(GameMap.topRightX(x, y, i), y - i).occupiedBy() != player) {
                    chainFound = false;
                    break;
                }
            if (chainFound) return true;
        }

        // top left
        if (gameMap.isWithinBounds(GameMap.topLeftX(x, y, 3), y - 3)) {
            boolean chainFound = true;
            for (int i = 1; i < 4; i++)
                if (gameMap.at(GameMap.topLeftX(x, y, i), y - i).occupiedBy() != player) {
                    chainFound = false;
                    break;
                }
            if (chainFound) return true;
        }

        // bottom right
        if (gameMap.isWithinBounds(GameMap.bottomRightX(x, y, 3), y + 3)) {
            boolean chainFound = true;
            for (int i = 1; i < 4; i++)
                if (gameMap.at(GameMap.bottomRightX(x, y, i), y + i).occupiedBy() != player) {
                    chainFound = false;
                    break;
                }
            if (chainFound) return true;
        }

        // bottom left
        if (gameMap.isWithinBounds(GameMap.bottomLeftX(x, y, 3), y + 3)) {
            boolean chainFound = true;
            for (int i = 1; i < 4; i++)
                if (gameMap.at(GameMap.bottomLeftX(x, y, i), y + i).occupiedBy() != player) {
                    chainFound = false;
                    break;
                }
            if (chainFound) return true;
        }

        return false;
    }

    /**
     * Returns an iterator that contains all surrounding tiles of a given hexagon.
     *
     * @param gameMap the map containing the tile.
     * @return All surrounding Tiles.
     */
    public Iterator<Tile> surroundingTilesIterator(GameMap gameMap) {

        return new Iterator<Tile>() {
            int state = -1;

            @Override
            public boolean hasNext() {
                switch (state) {
                    case -1:
                        // check given coordinates
                        if (!gameMap.isWithinBounds(x, y)) {
                            return false;
                        }
                    case 0:
                        // top left
                        if (GameMap.topLeftX(x, y) >= 0 && y > 0) {
                            state = 0;
                            return true;
                        }
                    case 1:
                        // top right
                        if (GameMap.topRightX(x, y) < gameMap.mapWidth && y > 0) {
                            state = 1;
                            return true;
                        }
                    case 2:
                        // left
                        if (x - 1 >= 0) {
                            state = 2;
                            return true;
                        }
                    case 3:
                        // right
                        if (x + 1 < gameMap.mapWidth) {
                            state = 3;
                            return true;
                        }
                    case 4:
                        // bottom left
                        if (GameMap.bottomLeftX(x, y) >= 0 && y + 1 < gameMap.mapWidth) {
                            state = 4;
                            return true;
                        }
                    case 5:
                        // bottom right
                        if (GameMap.bottomRightX(x, y) < gameMap.mapWidth && y + 1 < gameMap.mapWidth) {
                            state = 5;
                            return true;
                        }
                    default:
                        // all tiles checked
                        return false;
                }
            }

            @Override
            public Tile next() {
                switch (state) {
                    case 0:
                        state++;
                        return gameMap.at(GameMap.topLeftX(x, y), y - 1);
                    case 1:
                        state++;
                        return gameMap.at(GameMap.topRightX(x, y), y - 1);
                    case 2:
                        state++;
                        return gameMap.at(x - 1, y);
                    case 3:
                        state++;
                        return gameMap.at(x + 1, y);
                    case 4:
                        state++;
                        return gameMap.at(GameMap.bottomLeftX(x, y), y + 1);
                    case 5:
                        state++;
                        return gameMap.at(GameMap.bottomRightX(x, y), y + 1);
                    default:
                        return null;
                }
            }
        };
    }

    /**
     * Get all the surrounding tiles of the specified tile.
     *
     * @param gameMap the map containing the tile.
     * @return all surrounding tiles of the specified tile.
     */
    public Stream<Tile> surroundingTiles(GameMap gameMap) {
        if (!gameMap.isWithinBounds(x, y)) {
            return Stream.empty();
        }

        Iterable<Tile> iterable = () -> surroundingTilesIterator(gameMap);
        return StreamSupport.stream(iterable.spliterator(), false);

        /*
        Set<Tile> surroundingTiles = new HashSet<>();

        // top left
        if (GameMap.topLeftX(x, y) >= 0 && y > 0)
            surroundingTiles.add(gameMap.at(GameMap.topLeftX(x, y), y - 1));

        // top right
        if (GameMap.topRightX(x, y) < gameMap.mapWidth && y > 0)
            surroundingTiles.add(gameMap.at(GameMap.topRightX(x, y), y - 1));

        // left
        if (x - 1 >= 0)
            surroundingTiles.add(gameMap.at(x - 1, y));

        // right
        if (x + 1 < gameMap.mapWidth)
            surroundingTiles.add(gameMap.at(x + 1, y));

        // bottom left
        if (GameMap.bottomLeftX(x, y) >= 0 && y + 1 < gameMap.mapWidth)
            surroundingTiles.add(gameMap.at(GameMap.bottomLeftX(x, y), y + 1));

        // bottom right
        if (GameMap.bottomRightX(x, y) < gameMap.mapWidth && y + 1 < gameMap.mapWidth)
            surroundingTiles.add(gameMap.at(GameMap.bottomRightX(x, y), y + 1));

        return surroundingTiles;
        */
    }

    /**
     * Gets all free tiles that are skipped from a given position.
     *
     * @param gameMap the map containing the tile.
     * @return all free tiles that can be placed on that skipped position.
     */
    public Stream<Tile> surroundingTilesPaddock(GameMap gameMap) {
        Set<Tile> freeTiles = new HashSet<>();
        int tempX, tempY;

        // top left diagonal
        tempX = GameMap.topLeftX(x, y, 2);
        tempY = y - 2;

        if (gameMap.isWithinBounds(tempX, tempY) && !gameMap.at(tempX, tempY).isBlocked())
            freeTiles.add(gameMap.at(tempX, tempY));

        // top right diagonal
        tempX = GameMap.topRightX(x, y, 2);
        tempY = y - 2;

        if (gameMap.isWithinBounds(tempX, tempY) && !gameMap.at(tempX, tempY).isBlocked())
            freeTiles.add(gameMap.at(tempX, tempY));

        // left
        if (gameMap.isWithinBounds(x - 2, y) && !gameMap.at(x - 2, y).isBlocked())
            freeTiles.add(gameMap.at(x - 2, y));

        // right
        if (gameMap.isWithinBounds(x + 2, y) && !gameMap.at(x + 2, y).isBlocked())
            freeTiles.add(gameMap.at(x + 2, y));

        // bottom left diagonal
        tempX = GameMap.bottomLeftX(x, y, 2);
        tempY = y + 2;

        if (gameMap.isWithinBounds(tempX, tempY) && !gameMap.at(tempX, tempY).isBlocked())
            freeTiles.add(gameMap.at(tempX, tempY));

        // bottom right diagonal
        tempX = GameMap.bottomRightX(x, y, 2);
        tempY = y + 2;

        if (gameMap.isWithinBounds(tempX, tempY) && !gameMap.at(tempX, tempY).isBlocked())
            freeTiles.add(gameMap.at(tempX, tempY));

        return freeTiles.stream();
    }

    /**
     * Returns the surrounding settlements owned by the specified player.
     *
     * @param gameMap the map containing the tile.
     * @param player  the player whose settlements to look for.
     * @return all tiles with a settlement of the specified player.
     */
    public Stream<Tile> surroundingSettlements(GameMap gameMap, Player player) {
        return surroundingTiles(gameMap).filter(tile -> tile.occupiedBy == player);
    }

    /**
     * Returns the surrounding special places.
     *
     * @param gameMap the map containing the tile.
     * @return all surrounding tiles that are special places.
     */
    public Stream<Tile> surroundingSpecialPlaces(GameMap gameMap) {
        return surroundingTiles(gameMap).filter(tile -> TileType.tokenType.contains(tile.tileType));
    }

    /**
     * Check if settlement of a player has at least one neighbour.
     *
     * @param gameMap the map containing the tile.
     * @param player  the player to check.
     * @return True if player has a neighbouring settlement. False otherwise.
     */
    public boolean hasSurroundingSettlement(GameMap gameMap, Player player) {

        // right tile
        if (gameMap.isWithinBounds(x + 1, y) && (gameMap.at(x + 1, y).occupiedBy() == player))
            return true;

        // left tile
        if (gameMap.isWithinBounds(x - 1, y) && (gameMap.at(x - 1, y).occupiedBy() == player))
            return true;

        // top right
        if (gameMap.isWithinBounds(GameMap.topRightX(x, y), y + 1)
                && (gameMap.at(GameMap.topRightX(x, y), y + 1).occupiedBy() == player))
            return true;

        // top left
        if (gameMap.isWithinBounds(GameMap.topLeftX(x, y), y + 1)
                && (gameMap.at(GameMap.topLeftX(x, y), y + 1).occupiedBy() == player))
            return true;

        // bottom right
        if (gameMap.isWithinBounds(GameMap.bottomRightX(x, y), y - 1)
                && (gameMap.at(GameMap.bottomRightX(x, y), y - 1).occupiedBy() == player))
            return true;

        // bottom left
        return gameMap.isWithinBounds(GameMap.bottomLeftX(x, y), y - 1)
                && (gameMap.at(GameMap.bottomLeftX(x, y), y - 1).occupiedBy() == player);
    }

    ////////////////////////////////////////////////
    // Setter Methods //////////////////////////////

    /**
     * Place a settlement on the tile that belongs to the given player.
     *
     * @param ownerOfSettlement player that places a settlement.
     */
    public void placeSettlement(Player ownerOfSettlement) {
        if (occupiedBy != null)
            throw new TileIsAlreadyOccupiedException("Is already occupied by Player: " + occupiedBy.name);

        occupiedBy = ownerOfSettlement;
    }

    /**
     * Remove a settlement from the tile.
     *
     * @return The previous player that occupied the tile.
     */
    public Player removeSettlement() {
        if (occupiedBy == null)
            throw new RuntimeException("Tile is not occupied at: " + x + "/" + y);

        Player previousPlayer = occupiedBy;
        occupiedBy = null;

        return previousPlayer;
    }

    /**
     * Moves a settlement from this tile to another.
     *
     * @param destination the tile that the settlement gets moved to.
     */
    public void moveSettlement(Tile destination) {
        Player player = removeSettlement();
        destination.placeSettlement(player);
    }

    /**
     * Take a token from a special place.
     *
     * @return The token type of the special place.
     * @throws HasNoTokenException when tile is not a special place.
     */
    public TileType takeTokenFromSpecialPlace() throws HasNoTokenException {
        if (!TileType.tokenType.contains(tileType))
            throw new HasNoTokenException("Can't take a token from a non special place!");
        if (remainingTokens <= 0)
            throw new HasNoTokenException("No more tokens remaining!");

        remainingTokens--;
        return tileType;
    }
}
