package Brokers;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import spark.Request;
import spark.Response;

import java.util.*;

public class BrokerService {

    private static Service service = new Service("Brokers",
            "Provides Broker interaction", "brokers",
            "https://vs-docker.informatik.haw-hamburg.de/ports/11173/brokers");

    private static String serviceRegistrationUri = "http://vs-docker.informatik.haw-hamburg.de:8053/services";

	private Map<String, Broker> brokers = new HashMap<>();

	public Broker getBroker(String gameID) {
		return brokers.get(gameID);
	}

	public Broker newBroker(String gameID) {
		Broker broker = new Broker();
		brokers.put(gameID, broker);
		return broker;
	}

	public List<Event> visit(String gameid, String placeid, String playerid) {
		Broker broker = getBroker(gameid);
		Estate estate = broker.getEstate(placeid);

        if(
            estate.isOwned()
            && !estate.getOwner().equals(playerid)
            && !broker.placeHasCredit(placeid)
        ) {
            try {
                HttpResponse<JsonNode> bankResponse = Unirest
                        .post(Options.getSetting("bankUri") + "/transfer/from/{from}/to/{to}/{amount}")
                        .routeParam("from", playerid)
                        .routeParam("to", estate.getOwner())
                        .routeParam("amount", estate.rent().toString())
                        .body("Rent for " + estate.getPlace())
                        .asJson();

                if(bankResponse.getStatus() == 201) {
                    Event event = new Event("rent-paid", "Rent paid", "Player paid rent for " + estate.getPlace(), estate.getPlace(), null);

                    List<Event> events = new ArrayList<>();
                    events.add(event);

                    return events;
                }
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        }

		return null;
	}

	public List<Event> setOwner(Request request, Response response) {
        Gson gson = new Gson();

        Player player = gson.fromJson(request.body(), Player.class);

        if(player == null) {
            response.status(400);
            return null;
        }

        String gameid = request.params(":gameid");
        String placeid = request.params(":placeid");

		Broker broker = getBroker(gameid);
		Estate estate = broker.getEstate(placeid);

        if(estate.isOwned()) {
            response.status(409);
            return null;
        }

        try {
            HttpResponse<JsonNode> bankResponse = Unirest
                    .post(Options.getSetting("bankUri") + "/transfer/from/{from}/{amount}")
                    .routeParam("from", player.getId())
                    .routeParam("amount", estate.getValue().toString())
                    .asJson();

            if(bankResponse.getStatus() == 201) {
                broker.addPlayer(player);
                estate.setOwner(player.getId());

                Event event = new Event("ownership-changed", "Ownership Changed", "Player bought place", estate.getPlace(), player);

                List<Event> events = new ArrayList<>();
                events.add(event);

                return events;
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        response.status(500);
        return null;
	}

	public Player getOwner(String gameid, String placeid) {
		Broker broker = getBroker(gameid);
        return broker.getPlayer(broker.getEstate(placeid).getOwner());
	}

	public List<Event> credit(String gameid, String placeid) {
		Broker broker = getBroker(gameid);

        broker.getCredits().add(placeid);

        Player player = broker.getPlayer(broker.getEstate(placeid).getOwner());

        Event event = new Event("place-credit", "Place Credit", "Player credited a place", placeid, player);

        List<Event> events = new ArrayList<>();
        events.add(event);

        return events;
	}

    public EstatesList getEstates(String gameid) {
        return new EstatesList(this.getBroker(gameid).getEstates().values());
    }

    public Estate addEstate(String gameid, String placeid, Estate estate) {
        Broker broker = this.getBroker(gameid);
        if(!broker.hasEstate(placeid)) {
            return broker.addEstate(placeid, estate);
        }

        return broker.getEstate(placeid);
    }

    public Estate getEstate(String gameid, String placeid) {
        return getBroker(gameid).getEstate(placeid);
    }

    public List<Event> changeOwner(String gameid, String placeid, Player player) {
        Broker broker = getBroker(gameid);
        Estate estate = broker.getEstate(placeid);

        estate.setOwner(player.getId());
        broker.addPlayer(player);

        Event event = new Event("ownership-changed", "Ownership Changed", "Player owns place", estate.getPlace(), player);

        List<Event> events = new ArrayList<>();
        events.add(event);

        return events;
    }

    public List<Event> deleteCredit(String gameid, String placeid) {
        Broker broker = getBroker(gameid);

        List<String> places = broker.getCredits();

        for (Iterator<String> iterator = places.listIterator(); iterator.hasNext();) {
            String place = iterator.next();
            if(place.equals(placeid)) {
                iterator.remove();
            }
        }

        Player player = broker.getPlayer(broker.getEstate(placeid).getOwner());

        Event event = new Event("place-credit-delete", "Place Credit Delete", "Player deleted credit of a place", placeid, player);

        List<Event> events = new ArrayList<>();
        events.add(event);

        return events;
    }

    public void register() {
        Gson gson = new Gson();

        try {
            HttpResponse<JsonNode> response = Unirest
                    .post(serviceRegistrationUri)
                    .header("accept", "application/json")
                    .header("content-type", "application/json")
                    .body(gson.toJson(service)).asJson();

            System.out.println("Status: " + response.getStatus() + " Body:"
                    + response.getBody().toString());
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}
