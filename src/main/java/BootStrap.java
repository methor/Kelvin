import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.*;
import java.net.*;
import java.nio.ByteOrder;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
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
                                    .addLast(new StringDecoder(Charset.forName("utf-8")))
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

                            ;

                        }
                    });

            List<Channel> channels = new ArrayList<>();

            for (Server server : ServerPeers.getServerPeers()) {
                cb.connect(server.getAddress(), PortConstants.GOSSIP_PORT);
                channels.add(cb.connect(server.getAddress(), PortConstants.DATA_PORT).channel());
                cb.connect(server.getAddress(), PortConstants.REPLICA_PORT);
            }

            PrintWriter printWriter = new PrintWriter(new File("C:\\Users\\mio\\Desktop\\recv.txt"));
            printWriter.append("dasfsdfasfafasdfasf");
            printWriter.flush();
            printWriter.close();

            Scanner in = new Scanner(System.in);
            while (in.hasNextLine()) {
                String s = in.nextLine();
                File f = new File("C:\\Users\\mio\\Desktop", s);
                BufferedReader reader = new BufferedReader(new FileReader(f));
                while ((s = reader.readLine()) != null) {
                    byte[] bytes = s.getBytes(Charset.forName("utf-8"));
                    if (bytes.length > Short.MAX_VALUE) {
                        int mark = 0;
                        while (mark + Short.MAX_VALUE <= bytes.length) {
                            ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes, mark, Short.MAX_VALUE);
                            for (Channel channel : channels) {
                                channel.writeAndFlush(byteBuf);
                            }
                            mark += Short.MAX_VALUE;
                        }
                        if (mark < bytes.length - 1) {
                            ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes, mark, bytes.length - mark);
                            for (Channel channel : channels)
                                channel.writeAndFlush(byteBuf);
                        }
                    } else {
                        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
                        for (Channel channel : channels)
                            channel.writeAndFlush(byteBuf);
                    }
                }
                reader.close();

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            clientWorkerGroup.shutdownGracefully();

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
