package Events;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventsService {

    private static String serviceUri = "https://vs-docker.informatik.haw-hamburg.de/ports/11172/events";

    private static Service service = new Service("Events", "Provides Event actions", "events", serviceUri);

    private static String serviceRegistrationUri = "http://vs-docker.informatik.haw-hamburg.de:8053/services";

    private Map<String, List<Event>> events = new HashMap<>();

    public static String getServiceUri() {
        return serviceUri;
    }

    public static void setServiceUri(String serviceUri) {
        EventsService.serviceUri = serviceUri;
    }

    public List<Event> getEvents(String gameid) {
        return events.get(gameid);
    }

    public void setEvents(String gameid, List<Event> events) {
        this.events.put(gameid, events);
    }

    public void register() {
        Gson gson = new Gson();

        try {
            HttpResponse<JsonNode> response = Unirest
                    .post(serviceRegistrationUri)
                    .header("accept", "application/json")
                    .header("content-type", "application/json")
                    .body(gson.toJson(service)).asJson();

            System.out.println("Status: " + response.getStatus() + " Body:" + response.getBody().toString());
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

}
