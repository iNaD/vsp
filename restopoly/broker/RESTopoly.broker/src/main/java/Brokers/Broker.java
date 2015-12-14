package Brokers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Broker {
	private List<Place> places = new ArrayList<Place>();
	private Map<Place,Player> players= new HashMap<Place, Player>();
	private Map<Place, Boolean> owner= new HashMap<Place, Boolean>();

	public List<Place> getPlaces() {
		return this.places;
	}

	public void setPlace(List<Place> p) {
		this.places = p;
	}

	public void addPlace(Place p) {
		places.add(p);
	}

	public Place getPlace(String name) {
		Place erg = null;
		for (Place place : places) {
			if (place.getName().equals(name)) {
				erg = place;
			}
		}
		return erg;
	}
	public Map<Place,Player> getPlayers(){
		return this.players;
	}
	public void putPlayer(Place place, Player player){
		players.put(place, player);
	}
	public Player getPlayer(Place p){
		return players.get(p);
	}
	public void gekauft(Place place, Boolean kauf){
		owner.put(place, kauf);
	}
	public Boolean isOwner(Place place){
		Boolean erg = false;
		Boolean kauf=owner.get(place);
		if(kauf!=null){
			erg=kauf;
		}
		return erg;

	}
	public Map<Place, Boolean> getOwner(){
		return owner;
	}
}
