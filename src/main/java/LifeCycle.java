import project.dht.Identifier;
import project.dht.RemoteRequest;
import project.network.NetworkLifeCycle;
import project.GlobalConfiguration;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
        boolean networkStart = false;
        if (networkStart)
            NetworkLifeCycle.bootstrap();

        try {
            RemoteRequest request = new RemoteRequest(RemoteRequest.Type.CLOSEST_PRECEDING_FINGER
            , new Identifier(InetAddress.getByName("127.0.0.1")));
            System.out.println(request.toString());
            String s = request.serializeToJson().get();
            System.out.println(s);
            System.out.println(RemoteRequest.deserializeFromJson(s).get());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        try {
            //Thread.currentThread().join();
            Scanner in = new Scanner(System.in);
            in.next();
        } finally {
            if (networkStart)
                exit();
        }

    }

    public static void exit() {
        NetworkLifeCycle.exit();
    }


}
