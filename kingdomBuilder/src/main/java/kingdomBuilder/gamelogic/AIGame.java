package kingdomBuilder.gamelogic;

import java.util.Set;

// Temporary storage for methods that will be relevant for AI players later on
public class AIGame extends Game {

    /**
     * Throws if it's not the given player's turn.
     *
     * @param player the player to check if it's their turn.
     */
    protected void checkIsPlayersTurn(Player player) {
        if (!isPlayersTurn(player))
            throw new RuntimeException("It's not the player's turn!");
    }

    /**
     * Checks if the given player can still place settlements this turn.
     *
     * @param player the player to check if they can still place settlements this turn.
     */
    protected void checkHasRemainingSettlements(Player player) {
        if (!player.hasRemainingSettlements())
            throw new RuntimeException("Player has no settlements left!");
    }

    /**
     * Places a settlement as a basic turn and throws if the move is not valid.
     *
     * @param x      the horizontal position of the settlement on the map.
     * @param y      the vertical position of the settlement on the map.
     * @param player the player whose turn it is and who owns the settlement.
     */
    @Override
    public void unsafePlaceSettlement(Player player, int x, int y) {
        if (!canPlaceSettlement(player, x, y))
            throw new RuntimeException("can't place settlement");

        map.at(x, y).placeSettlement(player);
        player.remainingSettlements--;

        Set<Tile> specialPlaces = map.at(x, y).surroundingSpecialPlaces(map);

        // Player gets a token if settlement is next to special place
        for (var specialPlace : specialPlaces) {
            player.addToken(specialPlace);
        }
    }

    /**
     * Moves a settlement to a new position without context of a terrain.
     *
     * @param player the player whose turn it is and who owns the settlement.
     * @param fromX  the old horizontal position of the settlement on the map.
     * @param fromY  the old vertical position of the settlement on the map.
     * @param toX    the new horizontal position of the settlement on the map.
     * @param toY    the new vertical position of the settlement on the map.
     */
    @Override
    public void unsafeMoveSettlement(Player player, int fromX, int fromY, int toX, int toY) {
        if (!canMoveSettlement(player, fromX, fromY, toX, toY))
            throw new RuntimeException("can't move settlement");

        /* we just read the message from the server, this is for AI later on
        // Take token from player if settlement was last one on special place
        map.at(fromX, fromY).surroundingSettlements(map, player).size() == 1
        if (map.playerHasOnlyOneSettlementNextToSpecialPlace(player, fromX, fromY)) {
            Tile token = map.specialPlaceInSurrounding(fromX, fromY);
            player.removeToken(token);
        }
         */

        map.at(fromX, fromY).moveSettlement(map.at(toX, toY));

        /*
        Tile token = map.specialPlaceInSurrounding(toX, toY);

        // Player gets a token if settlement is next to special place
        if (token != null) {
            player.addToken(token);
            map.at(toX, toY).takeTokenFromSpecialPlace();
        }

         */
    }

    /**
     * Use the oracle token. The player is allowed to place an extra settlement on a tile that has the same type as
     * the players' terrain card.
     *
     * @param player the player that is using the token.
     * @param x      the x position of the settlement to place.
     * @param y      the y position of the settlement to place.
     * @throws RuntimeException gets thrown when player can not use oracle token.
     */
    @Override
    public void useTokenOracle(Player player, int x, int y) {
        if (!canPlaceSettlement(player, player.getTerrainCard(), x, y))
            throw new RuntimeException("can't use token oracle");

        //placeSettlement(player, x, y);
        player.useToken(TileType.ORACLE);
    }

    /**
     * Use the farm token. The player can place an extra settlement on gras.
     *
     * @param player the player that is using the token.
     * @param x      the x position of the settlement to place.
     * @param y      the y position of the settlement to place.
     * @throws RuntimeException gets thrown when player can not use farm token.
     */
    @Override
    public void useTokenFarm(Player player, int x, int y) {
        if (!canPlaceSettlement(player, TileType.GRAS, x, y))
            throw new RuntimeException("can't use token Farm");

        //placeSettlement(player, x, y);
        player.useToken(TileType.FARM);
    }

    /**
     * Use the Tavern token. The player can place an extra settlement at the front or back of a
     * chain of settlements that is owned by the player.
     *
     * @param player the player that is using the token.
     * @param x      the x position of the settlement to place.
     * @param y      the y position of the settlement to place.
     * @throws RuntimeException gets thrown when player can not use tavern token.
     */
    @Override
    public void useTokenTavern(Player player, int x, int y) {
        if (!canPlaceSettlement(player, x, y) || !map.at(x,y).tileIsInFrontOrBackOfAChain(map, player))
            throw new RuntimeException("can't use token tavern");

        //placeSettlement(player, x, y);
        player.useToken(TileType.TAVERN);
    }

    /**
     * Use tower token. The player can place a token at the border of the map.
     *
     * @param player that is using the token.
     * @param x      the x position of the settlement to place.
     * @param y      the y position of the settlement to place.
     * @throws RuntimeException gets thrown when player can not use tower token.
     */
    @Override
    public void useTokenTower(Player player, int x, int y) {
        if (!canPlaceSettlement(player, x, y)
                || !allPossibleSettlementPlacementsOnBorderOfMap(player).contains(map.at(x, y)))
            throw new RuntimeException("can't use token tower");

        //placeSettlement(player, x, y);
        player.useToken(TileType.TOWER);
    }

    /**
     * Use Oasis token. The player can place an extra settlement on Desert.
     *
     * @param player the player that is using the token.
     * @param x      the x position of the settlement to place.
     * @param y      the y position of the settlement to place.
     * @throws RuntimeException gets thrown when player can not use oasis token.
     */
    @Override
    public void useTokenOasis(Player player, int x, int y) {
        if (canPlaceSettlement(player, TileType.DESERT, x, y))
            throw new RuntimeException("can't use token oasis");

        //placeSettlement(player, x, y);
        player.useToken(TileType.OASIS);
    }

    /**
     * Use Harbor token. The player can move a settlement from any tile to a water tile.
     *
     * @param player the player that is using the token.
     * @param fromX  the x coordinate of settlement to move.
     * @param fromY  the y coordinate of settlement to move.
     * @param toX    the x coordinate of target tile to put settlement.
     * @param toY    the y coordinate of target tile to put settlement.
     * @throws RuntimeException gets thrown when player can not use harbor token.
     */
    @Override
    public void useTokenHarbor(Player player, int fromX, int fromY, int toX, int toY) {
        //TODO: REVISIT THIS
        if (canMoveSettlementOnTerrain(player, TileType.WATER, fromX, fromY, toX, toY))
            throw new RuntimeException("can't use token harbor");

        //moveSettlementOnTerrain(player, TileType.WATER, fromX, fromY, toX, toY);
        player.useToken(TileType.HARBOR);
    }

    /**
     * Use Paddock token. The player can move a settlement two tiles in horizontal or diagonal
     * direction.
     *
     * @param player the player that is using the token.
     * @param fromX  the x coordinate of settlement to move.
     * @param fromY  the y coordinate of settlement to move.
     * @param toX    the x coordinate of target tile to put settlement.
     * @param toY    the y coordinate of target tile to put settlement.
     * @throws RuntimeException gets thrown when player can not use paddock token.
     */
    @Override
    public void useTokenPaddock(Player player, int fromX, int fromY, int toX, int toY) {
        if (canPlaceSettlement(player, toX, toY))
            throw new RuntimeException("can't use token paddock");

        //moveSettlement(player, fromX, fromY, toX, toY);
        player.useToken(TileType.PADDOCK);
    }

    /**
     * Use Barn token. The player can move a settlement on a tile with current terrain  card.
     *
     * @param player the player that is using the token.
     * @param fromX  the x coordinate of settlement to move.
     * @param fromY  the y coordinate of settlement to move.
     * @param toX    the x coordinate of target tile to put settlement.
     * @param toY    the y coordinate of target tile to put settlement.
     * @throws RuntimeException gets thrown when player can not use barn token.
     */
    @Override
    public void useTokenBarn(Player player, int fromX, int fromY, int toX, int toY) {
        if (!canMoveSettlementOnTerrain(player, player.getTerrainCard(), fromX, fromY, toX, toY))
            throw new RuntimeException("can't use token paddock");

        //moveSettlementOnTerrain(player, player.terrainCard, fromX, fromY, toX, toY);
        player.useToken(TileType.BARN);
    }
}
