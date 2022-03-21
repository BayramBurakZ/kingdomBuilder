package kingdomBuilder.network.protocol;

import kingdomBuilder.annotations.Protocol;

/**
 * Represents the error message whenever a wrong password is entered.
 */
@Protocol(format = "[ERROR_MESSAGE] [WRONG_PASSWORD]")
public record WrongPassword() {
}
