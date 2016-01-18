package Brokers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Broker {

    private Game game;

    private String uri;

    private Map<String, Estate> estates = new HashMap<>();

    private Map<String, Player> players = new HashMap<>();

    private List<String> credits = new ArrayList<>();

    public List<String> getCredits() {
        return credits;
    }

    public void setCredits(List<String> credits) {
        this.credits = credits;
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String, Player> players) {
        this.players = players;
    }

    public Map<String, Estate> getEstates() {
        return estates;
    }

    public void setEstates(Map<String, Estate> estates) {
        this.estates = estates;
    }

    public Estate addEstate(String placeid, Estate estate) {
        this.estates.put(placeid, estate);

        return estate;
    }

    public boolean hasEstate(String placeid) {
        return this.estates.get(placeid) != null;
    }

    public Estate getEstate(String placeid) {
        return this.estates.get(placeid);
    }

    public Player getPlayer(String owner) {
        if(owner.isEmpty()) {
            return null;
        } else {
            return this.players.get(owner);
        }
    }

    public void addPlayer(Player player) {
        this.players.put(player.getId(), player);
    }

    public boolean placeHasCredit(String placeid) {
        boolean result = false;

        for (String place : credits) {
            if(place.equals(placeid)) {
                result = true;
            }
        }

        return result;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
