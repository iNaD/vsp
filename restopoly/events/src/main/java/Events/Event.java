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

    public boolean matches(Event event) {

        if(name != null) {
            System.out.println("Matching if name \"" + event.getName() + "\" is like \"" + name + "\"");

            if(!event.getName().matches(name)) {
                return false;
            }

            System.out.println("Name matched!");
        }

        if(type != null) {
            System.out.println("Matching if type \"" + event.getType() + "\" is like \"" + type + "\"");

            if(!event.getType().matches(type)) {
                return false;
            }

            System.out.println("Type matched.");
        }

        if(reason != null) {
            System.out.println("Matching if reason \"" + event.getReason() + "\" is like \"" + reason + "\"");

            if(!event.getReason().matches(reason)) {
                return false;
            }

            System.out.println("Reason matched.");
        }

        if(resource != null) {
            System.out.println("Matching if resource \"" + event.getResource() + "\" is like \"" + resource + "\"");

            if(!event.getResource().matches(resource)) {
                return false;
            }

            System.out.println("Resource matched.");
        }

        if(uri != null) {
            System.out.println("Matching if uri \"" + event.getUri() + "\" is like \"" + uri + "\"");

            if(!event.getUri().matches(uri)) {
                return false;
            }

            System.out.println("URI Macthed");
        }

        System.out.println("Event matched!");

        return true;
    }

    @Override
    public String toString() {
        String s = "Event(id=%s, type=%s, name=%s, reason=%s, resource=%s, player=%s, uri=%s)";

        return String.format(s, id, type, name, reason, resource, player != null ? player.getId() : null, uri);
    }
}
