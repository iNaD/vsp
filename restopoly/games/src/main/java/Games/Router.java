package Games;

import com.google.gson.Gson;
import static spark.Spark.*;

class Router {

    private Gson gson = new Gson();

    private GamesService service = new GamesService();

    public Router() {
        put("/games/:gameid/players/:playerid", (request, response) -> {
            response.type("application/json");
            return service.addPlayer(request.params(":gameid"), request.params(":playerid"));
        }, gson::toJson);

        post("/games", (request, response) -> {
            response.type("application/json");
            return service.newGame();
        }, gson::toJson);

        get("/games", (request, response) -> {
            response.type("application/json");
            return service.getGames();
        }, gson::toJson);
    }

    public static void main(String[] args) {
        new Router();
    }

}
