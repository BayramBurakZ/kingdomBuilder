package kingdomBuilder;

import javafx.application.Application;

import java.io.IOException;

import kingdomBuilder.gui.KingdomBuilderApplication;

/**
 * Class that is used to boot the program.
 */
public class Boot {

    /**
     * Starts the application.
     * @param args The command line arguments.
     * @throws IOException When something goes wrong.
     */
    public static void main(String[] args) throws IOException {
        Application.launch(KingdomBuilderApplication.class);
    }
}
