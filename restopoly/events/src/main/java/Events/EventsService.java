package Events;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.*;

public class EventsService {

    private static String serviceUri = "https://vs-docker.informatik.haw-hamburg.de/ports/11172/events";

    private static Service service = new Service("Events", "Provides Event actions", "events", serviceUri);

    private static String serviceRegistrationUri = "http://vs-docker.informatik.haw-hamburg.de:8053/services";

    private Map<String, EventsList> events = new HashMap<>();

    private List<Subscription> subscriptions = new ArrayList<>();


    public List<Subscription> getSubscriptions(String gameid) {
        List<Subscription> subscriptions = new ArrayList<>();

        for (Subscription subscription : this.subscriptions) {
            if(subscription.getGameid().equals(gameid)) {
                subscriptions.add(subscription);
            }
        }

        return subscriptions;
    }

    public static String getServiceUri() {
        return serviceUri;
    }

    public static void setServiceUri(String serviceUri) {
        EventsService.serviceUri = serviceUri;
    }

    public EventsList getEvents(String gameid) {
        return events.get(gameid);
    }

    public void setEvents(String gameid, EventsList events) {
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

    public Event addEvent(String gameid, Event event) {
        getEventslist(gameid).events.add(event);
        return event;
    }

    public void clearEvents(String gameid) {
        setEvents(gameid, new EventsList());
    }

    public EventsList getEventslist(String gameid) {
        EventsList events = this.events.get(gameid);

        if(events == null) {
            this.events.put(gameid, new EventsList());
            return this.events.get(gameid);
        } else {
            return events;
        }
    }

    public void addSubscription(Subscription subscription) {
        this.subscriptions.add(subscription);
    }

    public void removeSubscription(String subscriptionId) {
        for(Iterator<Subscription> subscriptionIterator = subscriptions.listIterator(); subscriptionIterator.hasNext();) {
            Subscription subscription = subscriptionIterator.next();

            if(subscription.getId().equals(subscriptionId)) {
                subscriptionIterator.remove();
            }
        }
    }

    public Event getEvent(String gameid, String eventid) {
        Event event = null;

        for(Event current : this.getEvents(gameid).events) {
            if(current.getId().equals(eventid)) {
                event = current;
            }
        }

        return event;
    }
}
