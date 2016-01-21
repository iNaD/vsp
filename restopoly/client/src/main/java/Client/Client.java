package Client;

import Client.Services.SelectorMenu;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.RequestBodyEntity;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static Client.Tools.readCli;

public class Client {

    protected static PlayerService playerService;

    protected static Credentials credentials;

    protected static Game game = null;

    protected static Gson gson = new Gson();

    protected static Boolean yellowPage;

    public static void main(String[] args) throws UnirestException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        /**
         * Make Unirest ignore certificates
         */
        SSLContext sslcontext = SSLContexts.custom()
                .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                .build();

        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();

        Unirest.setHttpClient(httpclient);

        playerService = new PlayerService();

        System.out.println("Welcome to RESTopoly!");

        Options.menu();

        yellowPage = useYellowPageService();

        credentials = new Credentials("", "");

        if(yellowPage) {
            System.out.println("First we need your HAW Login Credentials:");

            readCredentials();
        }

        System.out.println("To start or join a game we need your player id.");

        readPlayerId();

        System.out.println("That was easy, wasn't it? Now do you want to start a new game or join an existing one?");

        gameMenu();

        if(game != null && playerService.getPlayer() != null) {
            System.out.println("Registering you (" + playerService.getPlayer().getId() + ") with the game " + game.getGameid() + ".");
            Player player = gson.fromJson(addPlayerToGame(), Player.class);

            playerService.setPlayer(player);

            playerService.startRouter();

            sendReady();

            System.out.println("You (" + playerService.getPlayer().getId() + ") are ready to play!");

            waitForAction();

            playerService.stopRouter();
        } else {
            System.out.println("Hmm... seems we are missing either the game or the player id. Please try again :(");
        }
    }

    private static Boolean useYellowPageService() {
        String decision = readCli("Use yellow page service? (y/n)[n]:");

        if(decision.length() == 0 || decision.equals("n")) {
            return false;
        }

        return true;
    }

    private static void readCredentials() {
        String username = readCli("Username:");

        if(username.equals("")) {
            System.out.println("Please provide your logindata!");
            readCredentials();
            return;
        }

        String password = readCli("Password:", true);

        if(password.equals("")) {
            System.out.println("Please provide your logindata!");
            readCredentials();
            return;
        }

        credentials = new Credentials(username, password);

        return;
    }

    private static void waitForAction() {
        String actions =
            "You can do one of the following options:\n" +
            "t Turn\n" +
            "q Quit";

        String input = readCli(actions);
        Boolean quit = false;

        switch (input) {
            case "t":
                acquireTurn();
                break;
            case "q":
                quit = true;
                break;
            default:
        }

        if(quit == false) {
            waitForAction();
        }
    }

    private static void acquireTurn() {
        RequestBodyEntity request = Unirest.put(game.get_players() + "/turn")
                .header("accept", "application/json")
                .basicAuth(credentials.username, credentials.password)
                .queryString("player", playerService.getPlayer().getId())
                .body(gson.toJson(playerService.getPlayer()));

        HttpResponse<String> response = null;
        try {
            response = request.asString();
            System.out.println(response.getBody());
        } catch (UnirestException e) {
            e.printStackTrace();
            System.out.println("Failed to acquire Turn.");
        }

        if(response.getStatus() == 200 || response.getStatus() == 201) {
            turnMenu();
        } else {
            System.out.println("Failed to acquire Turn.");
        }
    }

    private static void turnMenu() {
        String actions =
            "1. Roll\n" +
            "2. End Turn";
        System.out.println("It's our turn! Select an action:");
        System.out.println(actions);

        String selection = readCli("Action:");

        if(selection.equals("1")) {
            doRoll();
        } else {
            endTurn();
        }
    }

    private static void endTurn() {
        HttpRequestWithBody request = Unirest.delete(game.get_players() + "/turn")
                .header("accept", "application/json")
                .basicAuth(credentials.username, credentials.password);

        try {
            System.out.println(request.asString().getBody());
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    private static void doRoll() {
        System.out.println(game.getComponents().board);
        endTurn();
    }


    public static String createGame(Game game) throws UnirestException {
        RequestBodyEntity request = Unirest.post(Options.getSetting("gamesUri"))
                .header("accept", "application/json")
                .basicAuth(credentials.username, credentials.password)
                .body(gson.toJson(game));

        HttpResponse<String> response = request.asString();

        System.out.println(response.getBody());

        return response.getBody();
    }

    public static String addPlayerToGame() throws UnirestException {
        HttpResponse<String> response = Unirest.post(game.get_players())
                .header("accept", "application/json")
                .basicAuth(credentials.username, credentials.password)
                .body(gson.toJson(playerService.getPlayer()))
                .asString();

        return response.getBody();
    }

    public static void readPlayerId() {
        String input = readCli("Please enter your player id:");

        if(input.length() > 0) {
            playerService.setPlayer(new Player(input));
        } else {
            System.out.println("Invalid player id: " + input);
            readPlayerId();
        }
    }

    public static void sendReady() {
        try {
            HttpResponse<String> response = Unirest.put(playerService.getPlayer().get_ready())
                    .header("accept", "application/json")
                    .basicAuth(credentials.username, credentials.password)
                    .asString();

            if(response.getStatus() == 200) {
                playerService.getPlayer().setReady(true);
            } else {
                playerService.getPlayer().setReady(false);
            }
        } catch (UnirestException e) {
            e.printStackTrace();
            playerService.getPlayer().setReady(false);
        }
    }

    public static void gameMenu() throws UnirestException {
        String decision = readCli("Start a new Game (y/n)[y]:");

        if(decision.length() == 0 || decision.equals("y")) {
            if(yellowPage) {
                SelectorMenu.menu(credentials);
            }

            System.out.println("Creating a new game...");
            Game tmpGame = new Game();
            Components components = new Components();

            components.game = Options.getSetting("gamesUri");
            components.bank = Options.getSetting("banksUri");
            components.board = Options.getSetting("boardsUri");
            components.broker = Options.getSetting("brokersUri");
            components.decks = Options.getSetting("decksUri");
            components.dice = Options.getSetting("diceUri");
            components.events = Options.getSetting("eventsUri");

            tmpGame.setComponents(components);

            System.out.println("Using components: " + components);

            game = gson.fromJson(createGame(tmpGame), Game.class);

            System.out.println("New game created with id " + game.getGameid() + "!");
        } else {
            if(yellowPage) {
                List<String> types = new ArrayList<>();
                types.add("games");

                SelectorMenu.menu(credentials, types);
            }

            joinGame();
        }
    }

    private static void joinGame() {
        GetRequest request = Unirest.get(Options.getSetting("gamesUri"))
                .header("accept", "application/json")
                .basicAuth(credentials.username, credentials.password);

        try {
            System.out.println(request.asString().getBody());

            HttpResponse<JsonNode> response = request.asJson();

            JSONArray games = response.getBody().getObject().getJSONArray("games");

            List<String> uriList = new ArrayList<>();

            Integer i = 0;
            for (Integer length = games.length(); i < length; i++) {
                String uri = games.getString(i);

                uriList.add(uri);
                System.out.println(i + ". " + uri);
            }

            String input = readCli("Ok, please enter the index of the game you want to join:");
            String gameIndex = "";

            if(input.length() > 0) {
                gameIndex = input;

                String uri = uriList.get(Integer.parseInt(gameIndex));

                if(uri != null) {
                    HttpResponse<String> gameResponse = Unirest.get(uri)
                            .basicAuth(credentials.username, credentials.password)
                            .asString();

                    game = gson.fromJson(gameResponse.getBody(), Game.class);
                }
            } else {
                System.out.println("Invalid index: " + input);
                joinGame();
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

}
