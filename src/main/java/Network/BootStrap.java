package Network;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.*;
import java.net.*;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
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
        EventLoopGroup clientWorkerGroup = new NioEventLoopGroup();
        EventExecutorGroup eventExecutorGroup = new DefaultEventExecutorGroup(4);

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new LengthFieldBasedFrameDecoder(
                                            ByteOrder.BIG_ENDIAN, Short.MAX_VALUE, 0, 2, 0, 2, true))
                                    .addLast(new BlockingResponseHandler())
                                    //.addLast(new LoggingHandler(LogLevel.INFO))
                                    ;
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            GenericFutureListener listener = new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess())
                        return;
                }
            };

            b.bind(PortConstants.GOSSIP_PORT).addListener(listener);
            b.bind(PortConstants.DATA_PORT).addListener(listener);
            b.bind(PortConstants.REPLICA_PORT).addListener(listener);

            Thread.sleep(5000);

            Bootstrap cb = new Bootstrap();
            cb.group(clientWorkerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    //.addLast(new StringEncoder(Charset.forName("utf-8")))
                                    .addLast(new LengthFieldPrepender(
                                            ByteOrder.BIG_ENDIAN, 2, 0, false))
                                    .addLast(new BlockingResponseHandler())

                            ;

                        }
                    });

            List<Channel> channels = new ArrayList<>();

            for (SeedServer seedServer : ServerPeers.getSeedServerPeers()) {
                cb.connect(seedServer.getAddress(), PortConstants.GOSSIP_PORT);
                channels.add(cb.connect(seedServer.getAddress(), PortConstants.DATA_PORT).channel());
                cb.connect(seedServer.getAddress(), PortConstants.REPLICA_PORT);
            }




        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            clientWorkerGroup.shutdownGracefully();

        }
//        try {
//            ServerSocket gossipSocket = new ServerSocket(Network.PortConstants.GOSSIP_PORT);
//            ServerSocket dataSocket = new ServerSocket(Network.PortConstants.DATA_PORT);
//            ServerSocket replicaSocket = new ServerSocket(Network.PortConstants.REPLICA_PORT);
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
//            ArrayList<Network.SeedServer> servers = Network.ServerPeers.getSeedServerPeers();
//            for (Network.SeedServer server : servers) {
//                Socket socket = new Socket(InetAddress.getLocalHost(), 0);
//                socket.connect(new InetSocketAddress(server.getAddress(), Network.PortConstants.GOSSIP_PORT));
//            }
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
