import project.dht.Identifier;
import project.dht.RemoteIdentifierReply;
import project.dht.RemoteRequest;
import project.gossip.NodeInfo;
import project.network.NetworkLifeCycle;
import project.GlobalConfiguration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
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
            String s = request.serializeToJson();
            System.out.println(s);
            System.out.println(RemoteRequest.deserializeFromJson(s));

//            RemoteIdentifierReply reply = new RemoteIdentifierReply(new Identifier(
//                    InetAddress.getByName("127.0.0.1")), InetAddress.getByName("127.0.0.1"));
//            System.out.println(reply.toString());
//            s = reply.serializeToJson();
//            System.out.println(s);
//            System.out.println(RemoteIdentifierReply.deserializeFromJson(s));
//
//            Map<Identifier, Identifier> map = new HashMap<>();
//            map.put(new Identifier(InetAddress.getByName("127.0.0.1")), new Identifier(InetAddress.getByName("127.0.0.1")));
//            map.put(new Identifier(InetAddress.getByName("127.0.0.2")), new Identifier(InetAddress.getByName("127.0.0.3")));
//            NodeInfo nodeInfo = new NodeInfo(InetAddress.getByName("127.0.0.1"),
//                    new Identifier(InetAddress.getByName("127.0.0.1")),
//                    map);
//            s = nodeInfo.serializeToJson();
//            System.out.println(s);
//            System.out.println(NodeInfo.deserializeFromJson(s));

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
