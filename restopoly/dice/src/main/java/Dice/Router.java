package Dice;

import com.google.gson.Gson;
import static spark.Spark.*;

class Router {

    private Gson gson = new Gson();

    public Router() {
        get("/dice", (request, response) -> {
            Dice dice = new Dice();
            dice.roll();
            response.type("application/json");
            return dice;
        }, gson::toJson);
    }

    public static void main(String[] args) {
        new Router();
    }

}
