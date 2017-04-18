import project.network.NetworkLifeCycle;
import project.GlobalConfiguration;

/**
 * Created by mio on 2017/4/18.
 */
public class LifeCycle {

    public static void main(String[] args) {
        try {
            GlobalConfiguration.readConfig(args[0]);
        } catch (Exception e) {
            System.err.println("Exception encountered reading configuration file: " + e.getMessage());
            return;
        }
        NetworkLifeCycle.bootstrap();

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            exit();
        }

    }

    public static void exit() {
        NetworkLifeCycle.exit();
    }


}
