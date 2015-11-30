package Boards;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

    private List<Field> fields = new ArrayList<>();

    private List<Player> players = new ArrayList<>();

    private Map<String, Integer> positions = new HashMap<>();

    private String uri;

    public Board() {
        fields.add(new Field(new Place("Los")));
        fields.add(new Field(new Place("Badstraße")));
        fields.add(new Field(new Place("Gemeinschaftsfeld")));
        fields.add(new Field(new Place("Turmstraße")));
        fields.add(new Field(new Place("Einkommensteuer")));
        fields.add(new Field(new Place("Südbahnhof")));
        fields.add(new Field(new Place("Chausseestraße")));
        fields.add(new Field(new Place("Ereignisfeld")));
        fields.add(new Field(new Place("Elisenstraße")));
        fields.add(new Field(new Place("Poststraße")));
        fields.add(new Field(new Place("Gefängnis / Besucher")));
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void addPlayer(Player player) {
        players.add(player);
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Map<String, Integer> getPositions() {
        return positions;
    }

    public void setPositions(Map<String, Integer> positions) {
        this.positions = positions;
    }

    public void updatePosition(Player player, Integer newPosition) {
        Integer oldPosition = player.getPosition();

        if(oldPosition != null) {
            fields.get(oldPosition).removePlayer(player.getId());
        }

        Integer position = newPosition % fields.size();
        player.setPosition(position);
        fields.get(position).addPlayer(player.getId());
        positions.put(player.getId(), position);
    }

}