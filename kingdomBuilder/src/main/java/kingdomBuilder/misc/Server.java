package kingdomBuilder.misc;

import java.io.*;
import java.nio.file.Path;
import java.util.MissingResourceException;

/**
 * Utility class, which provides a mechanism to extra the server binary from resources
 * and launch the process.
 */
public class Server {

    /**
     * Stores the key under which the server is available.
     */
    private static final String resourceKey = "gameserver-v1.3.2-complete.jar";

    /**
     * Stores a path to the "java" binary used to launch the server.
     */
    private static final Path javaPath = Path.of(System.getProperty("java.home"), "/bin/", "java");

    /**
     * Launches the game server as an external process.
     *
     * The method first "unpacks" the binary into a temporary file, which will be deleted on program exit,
     * then invokes the external process, which also will be cleaned up on program exit.
     *
     * @return the process representing the running server.
     * @throws IOException when the extraction of the server fails due to unforeseen circumstances.
     */
    public static Process launch() throws IOException {
        // Retrieve the input stream of the embedded server.
        InputStream stream = Server.class.getResourceAsStream(resourceKey);
        if(stream == null)
            throw new MissingResourceException(
                    "Failed to retrieve input stream for server binary",
                    Server.class.getName(),
                    resourceKey);

        // Create a temporary file to "temporarly extract" the server binary.
        File binary = File.createTempFile("server", ".jar");
        binary.deleteOnExit();

        // Write binary to our temp file
        try (OutputStream out = new FileOutputStream(binary)) {
            stream.transferTo(out);
        }

        // Launch the server
        Process process = new ProcessBuilder(
                javaPath.toString(),
                "-jar",
                binary.getAbsolutePath().toString()
        ).start();

        // Invoked by the JVM when our calling process exits.
        Runtime.getRuntime().addShutdownHook(new Thread(process::destroy));

        return process;
    }

}
