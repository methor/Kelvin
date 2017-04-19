package project.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.BlockingOperationException;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import project.GlobalConfiguration;

import java.net.*;
import java.nio.ByteOrder;

/**
 * Created by mio on 2017/4/5.
 */
public class NetworkLifeCycle {
    static EventLoopGroup bossGroup = new NioEventLoopGroup();
    static EventLoopGroup workerGroup = new NioEventLoopGroup();
    static EventLoopGroup clientWorkerGroup = new NioEventLoopGroup();
    static EventExecutorGroup eventExecutorGroup = new DefaultEventExecutorGroup(4);

    public static void bootstrap() {



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
                                    .addLast(new JsonObjectDecoder(true))
                                    .addLast(new LoggingHandler(LogLevel.INFO))
                                    .addLast(new ChannelRegisterHandler())
                                    .addLast(eventExecutorGroup, BlockingResponseHandler.NAME, new BlockingResponseHandler())
                                    ;
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);



            b.bind(GlobalConfiguration.getInstance().getGossipPort()).sync();
            b.bind(GlobalConfiguration.getInstance().getDataPort()).sync();
            b.bind(GlobalConfiguration.getInstance().getReplicaPort()).sync();




            io.netty.bootstrap.Bootstrap cb = new io.netty.bootstrap.Bootstrap();
            cb.group(clientWorkerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    //.addLast(new StringEncoder(Charset.forName("utf-8")))
                                    .addLast(new LengthFieldPrepender(
                                            ByteOrder.BIG_ENDIAN, 2, 0, false))
                                    .addLast(new JsonObjectDecoder(true))
                                    .addLast(new LoggingHandler(LogLevel.INFO))
                                    .addLast(new ChannelRegisterHandler())
                                    .addLast(eventExecutorGroup, BlockingResponseHandler.NAME, new BlockingResponseHandler())

                            ;

                        }
                    })


            ;



            for (InetAddress address : GlobalConfiguration.getInstance().getSeedsAddrs()) {
                cb.connect(address, GlobalConfiguration.getInstance().getGossipPort());
                cb.connect(address, GlobalConfiguration.getInstance().getDataPort());
                cb.connect(address, GlobalConfiguration.getInstance().getReplicaPort());
            }






        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void exit()
    {


            clientWorkerGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            eventExecutorGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();




    }
}
