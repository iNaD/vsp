package Boards;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.HashMap;
import java.util.Map;

public class BoardsService {

    private static Service service = new Service("boards", "Boards Service", "boards", "http://localhost:4567/boards");

    private static String serviceRegistrationUri = "https://vs-docker.informatik.haw-hamburg.de/ports/8053/services";

    private Map<String, Board> boards = new HashMap<>();

    public Map<String, Board> getBoards() {
        return boards;
    }

    public Board addBoard(String gameid, Board board) {
        boards.put(gameid, board);

        return board;
    }

    public Board getBoard(String gameid) {
        return boards.get(gameid);
    }

    public Board newBoard(String gameid) {
        return addBoard(gameid, new Board());
    }

    public Map<String, Roll> roll(String gameid, String playerid) {
        Map<String, Roll> rolls = new HashMap<>();

        rolls.put("roll1", new Roll(1));
        rolls.put("roll2", new Roll(2));

        return rolls;
    }

    public Board addPlayer(String gameid, String playerid) {
        Board board = getBoard(gameid);
        board.addPlayer(new Player(playerid));

        return board;
    }

    public void register() {
        Gson gson = new Gson();

        try {
            HttpResponse<JsonNode> response = Unirest
                    .post(serviceRegistrationUri)
                    .body(gson.toJson(service))
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}
