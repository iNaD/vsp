package Games.ServiceTests;

import Games.Game;
import Games.Player;
import Games.ServiceTests.Helpers.PlayerList;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class PlayersTest extends JsonServiceTest {

    @Test
    public void validNewPlayer() throws UnirestException {
        Game game = newGame();

        HttpResponse<JsonNode> response = newPlayerRequest(game, "foo", null, null);
        Assert.assertEquals(200, response.getStatus());

        Player player = gson.fromJson(response.getBody().toString(), Player.class);
        Assert.assertEquals("foo", player.getId());
    }

    @Test
    public void validNewPlayerWithName() throws UnirestException {
        Game game = newGame();

        HttpResponse<JsonNode> response = newPlayerRequest(game, "foo", "Foo", null);
        Assert.assertEquals(200, response.getStatus());

        Player player = gson.fromJson(response.getBody().toString(), Player.class);

        Assert.assertEquals("foo", player.getId());
        Assert.assertEquals("Foo", player.getName());
    }

    @Test
    public void validNewPlayerWithUri() throws UnirestException {
        Game game = newGame();

        HttpResponse<JsonNode> response = newPlayerRequest(game, "foo", null, "http://localhost/Foo");
        Assert.assertEquals(200, response.getStatus());

        Player player = gson.fromJson(response.getBody().toString(), Player.class);
        Assert.assertEquals("foo", player.getId());
        Assert.assertEquals("http://localhost/Foo", player.getUri());
        Assert.assertEquals(null, player.getName());
    }

    @Test
    public void validNewPlayerWithNameAndUri() throws UnirestException {
        Game game = newGame();

        HttpResponse<JsonNode> response = newPlayerRequest(game, "foo", "Foo", "http://localhost/Foo");
        Assert.assertEquals(200, response.getStatus());

        Player player = gson.fromJson(response.getBody().toString(), Player.class);
        Assert.assertEquals("foo", player.getId());
        Assert.assertEquals("Foo", player.getName());
        Assert.assertEquals("http://localhost/Foo", player.getUri());
    }

    @Test
    public void getPlayers() throws UnirestException {
        Game game = newGame();

        PlayerList expectedPlayers = new PlayerList();
        expectedPlayers.add(newPlayer(game, "foo", "Foo", "http://localhost/Foo"));
        expectedPlayers.add(newPlayer(game, "bar", "Bar", "http://localhost/Bar"));
        expectedPlayers.add(newPlayer(game, "baz", "Baz", "http://localhost/Baz"));

        HttpResponse<JsonNode> response = Unirest.get(baseUri + "/games/{gameid}/players")
                .routeParam("gameid", game.getGameid())
                .asJson();

        PlayerList players = gson.fromJson(response.getBody().toString(), PlayerList.class);

        Assert.assertTrue(players.containsAll(expectedPlayers));
    }

    @Test
    public void getPlayerReadyFalse() throws UnirestException {
        Game game = newGame();

        Player player = newPlayer(game, "foo", null, null);

        HttpResponse<String> response = Unirest
                .get(baseUri + "/games/{gameid}/players/{playerid}/ready")
                .routeParam("gameid", game.getGameid())
                .routeParam("playerid", player.getId())
                .asString();

        Assert.assertEquals(200, response.getStatus());

        Assert.assertFalse(gson.fromJson(response.getBody().toString(), Boolean.class));
    }

    @Test
    public void putPlayerReady() throws UnirestException {
        Game game = newGame();

        Player player = newPlayer(game, "foo", null, null);

        HttpResponse<String> response = Unirest
                .put(baseUri + "/games/{gameid}/players/{playerid}/ready")
                .routeParam("gameid", game.getGameid())
                .routeParam("playerid", player.getId())
                .asString();

        Assert.assertEquals(200, response.getStatus());

        Assert.assertTrue(gson.fromJson(response.getBody().toString(), Boolean.class));
    }

    @Test
    public void getPlayerReadyTrue() throws UnirestException {
        Game game = newGame();

        Player player = newPlayer(game, "foo", null, null);

        Unirest
            .put(baseUri + "/games/{gameid}/players/{playerid}/ready")
            .routeParam("gameid", game.getGameid())
            .routeParam("playerid", player.getId())
            .asString();

        HttpResponse<String> response = Unirest
                .get(baseUri + "/games/{gameid}/players/{playerid}/ready")
                .routeParam("gameid", game.getGameid())
                .routeParam("playerid", player.getId())
                .asString();

        Assert.assertEquals(200, response.getStatus());

        Assert.assertTrue(gson.fromJson(response.getBody().toString(), Boolean.class));
    }

    private Player newPlayer(Game game, String playerid, String name, String uri) throws UnirestException {
        return gson.fromJson(newPlayerRequest(game, playerid, name, uri).getBody().toString(), Player.class);
    }

    private HttpResponse<JsonNode> newPlayerRequest(Game game, String playerid, String name, String uri) throws UnirestException {
        String url = baseUri + "/games/{gameid}/players/{playerid}";
        Map<String, Object> query = new HashMap<>();

        if(name != null) {
            query.put("name", name);
        }

        if(uri != null) {
            query.put("uri", uri);
        }

        return Unirest.put(url).queryString(query).routeParam("gameid", game.getGameid()).routeParam("playerid", playerid).asJson();
    }

    private Game newGame() throws UnirestException {
        return gson.fromJson(Unirest.post(baseUri + "/games").asJson().getBody().toString(), Game.class);
    }

}
