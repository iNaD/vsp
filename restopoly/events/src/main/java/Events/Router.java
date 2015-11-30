package Events;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.List;

import static spark.Spark.*;

class Router {

    private Gson gson = new Gson();

    private EventsService service = new EventsService();

    public Router() {
        after((request, response) -> {
            response.type("application/json");
        });

        get("/events", (request, response) -> {
            String gameid = request.queryParams("gameid");
            if(gameid != null) {
                return service.getEvents(gameid);
            } else {
                response.status(400);
                return null;
            }
        }, gson::toJson);

        service.register();
    }

    public static void main(String[] args) {
        new Router();
    }
}
