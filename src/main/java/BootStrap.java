import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by mio on 2017/4/5.
 */
public class BootStrap {

    public static void main(String[] args) {

        File file = new File(args[0]);
        try {
            Scanner in = new Scanner(file);
            while (in.hasNextLine()) {
                String line = in.nextLine();
                Scanner lin = new Scanner(line);
                String name = lin.next();
                InetAddress address = InetAddress.getByName(lin.next());
                ServerPeers.addServer(name, address);
            }
            bootstrap();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private static void bootstrap() {
        try {
            ServerSocket gossipSocket = new ServerSocket(PortConstants.GOSSIP_PORT);
            ServerSocket dataSocket = new ServerSocket(PortConstants.DATA_PORT);
            ServerSocket replicaSocket = new ServerSocket(PortConstants.REPLICA_PORT);
            AcceptSelector acceptSelector = AcceptSelector.getInstance();
            Selector selector = acceptSelector.getSelector();
            gossipSocket.getChannel().configureBlocking(false).
                    register(selector, SelectionKey.OP_ACCEPT);
            dataSocket.getChannel().configureBlocking(false).
                    register(selector, SelectionKey.OP_ACCEPT);
            replicaSocket.getChannel().configureBlocking(false).
                    register(selector, SelectionKey.OP_ACCEPT);
            acceptSelector.startup();

            ArrayList<Server> servers = ServerPeers.getServerPeers();
            for (Server server : servers) {
                Socket socket = new Socket(InetAddress.getLocalHost(), 0);
                socket.connect(new InetSocketAddress(server.getAddress(), PortConstants.GOSSIP_PORT));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
