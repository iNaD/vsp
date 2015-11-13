package Games;

import com.google.gson.Gson;
import static spark.Spark.*;

class Router {

    private Gson gson = new Gson();

    public Router() {

        put("/games/:gameid/players/:playerid", (request, response) -> {
            Game game = new Game();
            game.setGameid(request.params(":gameid"));
            game.addPlayer(request.params(":playerid"));
            response.type("application/json");
            return game;
        }, gson::toJson);

        post("/games", (request, response) -> {
            Game game = new Game();
            game.setGameid("42");
            response.type("application/json");
            return game;
        }, gson::toJson);

        get("/games", (request, response) -> {
            Game game = new Game();
            game.setGameid("42");
            response.type("application/json");
            return game;
        }, gson::toJson);
    }

    public static void main(String[] args) {
        new Router();
    }

}
