package Client;

import com.google.gson.Gson;
import spark.Spark;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.post;

public class PlayerService {
    protected static Player player;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player p) {
        player = p;
    }

    public void stopRouter() {
        Spark.stop();
    }

    protected void startRouter() {
        Spark.port(4568);

        Gson gson = new Gson();

        after((request, response) -> {
            response.type("application/json");
        });

        post("/player/event", (request, response) -> {
            System.out.println("A bunch of events arrived:");
            Event[] events = gson.fromJson(request.body().toString(), Event[].class);
            for (Event event : events) {
                System.out.println(event);
            }
            return true;
        }, gson::toJson);

        post("/player/turn", (request, response) -> {
            System.out.println("It's our turn");
            return true;
        }, gson::toJson);

        get("/player", (request, response) -> {
            return player;
        }, gson::toJson);
    }
}
