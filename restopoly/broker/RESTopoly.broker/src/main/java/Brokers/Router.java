package Brokers;

import spark.Spark;

import com.google.gson.Gson;

import java.util.List;

import static spark.Spark.*;

public class Router {
	private Gson gson = new Gson();
	private BrokerService service = new BrokerService();

	public Router() {
		after((request, response) -> {
			response.type("application/json");
		});

        post("/brokers/:gameid/places/:placeid/visit/:playerid" , (request, response) -> {
            return service.visit(request.params(":gameid"),
					request.params(":placeid"),
					request.params(":playerid"));
        }, gson::toJson);

        put("/brokers/:gameid/places/:placeid/hypothecarycredit", (request, response) -> {
            List<Event> events = service.credit(request.params(":gameid"), request.params(":placeid"));

            if(events == null) {
                response.status(400);
                return "Hypothecary credit already acquired.";
            }

            return events;
        }, gson::toJson);

        delete("/brokers/:gameid/places/:placeid/hypothecarycredit", (request, response) -> {
            return service.deleteCredit(request.params(":gameid"), request.params(":placeid"));
        }, gson::toJson);

        put("/brokers/:gameid/places/:placeid/owner" , (request, response) -> {
            Player player = gson.fromJson(request.body(), Player.class);

            if(player == null) {
                response.status(400);
                return null;
            }

            List<Event> result = service.changeOwner(request.params(":gameid"),
                    request.params(":placeid"), player);

            return result;
        });

        post("/brokers/:gameid/places/:placeid/owner" , (request, response) -> service.setOwner(request, response), gson::toJson);

        get("/brokers/:gameid/places/:placeid/owner", (request, response) -> {
            return service.getOwner(request.params(":gameid"), request.params(":placeid"));
        }, gson::toJson);

        put("/brokers/:gameid/places/:placeid", (request, response) -> {
            Estate estate = gson.fromJson(request.body(), Estate.class);

            if(estate == null) {
                response.status(400);
                return false;
            }

            return service.addEstate(request.params(":gameid"), request.params(":placeid"), estate);
        }, gson::toJson);

        get("/brokers/:gameid/places/:placeid", (request, response) -> {
            Estate estate = service.getEstate(request.params(":gameid"), request.params(":placeid"));

            if(estate == null) {
                response.status(404);
            }

            return estate;
        }, gson::toJson);

        get("/brokers/:gameid/places", (request, response) -> {
            return service.getEstates(request.params("gameid"));
        }, gson::toJson);

        get("/brokers/:gameid", (request, response) -> {
            Broker broker = service.getBroker(request.params(":gameid"));
            if(broker == null) {
                response.status(404);
                return false;
            }

            return broker;
        }, gson::toJson);

        put("/brokers/:gameid", (request, response) -> {
            Game game = gson.fromJson(request.body(), Game.class);
            return service.newBroker(game);
        }, gson::toJson);

        service.register();
    }

	public static void main(String[] args)
    {
        Integer port = 4567;

        if(args.length > 0) {
            port = Integer.valueOf(args[0]);
        }

        Spark.port(port);

		new Router();
	}
}
