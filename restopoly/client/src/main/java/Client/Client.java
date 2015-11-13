package Client;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.cli.*;
import org.json.JSONObject;

public class Client {

    private static String gamesUri = "http://localhost:4567/games";

    private static boolean playerReady = false;
    private static String playerId = null;
    private static String gameId = null;

    private static Options getOptions() {
        Options options = new Options();

        Option h = new Option("h", "help", false, "Shows this help.");
        options.addOption(h);

        Option g = new Option("g", "gameId", true, "Start playing an existing game.");
        options.addOption(g);

        Option p = new Option("p", "playerId", true, "Specify your player id.");
        options.addOption(p);

        Option guri = new Option("guri", "gamesUri", true, "Uri to the games service.");
        options.addOption(guri);

        return options;
    }

    public static void main(String[] args) throws ParseException {
        Options options = getOptions();
        CommandLineParser parser = new GnuParser();

        CommandLine cmd = parser.parse(options, args);

        if(cmd.hasOption("h")) {
            HelpFormatter help = new HelpFormatter();

            options.getOption("p").setRequired(true);

            help.printHelp("restopoly-client", options, true);

            return;
        }

        if(cmd.hasOption("guri")) {
            gamesUri = cmd.getOptionValue("guri");
        }

        if(cmd.hasOption("p")) {
            playerId = cmd.getOptionValue("p");
        } else {
            System.out.println("Option p is mandatory.");
            return;
        }

        if(cmd.hasOption("g")) {
            gameId = cmd.getOptionValue("g");
        }

        System.out.println("Client started");

        try {
            if(gameId == null) {
                gameId = createGame().get("gameid").toString();
            }

            if(gameId != null && playerId != null) {
                System.out.println(addPlayerToGame(gameId, playerId).toString());
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        System.out.println("Client finished");
    }

    public static JSONObject createGame() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.post(gamesUri)
                .header("accept", "application/json")
                .asJson();

        return response.getBody().getObject();
    }

    public static JSONObject addPlayerToGame(String gameId, String playerId) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.put(gamesUri + "/{gameid}/players/{playerid}")
                .header("accept", "application/json")
                .routeParam("gameid", gameId)
                .routeParam("playerid", playerId)
                .asJson();

        return response.getBody().getObject();
    }

}
