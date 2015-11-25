package Client;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

import static Client.Tools.readCli;

public class Client {

    private static boolean playerReady = false;
    private static String playerId = null;
    private static String gameId = null;

    public static void main(String[] args) throws UnirestException {
        System.out.println("Welcome to RESTopoly!");

        Options.menu();

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
        HttpResponse<JsonNode> response = Unirest.post(Options.getSetting("gamesUri"))
                .header("accept", "application/json")
                .asJson();

        return response.getBody().getObject();
    }

    public static JSONObject addPlayerToGame(String gameId, String playerId) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.put(Options.getSetting("gamesUri") + "/{gameid}/players/{playerid}")
                .header("accept", "application/json")
                .routeParam("gameid", gameId)
                .routeParam("playerid", playerId)
                .asJson();

        return response.getBody().getObject();
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

        try {
            HttpResponse<String> response = Unirest.put(Options.getSetting("gamesUri") + "/{gameid}/players/{playerid}/ready")
                    .header("accept", "application/json")
                    .routeParam("gameid", gameId)
                    .routeParam("playerid", playerId)
                    .asString();

            if(response.getStatus() == 200) {
                playerReady = true;
            } else {
                playerReady = false;
            }
        } catch (UnirestException e) {
            e.printStackTrace();
            playerReady = false;
        }
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
