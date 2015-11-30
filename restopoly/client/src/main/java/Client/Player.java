package Client;

public class Player {

    protected String id;
    protected String name;
    protected String uri;
    protected Integer position;
    protected boolean ready = false;

    public Player(String id) {
        this.id = id;
    }

    public Player(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Player(String id, String name, String uri) {
        this.id = id;
        this.name = name;
        this.uri = uri;
    }

    public Player(String id, String name, String uri, boolean ready) {
        this.id = id;
        this.name = name;
        this.uri = uri;
        this.ready = ready;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

}
