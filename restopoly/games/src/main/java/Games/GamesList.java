package Games;

import java.util.ArrayList;
import java.util.List;

public class GamesList {
    public List<String> games = new ArrayList<>();

    public GamesList(List<Game> games) {
        for (Game game : games) {
            this.games.add(game.getUri());
        }
    }
}
