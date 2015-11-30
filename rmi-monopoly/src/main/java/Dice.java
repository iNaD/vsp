import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ThreadLocalRandom;

public class Dice extends UnicastRemoteObject implements DiceRMI {

    public Dice() throws RemoteException {}

    public Roll roll() throws RemoteException {
        return new Roll(randomNumber());
    }

    /**
     * Generates a random number between 1 and 6.
     *
     * @return int
     */
    private int randomNumber() {
        return ThreadLocalRandom.current().nextInt(1, 7);
    }

}
