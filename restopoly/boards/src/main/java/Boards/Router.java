package Boards;


import com.google.gson.Gson;

import static spark.Spark.*;

public class Router {

    private Gson gson = new Gson();

    private BoardsService service = new BoardsService();

    public Router() {
        post("/boards/:gameid/players/:playerid/roll", (request, response) -> {
            response.type("application/json");
            return service.roll(request.params(":gameid"), request.params(":playerid"));
        }, gson::toJson);

        put("/boards/:gameid/players/:playerid", (request, response) -> {
            response.type("application/json");
            return service.addPlayer(request.params(":gameid"), request.params(":playerid"));
        }, gson::toJson);

        get("/boards/:gameid/players/:playerid", (request, response) -> {
            response.type("application/json");
            return service.getPlayer(request.params(":gameid"), request.params(":playerid"));
        }, gson::toJson);

        get("/boards/:gameid", (request, response) -> {
            response.type("application/json");
            return service.getBoard(request.params(":gameid"));
        }, gson::toJson);

        put("/boards/:gameid", (request, response) -> {
            response.type("application/json");
            return service.newBoard(request.params(":gameid"));
        }, gson::toJson);

        get("/boards", (request, response) -> {
            response.type("application/json");
            return service.getBoards();
        }, gson::toJson);

//        service.register();
    }

    public static void main(String[] args) {
        new Router();
    }

}
