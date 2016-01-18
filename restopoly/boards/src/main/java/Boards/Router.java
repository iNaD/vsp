package Boards;


import com.mashape.unirest.http.exceptions.UnirestException;
import spark.Spark;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import static spark.Spark.*;

public class Router {

    private Gson gson = new Gson();

    private BoardsService service = new BoardsService();

    public Router() {
        after((request, response) -> {
            response.type("application/json");

        });

        post("/boards/:gameid/players/:playerid/move", (request, response) -> {
            service.move(request.params(":gameid"), request.params(":playerid"), Integer.parseInt(request.body()));
            return "";
        }, gson::toJson);

        post("/boards/:gameid/players/:playerid/roll", (request, response) -> {
            try {
                Throw theThrow = gson.fromJson(request.body(), Throw.class);

                return service.roll(request.params(":gameid"), request.params(":playerid"), theThrow);
            } catch(NullPointerException|JsonSyntaxException e) {
                response.status(400);
                return "";
            }
        }, gson::toJson);

        delete("/boards/:gameid/players/:playerid", (request, response) -> {
            service.removePlayer(request.params(":gameid"), request.params(":playerid"));
            return true;
        });

        put("/boards/:gameid/players/:playerid", (request, response) -> {
            Player player = service.addPlayer(request.params(":gameid"), new Player(request.params(":playerid")));

            response.header("Location", player.getUri());

            return player;
        }, gson::toJson);

        get("/boards/:gameid/players/:playerid", (request, response) -> {
            return service.getPlayer(request.params(":gameid"), request.params(":playerid"));
        }, gson::toJson);

        post("/boards/:gameid/players", (request, response) -> {
            Player player = gson.fromJson(request.body(), Player.class);

            service.addPlayer(request.params(":gameid"), player);

            response.status(201);
            response.header("Location", player.getUri());

            return true;
        }, gson::toJson);

        get("/boards/:gameid/players", (request, response) -> {
           return service.getBoard(request.params(":gameid")).getPlayers();
        }, gson::toJson);

        put("/boards/:gameid/places/:place", (request, response) -> {
            Place place = gson.fromJson(request.body(), Place.class);
            Field field = service.addPlace(request.params(":gameid"), request.params(":place"), place);

            response.header("Location", field.getUri());

            return field;
        }, gson::toJson);

        get("/boards/:gameid/places/:place", (request, response) -> {
            return service.getPlace(request.params(":gameid"), request.params(":place"));
        }, gson::toJson);

        get("/boards/:gameid/places", (request, response) -> {
           return service.getPlaces(request.params(":gameid"));
        }, gson::toJson);

        get("/boards/:gameid", (request, response) -> {
            return service.getBoard(request.params(":gameid"));
        }, gson::toJson);

        delete("/boards/:gameid", (request, response) -> {
            service.deleteBoard(request.params(":gameid"));
            return true;
        });

        put("/boards/:gameid", (request, response) -> {
            Game game = gson.fromJson(request.body(), Game.class);
            Board board = service.newBoard(game);

            response.header("Location", board.getUri());

            return board;
        }, gson::toJson);

        get("/boards", (request, response) -> {
            return service.getBoards();
        }, gson::toJson);

        service.register();
    }

    public static void main(String[] args) {
        Integer port = 4567;

        if(args.length > 0) {
            port = Integer.valueOf(args[0]);
        }

        Spark.port(port);

        new Router();
    }

}
