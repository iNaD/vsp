package Server;

import com.google.gson.Gson;
import static spark.Spark.*;

class Router {

    private Gson gson = new Gson();

    public Router() {
        get("/", (request, response) -> {
            return "Hello World!";
        });
    }

    public static void main(String[] args) {
        new Router();
    }

}
