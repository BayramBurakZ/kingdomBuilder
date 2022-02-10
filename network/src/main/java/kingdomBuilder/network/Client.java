package kingdomBuilder.network;

import kingdomBuilder.network.protocol.*;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Represents a client, which interacts with the server it is connected to.
 * <br>
 * Clients must be created through {@link ClientSelector#connect(InetSocketAddress)}.
 */
public abstract class Client {
    /**
     * Represents a constant.
     */
    public static final int NO_ID = -1;

    /**
     * Represents the name of the client.
     */
    protected String name;
    /**
     * Represents the id of a client.
     */
    protected int clientId;
    /**
     * Represents the game-ID of the game in which the client plays.
     */
    protected int gameId;

    /**
     * Event that gets dispatched when the connection closed unexpectedly.
     */
    public final Event<Client> onConnectionLost = new Event<>();

    /**
     * Event that gets dispatched when the client successfully logged into the server.
     */
    public final Event<Client> onLoggedIn = new Event<>();

    /**
     * Event that gets dispatched when the client was kicked from the server.
     */
    public final Event<Client> onKicked = new Event<>();

    /**
     * Event that gets dispatched whenever a player joins the server.
     */
    public final Event<ClientJoined> onClientJoined = new Event<>();

    /**
     * Event that gets dispatched whenever a player leaves the server.
     */
    public final Event<ClientLeft> onClientLeft = new Event<>();

    /**
     * Event that gets dispatched whenever a (chat) message is received.
     */
    public final Event<Message> onMessageReceived = new Event<>();

    /**
     * Event that gets dispatched whenever a new game is hosted.
     */
    public final Event<GameHosted> onGameHosted = new Event<>();

    /**
     * Event that gets dispatched whenever a reply containing all client info is received.
     */
    public final Event<ClientsReply> onClientsReply = new Event<>();

    /**
     * Event that gets dispatched whenever a reply containing all games info is received.
     */
    public final Event<GamesReply> onGamesReply = new Event<>();

    /**
     * Event that gets dispatched whenever a reply containing all quadrant ids info is received.
     */
    public final Event<QuadrantsReply> onQuadrantsReply = new Event<>();

    /**
     * Event that gets dispatched whenever a reply containing the data of a quadrant is received.
     */
    public final Event<QuadrantReply> onQuadrantReply = new Event<>();

    /**
     * Event that gets dispatched whenever a client gets the root state.
     */
    public final Event<YouAreRoot> onYouAreRoot = new Event<>();

    /**
     * Event that gets dispatched whenever a client loads the namespace.
     */
    public final Event<NamespaceLoaded> onNamespaceLoaded = new Event<>();

    /**
     * Event that gets dispatched whenever a client unloads the namespace.
     */
    public final Event<NamespaceUnloaded> onNamespaceUnloaded = new Event<>();

    /**
     * Event that gets dispatched whenever a client starts spectating a game.
     */
    public final Event<YouSpectateGame> onYouSpectateGame = new Event<>();

    /**
     * Event that gets dispatched whenever a client stops spectating a game.
     */
    public final Event<StoppedSpectating> onStoppedSpectating = new Event<>();

    /**
     * Event that gets dispatched whenever a client left a game.
     */
    public final Event<YouLeftGame> onYouLeftGame = new Event<>();

    /**
     * Event that gets dispatched whenever a WelcomeToGame message is received.
     */
    public final Event<WelcomeToGame> onWelcomeToGame = new Event<>();

    /**
     * Event that gets dispatched whenever a player joins a game.
     */
    public final Event<PlayerJoined> onPlayerJoined = new Event<>();

    /**
     * Event that gets dispatched whenever a player left a game.
     */
    public final Event<PlayerLeft> onPlayerLeft = new Event<>();

    /**
     * Event that gets dispatched whenever a turn ends by the server.
     */
    public final Event<TurnEndedByServer> onTurnEndedByServer = new Event<>();

    /**
     * Event that gets dispatched whenever the server sends his version.
     */
    public final Event<VersionReply> onVersionReply = new Event<>();

    /**
     * Event that gets dispatched whenever a WhoAmIReply is received.
     */
    public final Event<WhoAmIReply> onWhoAmIReply = new Event<>();

    /**
     * Event that gets dispatched whenever a message that contains information regarding a specific client is received -
     * reply message to ?client-request {@link kingdomBuilder.network.protocol.ClientRequest}
     */
    public final Event<ClientReply> onClientReply = new Event<>();

    /**
     * Event that gets dispatched when information about the players of the current game is received -
     * reply message ?playersofgame {@link kingdomBuilder.network.protocol.PlayersOfGameRequest}.
     */
    public final Event<PlayersOfGameReply> onPlayersOfGameReply = new Event<>();

    /**
     * Event that gets dispatched when all registered modules and their namespaces are received -
     * reply message ?modules {@link kingdomBuilder.network.protocol.ModulesRequest}.
     */
    public final Event<ModulesReply> onModulesReply = new Event<>();

    /**
     * Event that gets dispatched when all namespaces currently registered for the requesting client are received -
     * reply message ?mymodules {@link kingdomBuilder.network.protocol.MyModulesRequest}.
     */
    public final Event<MyNamespacesReply> onMyNamespacesReply = new Event<>();

    /**
     * Event that gets dispatched whenever a message that contains the details of the game is received -
     * reply message to ?detailsofgame-request {@link kingdomBuilder.network.protocol.DetailsOfGameRequest}
     */
    public final Event<DetailsOfGameReply> onDetailsOfGameReply = new Event<>();

    /**
     * Event that gets dispatched whenever the game started initialization.
     */
    public final Event<InitStart> onInitStart = new Event<>();

    /**
     * Event that gets dispatched whenever a message that contains the win conditions of this game is received.
     */
    public final Event<WinCondition> onWinCondition = new Event<>();

    /**
     * Event that gets dispatched when the game started.
     */
    public final Event<GameStart> onGameStart = new Event<>();

    /**
     * Event that gets dispatched whenever a message containing the terrain card of your turn is received.
     */
    public final Event<YourTerrainCard> onYourTerrainCard = new Event<>();

    /**
     * Event that gets dispatched when a turn started.
     */
    public final Event<TurnStart> onTurnStart = new Event<>();

    /**
     * Event that gets dispatched whenever a message with the terrain card of the current turn is received.
     */
    public final Event<TerrainTypeOfTurn> onTerrainTypeOfTurn = new Event<>();

    /**
     * Event that gets dispatched whenever a settlement has been placed.
     */
    public final Event<SettlementPlaced> onSettlementPlaced = new Event<>();

    /**
     * Event that gets dispatched whenever a settlement has been removed.
     */
    public final Event<SettlementRemoved> onSettlementRemoved = new Event<>();

    /**
     * Event that gets dispatched whenever a token was given to a client.
     */
    public final Event<TokenReceived> onTokenReceived = new Event<>();

    /**
     * Event that gets dispatched whenever a token has been lost.
     */
    public final Event<TokenLost> onTokenLost = new Event<>();

    /**
     * Event that gets dispatched whenever a token has been used.
     */
    public final Event<TokenUsed> onTokenUsed = new Event<>();

    /**
     * Event that gets dispatched whenever a player used his/her last settlement.
     */
    public final Event<PlayerUsedLastSettlement> onPlayerUsedLastSettlement = new Event<>();

    /**
     * Event that gets dispatched when the game is over containing a list of the client-IDs of the winners.
     */
    public final Event<GameOver> onGameOver = new Event<>();

    /**
     * Event that gets dispatched whenever a message that contains scores of the players is received.
     */
    public final Event<Scores> onScores = new Event<>();

    /**
     * Event that gets dispatched whenever a quadrant has been uploaded.
     */
    public final Event<QuadrantUploaded> onQuadrantUploaded = new Event<>();

    /**
     * Event that gets dispatched whenever a message that contains the time limit is received.
     */
    public final Event<TimeLimitReply> onTimeLimitReply = new Event<>();

    /**
     * Event that gets dispatched whenever a message is received that contains the player limit of a game.
     */
    public final Event<PlayerLimitReply> onPlayerLimitReply = new Event<>();

    /**
     * Event that gets dispatched whenever a message is received that contains information about other players.
     */
    public final Event<PlayersReply> onPlayersReply = new Event<>();

    /**
     * Event that gets dispatched whenever a message is received that contains the current turn number of a game.
     */
    public final Event<TurnsReply> onTurnsReply = new Event<>();

    /**
     * Event that gets dispatched whenever a message is received that contains the turn limit of a game.
     */
    public final Event<TurnLimitReply> onTurnLimitReply = new Event<>();

    /**
     * Event that gets dispatched whenever a message is received that contains the player whose turn it is.
     */
    public final Event<WhoseTurnReply> onWhoseTurnReply = new Event<>();

    /**
     * Event that gets dispatched whenever a message is received that contains the amount of settlements left.
     */
    public final Event<SettlementsLeftReply> onSettlementsLeftReply = new Event<>();

    /**
     * Event that gets dispatched whenever a message is received that contains the information about the board.
     */
    public final Event<BoardReply> onBoardReply = new Event<>();

    /**
     * Event that gets dispatched whenever a message is received that contains the win cinditions of a game.
     */
    public final Event<WinConditionReply> onWinConditionReply = new Event<>();

    /**
     * Event that gets dispatched whenever a message is received that contains data of a game.
     */
    public final Event<MyGameReply> onMyGameReply = new Event<>();

    /**
     * Constructs a new client object.
     */
    public Client() {
        name = null;
        clientId = NO_ID;
        gameId = NO_ID;
    }

    /**
     * {@return the name of the client or null, if the client is NOT logged in.}
     */
    public String getName() {
        return name;
    }

    /**
     * {@return the id of the client or NO_ID, if the client is not logged in yet.}
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * {@return the id of the current game or NO_ID if the client has not joined any games.}
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * Logs into the server with a preferred name.
     * @param preferredName the preferred name.
     *
     * @apiNote This method may be non-blocking.
     */
    public abstract void login(String preferredName);

    /**
     * Logs out from the server.
     *
     * @apiNote This method may be non-blocking.
     */
    public abstract void logout();

    /**
     * Loads the kingdom_builder namespace.
     */
    public abstract void loadNamespace();

    /**
     * Requests the list of clients on the server.
     */
    //ToDo: @apiNote?
    public abstract void clientsRequest();

    /**
     * Requests the list of games on the server.
     */
    //ToDo: @apiNote?
    public abstract void gamesRequest();

    /**
     * Requests the list of quadrants (their IDs) on the server.
     */
    //ToDo: @apiNote?
    public abstract void quadrantsRequest();

    /**
     * Requests the data of a quadrant on the server.
     * @param quadrantId the id for the quadrant to request.
     */
    //ToDo: @apiNote?
    public abstract void quadrantRequest(int quadrantId);

    /**
     * Sends a message to all recipients.
     * @param recipients the recipients of the message.
     * @param message the message to send.
     */
    public abstract void chat(List<Integer> recipients, String message);

    /**
     * Sends the myGame message.
     */
    public abstract void myGameRequest();

    /**
     * Hosts a game with the given parameters.
     *
     * @param gameName the name of the game.
     * @param gameDescription the description of the game.
     * @param playerLimit the limit for players in the game.
     * @param timeLimit the limit for the time.
     * @param turnLimit the limit for the turns for the game.
     * @param quadrantId1 the first quadrant id.
     * @param quadrantId2 the second quadrant id.
     * @param quadrantId3 the third quadrant id.
     * @param quadrantId4 the fourth quadrant id.
     */
    public abstract void hostGame(
            String gameName,
            String gameDescription,
            int playerLimit,
            int timeLimit,
            int turnLimit,
            int quadrantId1,
            int quadrantId2,
            int quadrantId3,
            int quadrantId4);

    /**
     * Joins a game with the given id.
     * @param gameId the id.
     */
    public abstract void joinGame(int gameId);

    /**
     * Requests the board configuration.
     */
    public abstract void boardRequest();

    /**
     * Requests the players of a game.
     */
    public abstract void playersRequest();

    /**
     * {@return whether the client is connected.}
     */
    public abstract boolean isConnected();

    /**
     * Closes the connection of the client.
     */
    public abstract void disconnect();

    /**
     * Sends a place message to the server with the given coordinates.
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
    public abstract void placeSettlement(int x, int y);

    /**
     * Sends an EndTurn Message to the server.
     */
    public abstract void endTurn();

    /**
     * Sends an Oracle message to the server.
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
    public abstract void useTokenOracle(int x, int y);

    /**
     * Sends a Farm message to the server.
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
    public abstract void useTokenFarm(int x, int y);

    /**
     * Sends a Tavern message to the server.
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
    public abstract void useTokenTavern(int x, int y);

    /**
     * Sends a Tower message to the server.
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
    public abstract void useTokenTower(int x, int y);

    /**
     * Sends an Oasis message to the server.
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
    public abstract void useTokenOasis(int x, int y);

    /**
     * Sends a Harbor message to the server.
     * @param fromX the x-coordinate from which the settlement will be removed.
     * @param fromY the y-coordinate from which the settlement will be removed.
     * @param x the x-coordinate to which the settlement will be moved.
     * @param y the y-coordinate to which the settlement will be moved.
     */
    public abstract void useTokenHarbor(int fromX, int fromY, int x, int y);

    /**
     * Sends a Paddock message to the server.
     * @param fromX the x-coordinate from which the settlement will be removed.
     * @param fromY the y-coordinate from which the settlement will be removed.
     * @param x the x-coordinate to which the settlement will be moved.
     * @param y the y-coordinate to which the settlement will be moved.
     */
    public abstract void useTokenPaddock(int fromX, int fromY, int x, int y);

    /**
     * Sends a Barn message to the server.
     * @param fromX the x-coordinate from which the settlement will be removed.
     * @param fromY the y-coordinate from which the settlement will be removed.
     * @param x the x-coordinate to which the settlement will be moved.
     * @param y the y-coordinate to which the settlement will be moved.
     */
    public abstract void useTokenBarn(int fromX, int fromY, int x, int y);

    /**
     * Uploads a quadrant to the server.
     * @param quadrant the quadrant.
     */
    public abstract void uploadQuadrant(String quadrant);

    /**
     * {@return whether the client has commands stored internally
     *  that were not transmitted successfully to the server yet.}
     */
    public abstract boolean hasPendingCommands();

}
