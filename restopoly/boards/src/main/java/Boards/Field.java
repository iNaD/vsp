package Boards;

import java.util.ArrayList;
import java.util.List;

public class Field {
    private String place;

    private List<String> players = new ArrayList<String>();

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }
}
