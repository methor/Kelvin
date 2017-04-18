package project.network;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created by mio on 2017/4/5.
 */
public class ServerPeers {
    private static ArrayList<Server> serverPeers = new ArrayList<>();

    public static boolean addServer(String name, InetAddress address) {
        return serverPeers.add(new Server(name, address));
    }

    public static ArrayList<Server> getServerPeers() {
        return new ArrayList<>(serverPeers);
    }


}
