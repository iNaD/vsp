package Client;



import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONObject;

import static Client.Tools.readCli;

public class Client {

    protected static String gameId = null;

    protected static PlayerService playerService;

    public static void main(String[] args) throws UnirestException {
        playerService = new PlayerService();

        System.out.println("Welcome to RESTopoly!");

        Options.menu();

        System.out.println("To start or join a game we need your player id.");

        readPlayerId();

        System.out.println("That was easy, wasn't it? Now do you want to start a new game or join an existing one?");

        gameMenu();

        if(gameId != null && playerService.getPlayer() != null) {
            System.out.println("Registering you (" + playerService.getPlayer().getId() + ") with the game " + gameId + ".");
            System.out.println(addPlayerToGame(gameId, playerService.getPlayer()));

            playerService.startRouter();

            waitForReady();

            System.out.println("You (" + playerService.getPlayer().getId() + ") are ready to play!");

            waitForQuit();

            playerService.stopRouter();
        } else {
            System.out.println("Hmm... seems we are missing either the game or the player id. Please try again :(");
        }
    }

    private static void waitForQuit() {
        String input = readCli("Press q if you want to quit:");

        if(input.equals("q")) {
            return;
        } else {
            System.out.println("Not q, still waiting for quit.");
            waitForQuit();
        }
    }

    public static JSONObject createGame() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.post(Options.getSetting("gamesUri"))
                .header("accept", "application/json")
                .asJson();

        return response.getBody().getObject();
    }

    public static JSONObject addPlayerToGame(String gameId, Player player) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.put(Options.getSetting("gamesUri") + "/{gameid}/players/{playerid}")
                .header("accept", "application/json")
                .routeParam("gameid", gameId)
                .routeParam("playerid", player.getId())
                .asJson();

        return response.getBody().getObject();
    }

    public static void readPlayerId() {
        String input = readCli("Please enter your player id:");

        if(input.length() > 0) {
            playerService.setPlayer(new Player(input));
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
                    .routeParam("playerid", playerService.getPlayer().getId())
                    .asString();

            if(response.getStatus() == 200) {
                playerService.getPlayer().setReady(true);
            } else {
                playerService.getPlayer().setReady(false);
            }
        } catch (UnirestException e) {
            e.printStackTrace();
            playerService.getPlayer().setReady(false);
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
	//   Spieler Grundstücke kaufen können durch
	//	post /brokers/{gameid}/places/{placeid}/owner
	public JSONObject kauf(String gameid, String placeid)
			throws UnirestException {
		HttpResponse<JsonNode> response = Unirest
				.post(Options.getSetting("brokerUri")
						+ "/{gameid}/places/{placeid}/owner")
				.header("accept", "application/json")
				.routeParam("gameid", gameid).routeParam("playerid", placeid)
				.asJson();
		return response.getBody().getObject();

	}

}
