package Boards;


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

        post("/boards/:gameid/players/:playerid/roll", (request, response) -> {
            try {
                Throw theThrow = gson.fromJson(request.body(), Throw.class);

                return service.roll(request.params(":gameid"), request.params(":playerid"), theThrow);
            } catch(NullPointerException|JsonSyntaxException e) {
                response.status(400);
                return "";
            }
        }, gson::toJson);

        put("/boards/:gameid/players/:playerid", (request, response) -> {
            return service.addPlayer(request.params(":gameid"), request.params(":playerid"));
        }, gson::toJson);

        get("/boards/:gameid/players/:playerid", (request, response) -> {
            return service.getPlayer(request.params(":gameid"), request.params(":playerid"));
        }, gson::toJson);

        get("/boards/:gameid", (request, response) -> {
            return service.getBoard(request.params(":gameid"));
        }, gson::toJson);

        put("/boards/:gameid", (request, response) -> {
            return service.newBoard(request.params(":gameid"));
        }, gson::toJson);

        get("/boards", (request, response) -> {
            return service.getBoards();
        }, gson::toJson);

//        service.register();
    }

    public static void main(String[] args) {
        new Router();
    }

}
