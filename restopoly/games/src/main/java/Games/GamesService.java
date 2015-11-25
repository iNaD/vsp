package Games;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import spark.QueryParamsMap;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GamesService {

    private static Service service = new Service("Games", "Provides Game actions", "games", "https://vs-docker.informatik.haw-hamburg.de/ports/101170/games");

    private static String serviceRegistrationUri = "http://vs-docker:8053/services";

    private List<Game> games = new ArrayList<>();

    public Game newGame() {
        Game game = new Game();
        game.setGameid(UUID.randomUUID().toString());
        return addGame(game);
    }

    public Game addGame(Game game) {
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

    public List<Game> getGames() {
        return games;
    }

    public Game addPlayer(String gameid, String playerid, QueryParamsMap queryParams) {
        Game game = getGame(gameid);

        String name = queryParams.get("name").value();
        String uri = queryParams.get("uri").value();

        game.addPlayer(new Player(playerid, name, uri));

        return game;
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
                    .body(gson.toJson(service))
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

}
