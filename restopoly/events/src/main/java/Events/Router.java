package Events;

import com.google.gson.Gson;
import spark.Spark;

import java.util.UUID;

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
                return "gameid is mandatory.";
            }
        }, gson::toJson);

        post("/events/subscriptions", (request, response) -> {
            String gameid = request.queryParams("gameid");
            if(gameid != null) {
                Subscription subscription = Subscription.fromJson(request.body());

                subscription.setGameid(gameid);
                subscription.setId(UUID.randomUUID().toString());

                service.addSubscription(subscription);

                return subscription;
            } else {
                response.status(400);
                return "gameid is mandatory.";
            }
        }, gson::toJson);

        get("/events/subscriptions", (request, response) -> {
            String gameid = request.queryParams("gameid");
            if(gameid != null) {
                return service.getSubscriptions(gameid);
            } else {
                response.status(400);
                return "gameid is mandatory.";
            }
        }, gson::toJson);

        get("/events/:eventid", (request, response) -> {
            String gameid = request.queryParams("gameid");
            String eventid = request.params(":eventid");

            if(gameid == null) {
                response.status(400);
                return "gameid is mandatory.";
            } else {
                Event event = service.getEvent(gameid, eventid);

                if (event == null) {
                    response.status(404);
                    return null;
                } else {
                    return event;
                }
            }
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
                return "gameid is mandatory.";
            }
        }, gson::toJson);

        post("/events", (request, response) -> {
            String gameid = request.queryParams("gameid");
            if(gameid != null) {
                Event event = gson.fromJson(request.body(), Event.class);
                if(event != null) {
                    event.setId(UUID.randomUUID().toString());

                    service.addEvent(gameid, event);
                    response.status(201);
                    return event;
                } else {
                    response.status(400);
                    return "no event was given.";
                }
            } else {
                response.status(400);
                return "gameid is mandatory.";
            }
        }, gson::toJson);

        get("/events", (request, response) -> {
            String gameid = request.queryParams("gameid");
            if(gameid != null) {
                EventsList events = service.getEvents(gameid);
                if(events != null) {
                    return events;
                } else {
                    response.status(404);
                    return null;
                }
            } else {
                response.status(400);
                return "gameid is mandatory.";
            }
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
