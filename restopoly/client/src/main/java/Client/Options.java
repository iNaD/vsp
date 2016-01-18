package Client;

import java.util.HashMap;

import static Client.Tools.readCli;

public class Options {

    private static HashMap<String, String> settings = new HashMap<String, String>(){{
        put("servicesUri", "https://vs-docker.informatik.haw-hamburg.de/ports/8053/services");
        put("gamesUri", "http://localhost:4567/games");
        put("boardUri", "http://localhost:4568/boards");
        put("brokerUri", "http://localhost:4569/brokers");
    }};

    public static String getSetting(String key) {
        return settings.get(key);
    }

    public static void setSetting(String key, String value) {
        settings.put(key, value);
    }

    public static void menu() {
        String decision = readCli("Modify the options? (y/n)[n]:");

        if(decision.equals("y")) {
            table();
        }
    }

    public static void table() {
        Integer firstCell = 20;
        Integer secondCell = 102;
        String leftAlignFormat = "| %-" + (firstCell - 2) + "s | %-" + (secondCell - 2) + "s |%n";
        String leftAlignFormatHead = "| %-" + (firstCell - 2) + "s | %-" + (secondCell - 2) + "s |%n";
        String tableBorder = "+";

        for (int i = 0; i < firstCell; i++) {
            tableBorder += "-";
        }

        tableBorder += "+";

        for (int i = 0; i < secondCell; i++) {
            tableBorder += "-";
        }

        tableBorder += "+%n";

        System.out.format(tableBorder);
        System.out.format(leftAlignFormatHead, "Option Name", "Value");
        System.out.format(tableBorder);
        for(HashMap.Entry<String, String> option : settings.entrySet()) {
            System.out.format(leftAlignFormat, option.getKey(), option.getValue());
        }
        System.out.format(tableBorder);

        selectOption();
    }

    public static void selectOption() {
        String decision = readCli("Which option do you want to change? (Enter a name or 'q' to quit options menu) [q]:");

        if(decision.length() > 0 && !decision.equals("q")) {
            changeOption(decision);
        }
    }

    public static void changeOption(String option) {
        if(settings.containsKey(option)) {
            String newValue = readCli("Please enter the new value for this option or leave empty to use current value. []:");

            if(newValue.length() > 0) {
                setSetting(option, newValue);
            }

            table();
        } else {
            System.out.println("Unknown option " + option);
            selectOption();
        }
    }

}
