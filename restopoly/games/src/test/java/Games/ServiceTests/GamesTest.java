package Games.ServiceTests;

import Games.Game;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Assert;
import org.junit.Test;

public class GamesTest extends JsonServiceTest {

    @Test
    public void validNewGame() throws UnirestException {
        HttpResponse<JsonNode> request = newGameRequest();
        Assert.assertEquals(201, request.getStatus());

        Game game = gson.fromJson(request.getBody().toString(), Game.class);
        Assert.assertTrue(game.getGameid().length() > 0);
        Assert.assertTrue(game.getUri().length() > 0);
        Assert.assertTrue(game.getPlayers().size() == 0);
    }

    private Game newGame() throws UnirestException {
        return gson.fromJson(newGameRequest().getBody().toString(), Game.class);
    }

    private HttpResponse<JsonNode> newGameRequest() throws UnirestException {
        return Unirest.post(baseUri + "/games").asJson();
    }

}
