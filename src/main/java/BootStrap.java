import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;

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

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception
                        {
                            ch.pipeline().addLast();
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            GenericFutureListener listener = new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception
                {
                    if (future.isSuccess())
                        return;
                }
            };

            b.bind(PortConstants.GOSSIP_PORT).addListener(listener);
            b.bind(PortConstants.DATA_PORT).addListener(listener);
            b.bind(PortConstants.REPLICA_PORT).addListener(listener);
        } finally
        {

        }
//        try {
//            ServerSocket gossipSocket = new ServerSocket(PortConstants.GOSSIP_PORT);
//            ServerSocket dataSocket = new ServerSocket(PortConstants.DATA_PORT);
//            ServerSocket replicaSocket = new ServerSocket(PortConstants.REPLICA_PORT);
//            AcceptSelector acceptSelector = AcceptSelector.getInstance();
//            Selector selector = acceptSelector.getSelector();
//            gossipSocket.getChannel().configureBlocking(false).
//                    register(selector, SelectionKey.OP_ACCEPT);
//            dataSocket.getChannel().configureBlocking(false).
//                    register(selector, SelectionKey.OP_ACCEPT);
//            replicaSocket.getChannel().configureBlocking(false).
//                    register(selector, SelectionKey.OP_ACCEPT);
//            acceptSelector.startup();
//
//            ArrayList<Server> servers = ServerPeers.getServerPeers();
//            for (Server server : servers) {
//                Socket socket = new Socket(InetAddress.getLocalHost(), 0);
//                socket.connect(new InetSocketAddress(server.getAddress(), PortConstants.GOSSIP_PORT));
//            }
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
