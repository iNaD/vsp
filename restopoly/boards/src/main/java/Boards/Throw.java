package Boards;

public class Throw {

    private Roll roll1;
    private Roll roll2;

    public Throw(Roll roll1, Roll roll2) {
        this.roll1 = roll1;
        this.roll2 = roll2;
    }

    public Integer sum() {
        return roll1.getNumber() + roll2.getNumber();
    }
}
