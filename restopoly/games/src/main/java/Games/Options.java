package Games;

import java.util.HashMap;
import java.util.Map;

public class Options {

    private static Map<String, String> settings = new HashMap<String, String>(){{
        put("boardUri", "http://localhost:4568/boards");
    }};

    public static String getSetting(String key) {
    return settings.get(key);
    }
}
