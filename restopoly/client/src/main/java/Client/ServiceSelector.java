package Client;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONArray;

public class ServiceSelector {

    public static void menu() {
        JSONArray gamesServices = getServicesOfType("games");

        Integer i = 0;
        for (Integer length = gamesServices.length(); i < length; i++) {
            System.out.format(gamesServices.getString(i));
        }
    }

    private static JSONArray getServicesOfType(String type) {
        JSONArray services = null;

        try {
            HttpResponse<JsonNode> response = Unirest.get(Options.getSetting("servicesUri") + "/of/type/{type}")
                    .routeParam("type", "games")
                    .asJson();

            services = response.getBody().getObject().getJSONArray("services");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return services;
    }

}
