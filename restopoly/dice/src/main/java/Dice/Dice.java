package Dice;

import java.util.concurrent.ThreadLocalRandom;

public class Dice {

    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Generates a random number between 1 and 6.
     *
     * @return int
     */
    public void roll() {
        this.number = ThreadLocalRandom.current().nextInt(1, 7);
    }
}
