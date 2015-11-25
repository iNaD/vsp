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

    private static String serviceRegistrationUri = "http://vs-docker:8053/services";

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

    public RollResponse roll(String gameid, String playerid, Throw theThrow) {
        Board board = getBoard(gameid);
        Player player = board.getPlayer(playerid);

        board.updatePosition(player, player.getPosition() + theThrow.sum());

        return new RollResponse(player, board);
    }

    public Board addPlayer(String gameid, String playerid) {
        Board board = getBoard(gameid);
        Player player = new Player(playerid);
        board.addPlayer(player);
        board.updatePosition(player, 0);

        return board;
    }

    public Player getPlayer(String gameid, String playerid) {
        Board board = getBoard(gameid);
        return board.getPlayer(playerid);
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
