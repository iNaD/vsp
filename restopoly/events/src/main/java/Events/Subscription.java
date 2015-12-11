package Events;

import com.google.gson.Gson;

public class Subscription {

    protected String gameid;
    protected String uri;
    protected String callbackuri;
    protected Event event;

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
}
