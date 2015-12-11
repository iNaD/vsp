package Events;

public class Event {

    protected String id;
    protected String type;
    protected String name;
    protected String reason;
    protected String resource;
    protected Player player;
    protected String uri;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public String toString() {
        String s = "Event(id=%s, type=%s, name=%s, reason=%s, resource=%s, player=%s, uri=%s)";

        return String.format(s, id, type, name, reason, resource, player.getId(), uri);
    }

    public boolean matches(Event event) {

        if(name != null) {
            if(!event.getName().matches(name)) {
                return false;
            }
        }

        if(type != null) {
            if(!event.getType().matches(type)) {
                return false;
            }
        }

        if(reason != null) {
            if(!event.getReason().matches(reason)) {
                return false;
            }
        }

        if(resource != null) {
            if(!event.getResource().matches(resource)) {
                return false;
            }
        }

        if(uri != null) {
            if(!event.getUri().matches(uri)) {
                return false;
            }
        }

        return true;
    }
}
