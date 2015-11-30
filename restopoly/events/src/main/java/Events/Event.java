package Events;

public class Event {
    protected String type;
    protected String name;
    protected String reason;
    protected String resource;
    protected Player player;

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
        String s = "Event(type=%s, name=%s, reason=%s, resource=%s, player=%s)";

        return String.format(s, type, name, reason, resource, player.getId());
    }
}
