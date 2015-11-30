import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Srv {
    public static void main(String args[]) {
        try {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            Naming.rebind("Dice", new Dice());
        }
        catch (MalformedURLException ex) {}
        catch (RemoteException ex) {}
    }
}
