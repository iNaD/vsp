package Games;

public class Player {

    private String id;
    private String name;
    private String uri;

    private Boolean ready = false;

    public Player(String id) {
        this.id = id;
    }

    public Player(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Player(String id, String name, String uri) {
        this.uri = uri;
        this.name = name;
        this.id = id;
    }

    public Boolean getReady() {
        return ready;
    }

    public void setReady(Boolean ready) {
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

}