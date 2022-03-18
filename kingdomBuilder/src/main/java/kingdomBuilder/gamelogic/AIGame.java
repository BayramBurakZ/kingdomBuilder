package kingdomBuilder.gamelogic;


import kingdomBuilder.KBState;
import kingdomBuilder.reducers.BotReducer;
import kingdomBuilder.redux.Store;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AIGame {

    public static void randomPlacement(GameMap gameMap, Player player, TileType tileType, Store<KBState> store) {
        // add Tokens somehow
        GameMap AIMap = new GameMap(gameMap);
        List<Tile> plannedMoves = new ArrayList<>();

        for (int i = 0; i < player.remainingSettlementsOfTurn; i++) {

            List<Tile> placeable = AIMap.getAllPlaceableTiles(player, tileType).toList();
            int index = (int) (Math.random() * (placeable.size()));
            Tile t = placeable.get(index);
            System.out.println(t.x + "|" + t.y + "| " + t.tileType);
            plannedMoves.add(t);
            AIMap.at(t.x, t.y).placeSettlement(player);
        }

        for (Tile t : plannedMoves)
            t.removeSettlement();

        store.dispatch(BotReducer.PLACE_BOT, plannedMoves);

    }
}



//import java.util.Set;
//// Temporary storage for methods that will be relevant for AI players later on
//public class AIGame extends Game {
//
//    /**
//     * Throws if it's not the given player's turn.
//     *
//     * @param player the player to check if it's their turn.
//     */
//    protected void checkIsPlayersTurn(Player player) {
//        if (!isPlayersTurn(player))
//            throw new RuntimeException("It's not the player's turn!");
//    }
//
//    /**
//     * Checks if the given player can still place settlements this turn.
//     *
//     * @param player the player to check if they can still place settlements this turn.
//     */
//    protected void checkHasRemainingSettlements(Player player) {
//        if (!player.hasRemainingSettlements())
//            throw new RuntimeException("Player has no settlements left!");
//    }
//
//    /**
//     * Moves a settlement to a new position within a given terrain.
//     *
//     * @param player  the player whose turn it is and who owns the settlement.
//     * @param terrain the terrain it is getting placed.
//     * @param fromX   the old horizontal position of the settlement on the map.
//     * @param fromY   the old vertical position of the settlement on the map.
//     * @param toX     the new horizontal position of the settlement on the map.
//     * @param toY     the new vertical position of the settlement on the map.
//     */
//    public void moveSettlement(GameMap gameMap, Player player, TileType terrain, int fromX, int fromY, int toX, int toY) {
//
//        if (!canMoveSettlement(gameMap, player, terrain, fromX, fromY, toX, toY))
//            throw new RuntimeException("can't move settlement on terrain");
//
//      /* we just read the message from the server, this is for AI later on
//        // Take token from player if settlement was last one on special place
//        map.at(fromX, fromY).surroundingSettlements(map, player).size() == 1
//        if (map.playerHasOnlyOneSettlementNextToSpecialPlace(player, fromX, fromY)) {
//            Tile token = map.specialPlaceInSurrounding(fromX, fromY);
//            player.removeToken(token);
//        }
//
//        // Take token from player if settlement was last one on special place
//        if (map.playerHasOnlyOneSettlementNextToSpecialPlace(player, fromX, fromY)) {
//
//            Tile token = map.specialPlaceInSurrounding(fromX, fromY);
//            player.removeToken(token);
//        }
//        */
//
//        gameMap.at(fromX, fromY).moveSettlement(gameMap.at(toX, toY));
//
//        /*
//        Tile token = map.specialPlaceInSurrounding(toX, toY);
//
//        // Player gets a token if settlement is next to special place
//        if (token != null) {
//            player.addToken(token);
//            map.at(toX, toY).takeTokenFromSpecialPlace();
//        }
//
//         */
//    }
//
//    /**
//     * Places a settlement as a basic turn and throws if the move is not valid.
//     *
//     * @param x      the horizontal position of the settlement on the map.
//     * @param y      the vertical position of the settlement on the map.
//     * @param player the player whose turn it is and who owns the settlement.
//     */
//    @Override
//    public void unsafePlaceSettlement(Player player, int x, int y) {
//        if (!canUseBasicTurn(player, x, y))
//            throw new RuntimeException("can't place settlement");
//
//        gameMap.at(x, y).placeSettlement(player);
//        player.remainingSettlements--;
//
//        Set<Tile> specialPlaces = gameMap.at(x, y).surroundingSpecialPlaces(gameMap);
//
//        // Player gets a token if settlement is next to special place
//        for (var specialPlace : specialPlaces) {
//            player.addToken(specialPlace);
//        }
//    }
//
//    /**
//     * Moves a settlement to a new position without context of a terrain.
//     *
//     * @param player the player whose turn it is and who owns the settlement.
//     * @param fromX  the old horizontal position of the settlement on the map.
//     * @param fromY  the old vertical position of the settlement on the map.
//     * @param toX    the new horizontal position of the settlement on the map.
//     * @param toY    the new vertical position of the settlement on the map.
//     */
//    @Override
//    public void unsafeMoveSettlement(Player player, int fromX, int fromY, int toX, int toY) {
//        if (!canMoveSettlement(player, fromX, fromY, toX, toY))
//            throw new RuntimeException("can't move settlement");
//
//        /* we just read the message from the server, this is for AI later on
//        // Take token from player if settlement was last one on special place
//        map.at(fromX, fromY).surroundingSettlements(map, player).size() == 1
//        if (map.playerHasOnlyOneSettlementNextToSpecialPlace(player, fromX, fromY)) {
//            Tile token = map.specialPlaceInSurrounding(fromX, fromY);
//            player.removeToken(token);
//        }
//         */
//
//        gameMap.at(fromX, fromY).moveSettlement(gameMap.at(toX, toY));
//
//        /*
//        Tile token = map.specialPlaceInSurrounding(toX, toY);
//
//        // Player gets a token if settlement is next to special place
//        if (token != null) {
//            player.addToken(token);
//            map.at(toX, toY).takeTokenFromSpecialPlace();
//        }
//
//         */
//    }
//
//    /**
//     * Use the oracle token. The player is allowed to place an extra settlement on a tile that has the same type as
//     * the players' terrain card.
//     *
//     * @param player the player that is using the token.
//     * @param x      the x position of the settlement to place.
//     * @param y      the y position of the settlement to place.
//     * @throws RuntimeException gets thrown when player can not use oracle token.
//     */
//    @Override
//    public void useTokenOracle(Player player, int x, int y) {
//        if (!canUseBasicTurn(player, player.getTerrainCard(), x, y))
//            throw new RuntimeException("Can't use token oracle on"
//                    + " tile: " + map.at(x, y).tileType + " at " + x + "," + y);
//
//        //placeSettlement(player, x, y);
//        player.useToken(TileType.ORACLE);
//    }
//
//    /**
//     * Use the farm token. The player can place an extra settlement on gras.
//     *
//     * @param player the player that is using the token.
//     * @param x      the x position of the settlement to place.
//     * @param y      the y position of the settlement to place.
//     * @throws RuntimeException gets thrown when player can not use farm token.
//     */
//    @Override
//    public void useTokenFarm(Player player, int x, int y) {
//        if (!canUseBasicTurn(player, TileType.GRAS, x, y))
//            throw new RuntimeException("Can't use token farm on"
//                    + " tile: " + map.at(x, y).tileType + " at " + x + "," + y);
//
//        //placeSettlement(player, x, y);
//        player.useToken(TileType.FARM);
//    }
//
//    /**
//     * Use the Tavern token. The player can place an extra settlement at the front or back of a
//     * chain of settlements that is owned by the player.
//     *
//     * @param player the player that is using the token.
//     * @param x      the x position of the settlement to place.
//     * @param y      the y position of the settlement to place.
//     * @throws RuntimeException gets thrown when player can not use tavern token.
//     */
//    @Override
//    public void useTokenTavern(Player player, int x, int y) {
//        if (!canUseBasicTurn(player, x, y) || !gameMap.at(x,y).isAtEndOfAChain(gameMap, player))
//            throw new RuntimeException("Can't use token tavern on"
//                    + " tile: " + map.at(x, y).tileType + " at " + x + "," + y);
//
//        //placeSettlement(player, x, y);
//        player.useToken(TileType.TAVERN);
//    }
//
//    /**
//     * Use tower token. The player can place a token at the border of the map.
//     *
//     * @param player that is using the token.
//     * @param x      the x position of the settlement to place.
//     * @param y      the y position of the settlement to place.
//     * @throws RuntimeException gets thrown when player can not use tower token.
//     */
//    @Override
//    public void useTokenTower(Player player, int x, int y) {
//        /*
//        if (!canUseBasicTurn(player, x, y)
//                || !allPossibleSettlementPlacementsOnBorderOfMap(player).contains(map.at(x, y)))
//            throw new RuntimeException("Can't use token oracle on"
//                    + " tile: " + map.at(x, y).tileType + " at " + x + "," + y);
//        */
//        //placeSettlement(player, x, y);
//        player.useToken(TileType.TOWER);
//    }
//
//    /**
//     * Use Oasis token. The player can place an extra settlement on Desert.
//     *
//     * @param player the player that is using the token.
//     * @param x      the x position of the settlement to place.
//     * @param y      the y position of the settlement to place.
//     * @throws RuntimeException gets thrown when player can not use oasis token.
//     */
//    @Override
//    public void useTokenOasis(Player player, int x, int y) {
//        if (canUseBasicTurn(player, TileType.DESERT, x, y))
//            throw new RuntimeException("Can't use token oasis on"
//                    + " tile: " + map.at(x, y).tileType + " at " + x + "," + y);
//
//        //placeSettlement(player, x, y);
//        player.useToken(TileType.OASIS);
//    }
//
//    /**
//     * Use Harbor token. The player can move a settlement from any tile to a water tile.
//     *
//     * @param player the player that is using the token.
//     * @param fromX  the x coordinate of settlement to move.
//     * @param fromY  the y coordinate of settlement to move.
//     * @param toX    the x coordinate of target tile to put settlement.
//     * @param toY    the y coordinate of target tile to put settlement.
//     * @throws RuntimeException gets thrown when player can not use harbor token.
//     */
//    @Override
//    public void useTokenHarbor(Player player, int fromX, int fromY, int toX, int toY) {
//        //TODO: REVISIT THIS
//        if (canMoveSettlement(player, TileType.WATER, fromX, fromY, toX, toY))
//            throw new RuntimeException("Can't use token harbor on"
//                    + " tile from: " + map.at(fromX, fromY).tileType + " at " + fromX + "," + fromY
//                    + " tile to: " + map.at(fromX, fromY).tileType + " at " + toX + "," + toY);
//
//        //moveSettlementOnTerrain(player, TileType.WATER, fromX, fromY, toX, toY);
//        player.useToken(TileType.HARBOR);
//    }
//
//    /**
//     * Use Paddock token. The player can move a settlement two tiles in horizontal or diagonal
//     * direction.
//     *
//     * @param player the player that is using the token.
//     * @param fromX  the x coordinate of settlement to move.
//     * @param fromY  the y coordinate of settlement to move.
//     * @param toX    the x coordinate of target tile to put settlement.
//     * @param toY    the y coordinate of target tile to put settlement.
//     * @throws RuntimeException gets thrown when player can not use paddock token.
//     */
//    @Override
//    public void useTokenPaddock(Player player, int fromX, int fromY, int toX, int toY) {
//        if (canUseBasicTurn(player, toX, toY))
//            throw new RuntimeException("Can't use token paddock on"
//                    + " tile from: " + map.at(fromX, fromY).tileType + " at " + fromX + "," + fromY
//                    + " tile to: " + map.at(fromX, fromY).tileType + " at " + toX + "," + toY);
//
//        //moveSettlement(player, fromX, fromY, toX, toY);
//        player.useToken(TileType.PADDOCK);
//    }
//
//    /**
//     * Use Barn token. The player can move a settlement on a tile with current terrain  card.
//     *
//     * @param player the player that is using the token.
//     * @param fromX  the x coordinate of settlement to move.
//     * @param fromY  the y coordinate of settlement to move.
//     * @param toX    the x coordinate of target tile to put settlement.
//     * @param toY    the y coordinate of target tile to put settlement.
//     * @throws RuntimeException gets thrown when player can not use barn token.
//     */
//    @Override
//    public void useTokenBarn(Player player, int fromX, int fromY, int toX, int toY) {
//        if (!canMoveSettlement(player, player.getTerrainCard(), fromX, fromY, toX, toY))
//            throw new RuntimeException("Can't use token barn on"
//                    + " tile from: " + map.at(fromX, fromY).tileType + " at " + fromX + "," + fromY
//                    + " tile to: " + map.at(fromX, fromY).tileType + " at " + toX + "," + toY);
//
//        //moveSettlementOnTerrain(player, player.terrainCard, fromX, fromY, toX, toY);
//        player.useToken(TileType.BARN);
//    }
//}
