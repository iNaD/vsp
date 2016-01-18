package Games;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONObject;

public class GamesService {

    private static String serviceUri = "https://vs-docker.informatik.haw-hamburg.de/ports/11170/games";//    19850

    private static Service service = new Service("Games", "Provides Game actions", "games", serviceUri);

    private static String serviceRegistrationUri = "http://vs-docker.informatik.haw-hamburg.de:8053/services";

    private List<Game> games = new ArrayList<>();
    public static String getServiceUri() {
        return serviceUri;
    }

    public static void setServiceUri(String serviceUri) {
        GamesService.serviceUri = serviceUri;
    }

    public Game newGame(String json) {
        Gson gson = new Gson();
        Game game = gson.fromJson(json, Game.class);

        game.setGameid(UUID.randomUUID().toString());
        game.setUri(serviceUri + "/" + game.getGameid());

        game.getComponents().game = game.getUri();
        game.getComponents().bank = game.getComponents().bank + "/" + game.getGameid();
        game.getComponents().board = game.getComponents().board + "/" + game.getGameid();
        game.getComponents().broker = game.getComponents().broker + "/" + game.getGameid();
        game.getComponents().decks = game.getComponents().decks + "/" + game.getGameid();

        try {
            newBoard(game);
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        try {
            newBank(game);
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        games.add(game);

        return game;
    }

    public Game getGame(String gameid) {
        Game result = null;

        for(Game game : games) {
            if(game.getGameid().equals(gameid)) {
                result = game;
            }
        }

        return result;
    }

    public GamesList getGames() {
        return new GamesList(games);
    }

    public Player addPlayer(String gameid, Player player) throws UnirestException {
        Game game = getGame(gameid);

        game.addPlayer(player);

        newBoardPlayer(game, player);

        return player;
    }

    public Game releaseMutex(String gameid) {
        Game game = getGame(gameid);

        game.releaseMutex();

        return game;
    }

    public boolean acquireMutex(String gameid, String playerid) {
        Game game = getGame(gameid);
        Player player = game.getPlayer(playerid);

        return game.acquireMutex(player);
    }

    public Player getTurnMutex(String gameid) {
        return getGame(gameid).getTurnMutex();
    }

    public void register() {
        Gson gson = new Gson();

        try {
            HttpResponse<JsonNode> response = Unirest
                    .post(serviceRegistrationUri)
                    .header("accept", "application/json")
                    .header("content-type", "application/json")
                    .body(gson.toJson(service)).asJson();

            System.out.println("Status: " + response.getStatus() + " Body:" + response.getBody().toString());
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    public JSONObject newBank(Game game) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.put(game.getComponents().bank)
                .header("accept", "application/json")
                .body(game)
                .asJson();

        return response.getBody().getObject();
    }

	public JSONObject newBoard(Game game) throws UnirestException {
        Gson gson = new Gson();

        HttpResponse<JsonNode> response = Unirest.put(game.getComponents().board)
            .header("accept", "application/json")
            .body(gson.toJson(game))
            .asJson();

        return response.getBody().getObject();
	}

	public JSONObject newBoardPlayer(Game game, Player player)throws UnirestException{
		HttpResponse<JsonNode> response = Unirest.put(game.getComponents().board + "/players/{playerid}")
            .header("accept","application/json")
            .routeParam("playerid",player.getId())
            .asJson();
		return response.getBody().getObject();
	}

}
