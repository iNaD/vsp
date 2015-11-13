package Games;

import com.google.gson.Gson;
import static spark.Spark.*;

class Router {

    private Gson gson = new Gson();

    public Router() {
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
