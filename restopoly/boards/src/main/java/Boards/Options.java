package Boards;

import java.util.HashMap;

public class Options {
    private static HashMap<String, String> settings = new HashMap<String, String>(){{
        put("brokerUri", "http://localhost:4569/brokers");
    }};
    public static String getSetting(String key) {
    return settings.get(key);
}
}
