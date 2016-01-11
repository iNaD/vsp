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

import org.json.JSONObject;

public class BoardsService {

	private static Service service = new Service("Boards",
			"Provides Board interaction", "boards",
			"https://vs-docker.informatik.haw-hamburg.de/ports/11171/boards");

	private static String serviceRegistrationUri = "http://vs-docker.informatik.haw-hamburg.de:8053/services";

	private Map<String, Board> boards = new HashMap<>();

	public Map<String, Board> getBoards() {
		return boards;
	}

	public Board addBoard(String gameid, Board board) {
		boards.put(gameid, board);
		try{
			newBroker(gameid);
			for (Field field : board.getFields()) {
				String placeid=field.getPlace().getName();
				grundstRegistr( gameid, placeid);
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

	public Board newBoard(String gameid) {
		return addBoard(gameid, new Board());
	}

	public void deleteBoard(String gameid) {
		boards.remove(gameid);
	}

    public void move(String gameid, String playerid, Integer steps) {
        Board board = getBoard(gameid);
        Player player = board.getPlayer(playerid);

        Field newField = board.updatePosition(player, player.getPosition() + steps);

        try {
            visit(gameid, newField.getPlace().getName(), playerid);
        } catch(UnirestException e) {
            e.printStackTrace();
        }
    }

	public RollResponse roll(String gameid, String playerid, Throw theThrow) {
		Board board = getBoard(gameid);
		Player player = board.getPlayer(playerid);

		Field newField = board.updatePosition(player, player.getPosition() + theThrow.sum());

		try {
			visit(gameid, newField.getPlace().getName(), playerid);
		} catch(UnirestException e) {
			e.printStackTrace();
		}

		return new RollResponse(player, board);
	}

	public Board addPlayer(String gameid, Player player) {
		Board board = getBoard(gameid);
        player.setUri(service.getUri() + "/" + gameid + "/players/" + player.getId());

		board.addPlayer(player);

		Field newField = board.updatePosition(player, 0);

		try {
			visit(gameid, newField.getPlace().getName(), player.getId());
		} catch(UnirestException e) {
			e.printStackTrace();
		}
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

	public JSONObject newBroker(String gameid) throws UnirestException {
		HttpResponse<JsonNode> response = Unirest
				.put(Options.getSetting("brokerUri") + "/{gameid}")
				.header("accept", "application/json")
				.routeParam("gameid", gameid)
				.asJson();

		return response.getBody().getObject();
	}

	// /boards die verfügbaren Grundstücke registriert mit
	// put /brokers/{gameid}/places/{placeid}
	public JSONObject grundstRegistr(String gameid, String placeid)
			throws UnirestException {
        Estate estate = new Estate();
        estate.setPlace(placeid);

        Gson gson = new Gson();

		HttpResponse<JsonNode> response = Unirest
				.put(Options.getSetting("brokerUri")
						+ "/{gameid}/places/{placeid}")
				.header("accept", "application/json")
				.routeParam("gameid", gameid).routeParam("placeid", placeid)
                .body(gson.toJson(estate))
				.asJson();
		return response.getBody().getObject();

	}

	// /boards bei /brokers Besuche durch Spieler anmeldet
	// post /brokers/{gameid}/places/{placeid}/visit/{playerid}
	public JSONObject visit(String gameid, String placeid, String playerid)
			throws UnirestException {
		HttpResponse<JsonNode> response = Unirest
				.post(Options.getSetting("brokerUri")
						+ "/{gameid}/places/{placeid}/visit/{playerid}")
				.header("accept", "application/json")
				.routeParam("gameid", gameid).routeParam("placeid", placeid)
                .routeParam("playerid", playerid)
				.asJson();
		return response.getBody().getObject();

	}

    public void addPlace(String gameid, String placeName, Place place) {
        place.setName(placeName);
        Field field = new Field(place);
        getBoard(gameid).getFields().add(field);
    }
}
