package kingdomBuilder;

import javafx.application.Application;
import kingdomBuilder.gui.KingdomBuilderApplication;

import java.io.IOException;

/**
 * Class that is used to boot the program.
 */
public class Boot {

    /**
     * Starts the application.
     * @param args the command line arguments.
     * @throws IOException When something goes wrong.
     */
    public static void main(String[] args) throws IOException {
        Application.launch(KingdomBuilderApplication.class);
    }

}
