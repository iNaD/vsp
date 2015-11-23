package Games;

import com.google.gson.Gson;
import static spark.Spark.*;

class Router {

    private Gson gson = new Gson();

    private GamesService service = new GamesService();

    public Router() {
        get("/games/:gameid/players/turn", (request, response) -> {
            response.type("application/json");
            return service.getTurnMutex(request.params(":gameid"));
        }, gson::toJson);

        put("/games/:gameid/players/turn", (request, response) -> {
            response.type("application/json");

            if(service.acquireMutex(request.params(":gameid"), "123")) {
                response.status(201);
            } else {
                if(service.getTurnMutex(request.params(":gameid")).getId().equals("123")) {
                    response.status(200);
                } else {
                    response.status(409);
                }
            }

            return "";
        }, gson::toJson);

        delete("/games/:gameid/players/turn", (request, response) -> {
            response.type("application/json");
            return service.releaseMutex(request.params(":gameid"));
        }, gson::toJson);

        put("/games/:gameid/players/:playerid/ready", (request, response) -> {
            response.type("application/json");
            service.getGame(request.params(":gameid")).getPlayer(request.params(":playerid")).setReady(true);
            return null;
        }, gson::toJson);

        get("/games/:gameid/players/:playerid/ready", (request, response) -> {
            response.type("application/json");

            return service.getGame(request.params(":gameid")).getPlayer(request.params(":playerid")).getReady();
        }, gson::toJson);

        put("/games/:gameid/players/:playerid", (request, response) -> {
            response.type("application/json");
            return service.addPlayer(request.params(":gameid"), request.params(":playerid"));
        }, gson::toJson);

        get("/games/:gameid/players", (request, response) -> {
            response.type("application/json");

            return service.getGame(request.params(":gameid")).getPlayers();
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
