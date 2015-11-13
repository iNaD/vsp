package Client;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Client {

    private static HashMap<String, String> options = new HashMap<String, String>(){{
        put("gamesUri", "http://localhost:4567/games");
        put("boardUri", "http://localhost:4567/boards");
    }};

    private static boolean playerReady = false;
    private static String playerId = null;
    private static String gameId = null;

    private static BufferedReader bufferReader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws UnirestException {
        System.out.println("Welcome to RESTopoly!");

        optionsMenu();

        System.out.println("To start or join a game we need your player id.");

        readPlayerId();

        System.out.println("That was easy, wasn't it? Now do you want to start a new game or join an existing one?");

        gameMenu();

        if(gameId != null && playerId != null) {
            System.out.println("Registering you (" + playerId + ") with the game " + gameId + ".");
            System.out.println(addPlayerToGame(gameId, playerId).toString());

            waitForReady();

            System.out.println("You (" + playerId + ") are ready to play!");
        } else {
            System.out.println("Hmm... seems we are missing either the game or the player id. Please try again :(");
        }
    }

    public static JSONObject createGame() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.post(options.get("gamesUri"))
                .header("accept", "application/json")
                .asJson();

        return response.getBody().getObject();
    }

    public static JSONObject addPlayerToGame(String gameId, String playerId) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.put(options.get("gamesUri") + "/{gameid}/players/{playerid}")
                .header("accept", "application/json")
                .routeParam("gameid", gameId)
                .routeParam("playerid", playerId)
                .asJson();

        return response.getBody().getObject();
    }

    public static void optionsMenu() {
        String decision = readCli("Modify the options? (y/n)[n]:");

        if(decision.equals("y")) {
            optionsTable();
        }
    }

    public static void optionsTable() {
        Integer firstCell = 20;
        Integer secondCell = 102;
        String leftAlignFormat = "| %-" + (firstCell - 2) + "s | %-" + (secondCell - 2) + "s |%n";
        String leftAlignFormatHead = "| %-" + (firstCell - 2) + "s | %-" + (secondCell - 2) + "s |%n";
        String tableBorder = "+";

        for (int i = 0; i < firstCell; i++) {
            tableBorder += "-";
        }

        tableBorder += "+";

        for (int i = 0; i < secondCell; i++) {
            tableBorder += "-";
        }

        tableBorder += "+%n";

        System.out.format(tableBorder);
        System.out.format(leftAlignFormatHead, "Option Name", "Value");
        System.out.format(tableBorder);
        for(HashMap.Entry<String, String> option : options.entrySet()) {
            System.out.format(leftAlignFormat, option.getKey(), option.getValue());
        }
        System.out.format(tableBorder);

        selectOption();
    }

    public static void selectOption() {
        String decision = readCli("Which option do you want to change? (Enter a name or 'q' to quit options menu) [q]:");
        if(decision.length() == 0 || decision.equals("q")) {
            return;
        } else {
            changeOption(decision);
        }
    }

    public static void changeOption(String option) {
        if(options.containsKey(option)) {
            String newValue = readCli("Please enter the new value for this option or leave empty to use current value. []:");

            if(newValue.length() > 0) {
                options.put(option, newValue);
            }

            optionsTable();
        } else {
            System.out.println("Unknown option " + option);
            selectOption();
        }
    }

    public static void readPlayerId() {
        String input = readCli("Please enter your player id:");

        if(input.length() > 0) {
            playerId = input;
        } else {
            System.out.println("Invalid player id: " + input);
            readPlayerId();
        }
    }

    public static void readGameId() {
        String input = readCli("Ok, please enter the id of the game you want to join:");

        if(input.length() > 0) {
            gameId = input;
        } else {
            System.out.println("Invalid game id: " + input);
            readGameId();
        }
    }

    public static void waitForReady() {
        readCli("Press Enter if you are ready to play:");

        playerReady = true;
    }

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

    public static void gameMenu() throws UnirestException {
        String decision = readCli("Start a new Game (y/n)[y]:");

        if(decision.length() == 0 || decision.equals("y")) {
            System.out.println("Creating a new game...");
            gameId = createGame().get("gameid").toString();
            System.out.println("New game created with id " + gameId + "!");
        } else {
            readGameId();
        }
    }

}
