import project.network.NetworkLifeCycle;
import project.GlobalConfiguration;

import java.util.Scanner;

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
            //Thread.currentThread().join();
            Scanner in = new Scanner(System.in);
            in.next();
        } finally {
            exit();
        }

    }

    public static void exit()
    {
        NetworkLifeCycle.exit();
    }


}
