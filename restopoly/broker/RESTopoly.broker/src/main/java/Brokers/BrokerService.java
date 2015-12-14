package Brokers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrokerService {

	private Map<String, Broker> brokers = new HashMap<>();

	public Broker getBroker(String gameID) {
		return brokers.get(gameID);
	}

	public Broker newBroker(String gameID) {
		Broker broker = new Broker();
		brokers.put(gameID, broker);
		return broker;
	}

	public Broker addPlaces(String gameid, String placeid) {
		Broker broker = getBroker(gameid);
		Place place = new Place(placeid);
		broker.addPlace(place);
		return broker;
	}

	public Place getPlace(String gameid, String placeid) {
		Broker broker = getBroker(gameid);
		Place place = broker.getPlace(placeid);

		return place;
	}

	public List<Place> getPlaces(String gameid) {
		Broker broker = getBroker(gameid);
		return broker.getPlaces();
	}
	// /boards bei /brokers Besuche durch Spieler anmeldet
	public Broker visit(String gameid, String placeid, String playerid) {
		Broker broker = getBroker(gameid);
		Place place=getPlace(gameid,placeid);
		Player player=new Player(playerid);
		broker.putPlayer(place, player);
		return broker;
	}
	
	public Broker owner(String gameid, String placeid) {
		Broker broker = getBroker(gameid);
		Place place=getPlace(gameid,placeid);
		broker.gekauft(place, true);
		return null;
	}
	// Spieler Grundstücke kaufen können durch
	public Boolean isOwner(String gameid, String placeid) {
		Broker broker = getBroker(gameid);
		Place place=getPlace(gameid,placeid);
		return broker.isOwner(place);
		 
	}
	//  /broker/{gameid}/places/{placeid}/hypothecarycredit 
	public Object credit(String gameid, String placeid) {
		Broker broker = getBroker(gameid);
		Place place=getPlace(gameid,placeid);
		
		return null;
	}
}
