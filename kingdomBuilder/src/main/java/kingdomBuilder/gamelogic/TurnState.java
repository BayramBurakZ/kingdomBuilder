package kingdomBuilder.gamelogic;

/**
 * Enum to represent a state of the turn.
 */
public enum TurnState {
    /**
     * Represents the state before the basic turn.
     */
    START_OF_TURN,

    /**
     * Represents the state while placing settlements as the basic turn.
     */
    BASIC_TURN,

    /**
     * Represents the state after placing all necessary settlements of a basic turn.
     */
    END_OF_TURN}
