package Events;

import com.google.gson.Gson;

import static spark.Spark.*;

class Router {

    private Gson gson = new Gson();

    private EventsService service = new EventsService();

    public Router() {
        after((request, response) -> {
            response.type("application/json");
        });

        delete("/events/subscriptions/:subscription", (request, response) -> {
            String gameid = request.queryParams("gameid");
            if(gameid != null) {
                service.removeSubscription(request.params(":subscription"));
                return true;
            } else {
                response.status(400);
                return false;
            }
        }, gson::toJson);

        post("/events/subscriptions", (request, response) -> {
            String gameid = request.queryParams("gameid");
            if(gameid != null) {
                Subscription subscription = Subscription.fromJson(request.body());

                subscription.setGameid(gameid);

                service.addSubscription(subscription);

                return subscription;
            } else {
                response.status(400);
                return false;
            }
        }, gson::toJson);

        get("/events/subscriptions", (request, response) -> {
            String gameid = request.queryParams("gameid");
            if(gameid != null) {
                return service.getSubscriptions(gameid);
            } else {
                response.status(400);
                return false;
            }
        }, gson::toJson);

        // TODO: Which eventid?! Global or local (per game)?
        get("/events/:eventid", (request, response) -> {
            String eventid = request.params(":eventid");

            return eventid;
        }, gson::toJson);

        /*
         * removes all events belonging to a specific game
         */
        delete("/events", (request, response) -> {
            String gameid = request.queryParams("gameid");
            if(gameid != null) {
                service.clearEvents(gameid);
                return true;
            } else {
                response.status(400);
                return false;
            }
        }, gson::toJson);

        post("/events", (request, response) -> {
            String gameid = request.queryParams("gameid");
            if(gameid != null) {
                Event event = gson.fromJson(request.body(), Event.class);
                service.addEvent(gameid, event);
                response.status(201);
                return true;
            } else {
                response.status(400);
                return false;
            }
        }, gson::toJson);

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
