import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
    public static void main(String args[]) {
        try {
            DiceRMI dice = (DiceRMI)Naming.lookup("//127.0.0.1/Dice");
            System.out.println(dice.roll().getNumber());
        }
        catch (RemoteException | NotBoundException | MalformedURLException ex) {

        }
    }
}
