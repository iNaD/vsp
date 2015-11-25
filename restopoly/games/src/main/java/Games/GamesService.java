package Games;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GamesService {

    private List<Game> games = new ArrayList<>();

    public Game newGame() {
        Game game = new Game();
        game.setGameid(UUID.randomUUID().toString());
        return addGame(game);
    }

    public Game addGame(Game game) {
        games.add(game);
        return game;
    }

    public Game getGame(String gameid) {
        Game result = null;

        for(Game game : games) {
            if(game.getGameid().equals(gameid)) {
                result = game;
            }
        }

        return result;
    }

    public List<Game> getGames() {
        return games;
    }

    public Game addPlayer(String gameid, String playerid) {
        Game game = getGame(gameid);
        game.addPlayer(new Player(playerid));

        return game;
    }

    public Game releaseMutex(String gameid) {
        Game game = getGame(gameid);

        game.releaseMutex();

        return game;
    }

    public boolean acquireMutex(String gameid, String playerid) {
        Game game = getGame(gameid);
        Player player = game.getPlayer(playerid);

        return game.acquireMutex(player);
    }

    public Player getTurnMutex(String gameid) {
        return getGame(gameid).getTurnMutex();
    }

}
