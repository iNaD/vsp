package Boards;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mashape.unirest.request.body.RequestBodyEntity;
import org.json.JSONObject;

public class BoardsService {

	private static Service service = new Service(
            "Boards Olga-Daniel",
			"Provides Board interaction",
            "boards",
			"http://localhost:4568/boards");

    private Gson gson = new Gson();

	private static String serviceRegistrationUri = "http://vs-docker.informatik.haw-hamburg.de:8053/services";

	private Map<String, Board> boards = new HashMap<>();

	public Map<String, Board> getBoards() {
		return boards;
	}

	public Board addBoard(String gameid, Board board) {
		boards.put(gameid, board);

		try {
			String brokerUri = newBroker(board);
            board.getGame().getComponents().broker = brokerUri;

			for (Field field : board.getFields()) {
				String placeid = field.getPlace().getName();
				grundstRegistr(board, placeid);
			}
        }
		catch(UnirestException e){
			e.printStackTrace();
		}

		return board;
	}

	public Board getBoard(String gameid) {
		return boards.get(gameid);
	}

	public Board newBoard(Game game) {
        Board board = new Board();

        board.setGame(game);

        board.setUri(service.getUri() + "/" + board.getGame().getGameid());

        board.setGame(game);

		return addBoard(game.getGameid(), board);
	}

	public void deleteBoard(String gameid) {
		boards.remove(gameid);
	}

    public void move(String gameid, String playerid, Integer steps) {
        Board board = getBoard(gameid);
        Player player = board.getPlayer(playerid);

        Field newField = board.updatePosition(player, player.getPosition() + steps);

        try {
            visit(board, newField.getPlace().getName(), playerid);
        } catch(UnirestException e) {
            e.printStackTrace();
        }
    }

	public RollResponse roll(String gameid, String playerid, Throw theThrow) {
		Board board = getBoard(gameid);
		Player player = board.getPlayer(playerid);

		Field newField = board.updatePosition(player, player.getPosition() + theThrow.sum());

		try {
			visit(board, newField.getPlace().getName(), playerid);
		} catch(UnirestException e) {
			e.printStackTrace();
		}

		return new RollResponse(player, board);
	}

	public Player addPlayer(String gameid, Player player) {
		Board board = getBoard(gameid);
        player.setUri(service.getUri() + "/" + gameid + "/players/" + player.getId());

		board.addPlayer(player);

		Field newField = board.updatePosition(player, 0);

		try {
			visit(board, newField.getPlace().getName(), player.getId());
		} catch(UnirestException e) {
			e.printStackTrace();
		}

		return player;
	}

	public Player getPlayer(String gameid, String playerid) {
		Board board = getBoard(gameid);
		return board.getPlayer(playerid);
	}

	public void register() {
		try {
			HttpResponse<JsonNode> response = Unirest
					.post(serviceRegistrationUri)
					.header("accept", "application/json")
					.header("content-type", "application/json")
					.body(gson.toJson(service)).asJson();

			System.out.println("Status: " + response.getStatus() + " Body:"
					+ response.getBody().toString());
		} catch (UnirestException e) {
			e.printStackTrace();
		}
	}

	public void removePlayer(String gameid, String playerid) {
		getBoard(gameid).removePlayer(playerid);
	}

	public Place getPlace(String gameid, String placeName) {
		List<Place> places = getPlaces(gameid);
		Place result = null;

		for (Place place : places) {
			if (place.getName().equals(placeName)) {
				result = place;
			}
		}

		return result;
	}

	public List<Place> getPlaces(String gameid) {
		List<Field> fields = getBoard(gameid).getFields();
		List<Place> places = new ArrayList<>();

		for (Field field : fields) {
			places.add(field.getPlace());
		}

		return places;

	}

	// /boards einen Broker pro Spiel erstellt
	// put /brokers/{gameid}
	public String newBroker(Board board) throws UnirestException {
		HttpResponse<JsonNode> response = Unirest
				.put(board.getGame().getComponents().broker + "/{gameid}")
				.header("accept", "application/json")
                .routeParam("gameid", board.getGame().getGameid())
                .body(gson.toJson(board.getGame()))
				.asJson();

        String location = null;

        if(response.getStatus() == 200) {
            location = response.getHeaders().getFirst("Location");
        }

        System.out.println("broker uri: " + location);

        return location;
	}

	// /boards die verfügbaren Grundstücke registriert mit
	// put /brokers/{gameid}/places/{placeid}
	public JSONObject grundstRegistr(Board board, String placeid)
			throws UnirestException {
        Estate estate = new Estate();
        estate.setPlace(placeid);

        RequestBodyEntity request = Unirest
                .put(board.getGame().getComponents().broker + "/places/{placeid}")
                .header("accept", "application/json")
                .routeParam("placeid", placeid)
                .body(gson.toJson(estate));

        System.out.println(request.asString().getBody());

		return request.asJson().getBody().getObject();

	}

	// /boards bei /brokers Besuche durch Spieler anmeldet
	// post /brokers/{gameid}/places/{placeid}/visit/{playerid}
	public JSONObject visit(Board board, String placeid, String playerid)
			throws UnirestException {
		HttpResponse<JsonNode> response = Unirest
				.post(board.getGame().getComponents().broker + "/places/{placeid}/visit/{playerid}")
				.header("accept", "application/json")
				.routeParam("placeid", placeid)
                .routeParam("playerid", playerid)
				.asJson();
		return response.getBody().getObject();

	}

    public Field addPlace(String gameid, String placeName, Place place) {
        place.setName(placeName);
        Field field = new Field(place);
        field.setUri(service.getUri() + "/" + gameid + "/places/" + placeName);

        getBoard(gameid).getFields().add(field);

        return field;
    }
}
