package Client;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

public class Tools {

    private static BufferedReader bufferReader = new BufferedReader(new InputStreamReader(System.in));
    private static Console console = System.console();

    public static String readCli(String message) {
        return readCli(message, false);
    }

    public static String readCli(String message, Boolean hidden) {
        message += " ";

        if(hidden == true) {
            return String.valueOf(console.readPassword(message));
        } else {
            return console.readLine(message);
        }
    }

}
