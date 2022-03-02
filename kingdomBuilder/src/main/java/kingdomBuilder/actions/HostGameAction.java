package kingdomBuilder.actions;

import kingdomBuilder.redux.Action;

/**
 * Represents the HostGameAction.
 * Used for the {@link kingdomBuilder.redux.Store#dispatchOld(Action) dispatch()}-method
 * in the {@link kingdomBuilder.redux.Store Store} so the reducer knows what type of action he needs to run.
 */
public class HostGameAction extends Action {
    /**
     * Represents the name of the game.
     */
    public String gameName;
    /**
     * Represents the description of the game.
     */
    public String gameDescription;
    /**
     * Represents the limit of players of the game.
     */
    public int playerLimit;
    /**
     * Represents the time limit of the game.
     */
    public int timeLimit;
    /**
     * Represents the turn limit of the game.
     */
    public int turnLimit;
    /**
     * Represents the id of the upper left quadrant.
     */
    public int quadrantId1;
    /**
     * Represents the id of the upper right quadrant.
     */
    public int quadrantId2;
    /**
     * Represents the id of the bottom left quadrant.
     */
    public int quadrantId3;
    /**
     * Represents the id of the bottom right quadrant.
     */
    public int quadrantId4;

    /**
     * Constructs a new HostGameAction.
     *
     * @param gameName the name.
     * @param gameDescription the description.
     * @param playerLimit the limit for players.
     * @param timeLimit the limit of the time.
     * @param turnLimit the limit for the turns.
     * @param quadrantId1 the first quadrant.
     * @param quadrantId2 the second quadrant.
     * @param quadrantId3 the third quadrant.
     * @param quadrantId4 the fourth quadrant.
     */
    public HostGameAction(String gameName,
                          String gameDescription,
                          int playerLimit,
                          int timeLimit,
                          int turnLimit,
                          int quadrantId1,
                          int quadrantId2,
                          int quadrantId3,
                          int quadrantId4) {
        this.gameName = gameName;
        this.gameDescription = gameDescription;
        this.playerLimit = playerLimit;
        this.timeLimit = timeLimit;
        this.turnLimit = turnLimit;
        this.quadrantId1 = quadrantId1;
        this.quadrantId2 = quadrantId2;
        this.quadrantId3 = quadrantId3;
        this.quadrantId4 = quadrantId4;
    }
}
