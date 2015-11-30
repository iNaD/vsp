package Games;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private String gameid;

    private Components components;

    private Boolean started = false;

    private Player turnMutex = null;

    private String uri;

    private List<Player> players = new ArrayList<>();

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public boolean addPlayer(Player player) {
        if(getPlayer(player.getId()) == null) {
            this.players.add(player);

            return true;
        } else {
            return false;
        }
    }

    public Player getPlayer(String playerid) {
        Player result = null;
        for(Player player : players) {
            if(player.getId().equals(playerid)) {
                result = player;
            }
        }

        return result;
    }

    public Boolean getStarted() {
        return started;
    }

    public void setStarted(Boolean started) {
        this.started = started;
    }

    public Player getTurnMutex() {
        return turnMutex;
    }

    public void releaseMutex() {
        turnMutex = null;
    }

    public boolean acquireMutex(Player player) {
        if(turnMutex == null && player != null) {
            turnMutex = player;
            return true;
        }

        return false;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Components getComponents() {
        return components;
    }

    public void setComponents(Components components) {
        this.components = components;
    }

}
