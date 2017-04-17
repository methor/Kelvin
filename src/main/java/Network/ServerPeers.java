package Network;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created by mio on 2017/4/5.
 */
public class ServerPeers {
    private static ArrayList<SeedServer> seedServerPeers = new ArrayList<>();

    public static boolean addServer(String name, InetAddress address) {
        return seedServerPeers.add(new SeedServer(name, address));
    }

    public static ArrayList<SeedServer> getSeedServerPeers() {
        return new ArrayList<>(seedServerPeers);
    }


}
