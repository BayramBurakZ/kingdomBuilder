package kingdomBuilder;

import kingdomBuilder.network.CommandSink;
import kingdomBuilder.network.CommandSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Boot {

    public static void main(String[] args) throws IOException {
        System.out.println("Hello, Builder!");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            CommandSink sink = new CommandSocket("localhost", 6666);

            while (true) {
                System.out.print("> ");
                String line = reader.readLine();

                if (line.equals(".quit"))
                    break;

                String response = sink.sendCommandString(line);
                System.out.println(response);
            }
        } catch(Exception exc) {
             System.out.println("Something bad happened:");
             exc.printStackTrace();
        }
    }

}
