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

    private static Service service = new Service(
        "Games Olga-Daniel",
        "Provides Game actions",
        "games",
        "https://vs-docker.informatik.haw-hamburg.de/ports/11170/games");

    private static String serviceRegistrationUri = "http://vs-docker.informatik.haw-hamburg.de:8053/services";

    private List<Game> games = new ArrayList<>();

    private Gson gson = new Gson();

    public Game newGame(String json) {
        Gson gson = new Gson();
        Game game = gson.fromJson(json, Game.class);

        game.setGameid(UUID.randomUUID().toString());
        game.setUri(service.getUri() + "/" + game.getGameid());

        game.getComponents().game = game.getUri();

        try {
            String boardUri = newBoard(game);
            game.getComponents().board = boardUri;
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        try {
            String bankUri = newBank(game);
            game.getComponents().bank = bankUri;
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        game.getComponents().broker = null;
        game.getComponents().decks = null;

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

    public String newBank(Game game) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.put(game.getComponents().bank)
                .header("accept", "application/json")
                .body(gson.toJson(game))
                .asJson();

        String location = null;

        if(response.getStatus() == 200) {
            location = response.getHeaders().getFirst("Location");
        }

        return location;
    }

	public String newBoard(Game game) throws UnirestException {
        Gson gson = new Gson();

        HttpResponse<JsonNode> response = Unirest.put(game.getComponents().board)
            .header("accept", "application/json")
            .body(gson.toJson(game))
            .asJson();

        String location = null;

        if(response.getStatus() == 200) {
            location = response.getHeaders().getFirst("Location");
        }

        return location;
	}

	public JSONObject newBoardPlayer(Game game, Player player)throws UnirestException{
		HttpResponse<JsonNode> response = Unirest.put(game.getComponents().board + "/players/{playerid}")
            .header("accept","application/json")
            .routeParam("playerid", player.getId())
            .asJson();
		return response.getBody().getObject();
	}

}
