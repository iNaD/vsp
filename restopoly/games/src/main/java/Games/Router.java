package Games;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import static spark.Spark.*;

class Router {

    private Gson gson = new Gson();

    private GamesService service = new GamesService();

    public Router() {
        after((request, response) -> {
            response.type("application/json");
        });

        get("/games/:gameid/players/turn", (request, response) -> {
            return service.getTurnMutex(request.params(":gameid"));
        }, gson::toJson);

        put("/games/:gameid/players/turn", (request, response) -> {
            try {
                Player player = gson.fromJson(request.body(), Player.class);

                if (service.acquireMutex(request.params(":gameid"), player.getId())) {
                    response.status(201);
                } else {
                    if (service.getTurnMutex(request.params(":gameid")).getId().equals(player.getId())) {
                        response.status(200);
                    } else {
                        response.status(409);
                    }
                }
            } catch(NullPointerException|JsonSyntaxException e) {
                response.status(400);
            }

            return "";
        }, gson::toJson);

        delete("/games/:gameid/players/turn", (request, response) -> {
            return service.releaseMutex(request.params(":gameid"));
        }, gson::toJson);

        get("/games/:gameid/players/current", (request, response) -> {
            return service.getGame(request.params(":gameid")).getTurnMutex();
        }, gson::toJson);

        put("/games/:gameid/players/:playerid/ready", (request, response) -> {
            service.getGame(request.params(":gameid")).getPlayer(request.params(":playerid")).setReady(true);
            return true;
        }, gson::toJson);

        get("/games/:gameid/players/:playerid/ready", (request, response) -> {
            return service.getGame(request.params(":gameid")).getPlayer(request.params(":playerid")).getReady();
        }, gson::toJson);

        put("/games/:gameid/players/:playerid", (request, response) -> {
        	String playeridRequest = request.params("playerid");
        	String gameidRequest = request.params("gameid");
            Player player = service.addPlayer(gameidRequest, playeridRequest, request.queryParams("name"), request.queryParams("uri"));

            service.newBoardPlayer(gameidRequest, playeridRequest);

            return player;
        }, gson::toJson);

        get("/games/:gameid/players", (request, response) -> {
            return service.getGame(request.params(":gameid")).getPlayers();
        }, gson::toJson);

        get("/games/:gameid", (request, response) -> {
            return service.getGame(request.params(":gameid"));
        }, gson::toJson);

        post("/games", (request, response) -> {
            response.status(201);
            Game game = service.newGame();
            String gameid = game.getGameid();
            //Unirest
            service.newBoard(gameid);
            return game;
        }, gson::toJson);

        get("/games", (request, response) -> {
            return service.getGames();
        }, gson::toJson);

        service.register();
    }

    public static void main(String[] args) {
        new Router();
    }

}
