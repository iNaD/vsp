package Games;

import java.util.HashMap;

public class Options {
	  private static HashMap<String, String> settings = new HashMap<String, String>(){{
	        put("gamesUri", "http://localhost:4567/games");
	        put("boardUri", "http://localhost:4568/boards");
	    }};
	    public static String getSetting(String key) {
	        return settings.get(key);
	    }

}
