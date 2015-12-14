package Brokers;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.put;
import static spark.Spark.post;
import spark.Spark;

import com.google.gson.Gson;

public class Router {
	private Gson gson = new Gson();
	private BrokerService service = new BrokerService();

	public Router() {
		Spark.port(4569);
		after((request, response) -> {
			response.type("application/json");
		});
		get("/brokers/:gameid", (request, response) -> {
			return service.getBroker(request.params(":gameid"));
		}, gson::toJson);
		put("/brokers/:gameid", (request, response) -> {
			return service.newBroker(request.params(":gameid"));
		}, gson::toJson);

		put("/brokers/:gameid/places/:placeid",
				(request, response) -> {
					return service.addPlaces(request.params(":gameid"),
							request.params(":placeid"));
				}, gson::toJson);
		
		get("/brokers/:gameid/places/:placeid",
				(request, response) -> {
					return service.getPlace(request.params(":gameid"),
							request.params(":placeid"));
				}, gson::toJson);
		get("/brokers/:gameid/places", (request, response) -> {
			return service.getPlaces(request.params(":gameid"));
		}, gson::toJson);
		
		
		post("/brokers/:gameid/places/:placeid/visit/:playerid" , (request, response) -> {
			return  service.visit(request.params(":gameid"),
					request.params(":placeid"),
					request.params(":playerid"));
			
		}, gson::toJson);
		
		post("/brokers/:gameid/places/:placeid/owner" , (request, response) -> {
			return  service.owner(request.params(":gameid"),
					request.params(":placeid"));
		}, gson::toJson);
		
		get("/brokers/:gameid/places/:placeid/owner",
				(request, response) -> {
					return service.isOwner(request.params(":gameid"),
							request.params(":placeid"));
				}, gson::toJson);
		
		//  /broker/{gameid}/places/{placeid}/hypothecarycredit 
		put("/brokers/:gameid/places/:placeid/hypothecarycredit ",
				(request, response) -> {
					return service.credit(request.params(":gameid"),
							request.params(":placeid"));
				}, gson::toJson);
		
		
	}

	
	

	public static void main(String[] args) {
		new Router();
	}
}
