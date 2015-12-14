package Events;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Subscription {

    protected String id;
    protected String gameid;
    protected String uri;
    protected String callbackuri;
    protected Event event;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getCallbackuri() {
        return callbackuri;
    }

    public void setCallbackuri(String callbackuri) {
        this.callbackuri = callbackuri;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public static Subscription fromJson(String json) {
        Gson gson = new Gson();

        return gson.fromJson(json, Subscription.class);
    }

    public void sendEvent(Event event) {
        Gson gson = new Gson();

        List<Event> events = new ArrayList<>();
        events.add(event);

        try {
            System.out.println("Sending events:");

            for (Event e : events) {
                System.out.println(e);
            }

            HttpResponse<JsonNode> response = Unirest
                    .post(callbackuri)
                    .header("accept", "application/json")
                    .header("content-type", "application/json")
                    .body(gson.toJson(events)).asJson();

            System.out.println("Status: " + response.getStatus() + " Body:" + response.getBody().toString());
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}
