package Games;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private String gameid;

    private List<String> players = new ArrayList<>();

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public void addPlayer(String player) {
        this.players.add(player);
    }
}
