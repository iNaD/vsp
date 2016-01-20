package Client;

public class Game {

    private String gameid;

    private String _players;

    private Components components;

    private String uri;

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public Components getComponents() {
        return components;
    }

    public void setComponents(Components components) {
        this.components = components;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String get_players() {
        return _players;
    }

    public void set_players(String _players) {
        this._players = _players;
    }
}
