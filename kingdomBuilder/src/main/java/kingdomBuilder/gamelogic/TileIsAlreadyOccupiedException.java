package kingdomBuilder.gamelogic;

/**
 * Exception whether the tile is already occupied.
 */
public class TileIsAlreadyOccupiedException extends RuntimeException {

    /**
     * Constructs an exception whether the tile is already occupied.
     *
     * @param message the detail message. The detail message is saved for later retrieval
     *                by the Throwable.getMessage() method.
     */
    TileIsAlreadyOccupiedException(String message) {
        super(message);
    }

    /**
     * Constructs an exception whether the tile is already occupied.
     */
    TileIsAlreadyOccupiedException(){}
}
