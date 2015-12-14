package Brokers;

import java.util.ArrayList;
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

	public Broker visit(String gameid, String placeid, String playerid) {
		Broker broker = getBroker(gameid);
		Estate estate = broker.getEstate(placeid);
		// TODO: Player visits estate
		return broker;
	}

	public List<Event> setOwner(String gameid, String placeid, Player player) {
		Broker broker = getBroker(gameid);
		Estate estate = broker.getEstate(placeid);

        if(estate.isOwned()) {
            return null;
        }

        estate.setOwner(player.getId());
        broker.addPlayer(player);

        Event event = new Event("ownership-changed", "Ownership Changed", "Player bought place", estate.getPlace(), player);

        List<Event> events = new ArrayList<>();
        events.add(event);

        return events;
	}

	public Player getOwner(String gameid, String placeid) {
		Broker broker = getBroker(gameid);
        return broker.getPlayer(broker.getEstate(placeid).getOwner());
	}

	public List<Event> credit(String gameid, String placeid) {
		Broker broker = getBroker(gameid);

        broker.getCredits().add(placeid);

        Player player = broker.getPlayer(broker.getEstate(placeid).getOwner());

        Event event = new Event("place-credit", "Place Credit", "Player credited a place", placeid, player);

        List<Event> events = new ArrayList<>();
        events.add(event);

        return events;
	}

    public EstatesList getEstates(String gameid) {
        return new EstatesList(this.getBroker(gameid).getEstates().values());
    }

    public Estate addEstate(String gameid, String placeid, Estate estate) {
        Broker broker = this.getBroker(gameid);
        if(!broker.hasEstate(placeid)) {
            return broker.addEstate(placeid, estate);
        }

        return broker.getEstate(placeid);
    }

    public Estate getEstate(String gameid, String placeid) {
        return getBroker(gameid).getEstate(placeid);
    }
}
