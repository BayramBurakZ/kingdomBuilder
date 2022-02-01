package kingdomBuilder.gamelogic;

/**
 * Exception whether the tile does not have a token.
 */
public class HasNoTokenException extends RuntimeException {

    /**
     * Constructs an exception whether the tile does not have a token.
     *
     * @param message the detail message. The detail message is saved for later retrieval
     *                by the Throwable.getMessage() method.
     */
    HasNoTokenException(String message) {
        super(message);
    }

    HasNoTokenException(){}
}