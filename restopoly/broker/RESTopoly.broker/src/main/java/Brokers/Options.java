package Brokers;

import java.util.HashMap;

public class Options {
    private static HashMap<String, String> settings = new HashMap<String, String>(){{
        put("bankUri", "http://localhost:4571/banks");
        put("eventUri", "http://localhost:4570/events");
    }};
public static String getSetting(String key) {
return settings.get(key);
}

}
