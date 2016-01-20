package Client.Services;

import static Client.Tools.readCli;

import Client.Credentials;
import Client.Options;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class SelectorMenu {

    private static Credentials credentials;
    private static Gson gson = new Gson();

    public static void menu(Credentials credentials, List<String> types) {
        SelectorMenu.credentials = credentials;

        for (String type : types) {
            selectServiceOfType(type);
        }
    }

    public static void menu(Credentials credentials) {
        List<String> types = new ArrayList<>();
        types.add("games");
        types.add("boards");
        types.add("brokers");
        types.add("events");
        types.add("banks");
        types.add("dice");
        types.add("decks");

        menu(credentials, types);
    }

    private static void selectServiceOfType(String type) {
        List<Service> services = getServicesOfType(type);

        System.out.println("Select one of the listed services of type " + type + ":");

        for (Service service : services) {
            System.out.println(services.indexOf(service) + ". " + service.name);
        }

        String index = readCli("Index of service to use:");

        Options.setSetting(type + "Uri", services.get(Integer.parseInt(index)).uri);
    }

    private static List<Service> getServicesOfType(String type) {
        List<Service> services = new ArrayList<>();

        try {
            GetRequest request = Unirest.get(Client.Options.getSetting("servicesUri") + "/services/of/type/{type}")
                    .routeParam("type", type)
                    .basicAuth(credentials.username, credentials.password);

            HttpResponse<JsonNode> response = request.asJson();

            JSONArray serviceUris = response.getBody().getObject().getJSONArray("services");

            Integer i = 0;
            for (Integer length = serviceUris.length(); i < length; i++) {
                String uri = serviceUris.getString(i);
                Service service = getService(uri);
                if(service != null) {
                    services.add(service);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return services;
    }

    private static Service getService(String uri) {
        Service service = null;
        try {
            GetRequest request = Unirest.get(Client.Options.getSetting("servicesUri") + uri)
                    .basicAuth(credentials.username, credentials.password);

            HttpResponse<String> response = request.asString();

            service = gson.fromJson(response.getBody(), Service.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return service;
    }

}
