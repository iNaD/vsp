package Games.ServiceTests;

import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import org.junit.Before;

abstract class JsonServiceTest {

    protected static String baseUri = "http://localhost:4567";

    protected Gson gson = new Gson();

    @Before
    public void setup() {
        Unirest.setDefaultHeader("Content-Type", "application/json");
        Unirest.setDefaultHeader("Accept", "application/json");
    }

}
