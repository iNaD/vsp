package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Tools {

    private static BufferedReader bufferReader = new BufferedReader(new InputStreamReader(System.in));

    public static String readCli(String message) {
        String string;

        System.out.println(message);

        try {
            string = bufferReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return readCli(message);
        }

        return string;
    }

}
