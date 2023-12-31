package kingdomBuilder;

import javafx.application.Application;
import kingdomBuilder.gui.KingdomBuilderApplication;
import kingdomBuilder.misc.Server;

import java.io.IOException;

/**
 * Class that is used to boot the program.
 */
public class Boot {

    /**
     * Starts the application.
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        Application.launch(KingdomBuilderApplication.class);
    }

}
