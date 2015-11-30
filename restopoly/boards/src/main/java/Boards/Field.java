package Boards;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Field {
    private Place place;

    private List<String> players = new ArrayList<>();

    public Field(Place place) {
        this.place = place;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public void addPlayer(String playerid) {
        players.add(playerid);
    }

    public void removePlayer(String playerid) {
        for(Iterator<String> iterator = players.listIterator(); iterator.hasNext();) {
            String player = iterator.next();
            if(player.equals(playerid)) {
                iterator.remove();
            }
        }
    }
}
