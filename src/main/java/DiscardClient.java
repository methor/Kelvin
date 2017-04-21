import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * Created by Mio on 2017/4/11.
 */
public class DiscardClient {

    public static void main(String[] args) throws Exception
    {

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try
        {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception
                {
                    ch.pipeline()
                            .addLast(new LoggingHandler(LogLevel.INFO))
                            //.addLast(new DiscardClientHandler())
                            .addLast("a", new ChannelDuplexHandler() {
                                @Override
                                public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception
                                {
                                    promise.addListener(new ChannelFutureListener() {
                                        @Override
                                        public void operationComplete(ChannelFuture future) throws Exception
                                        {
                                            System.out.println(ctx.channel().remoteAddress());
                                        }
                                    });
                                    super.close(ctx, promise);
                                }



                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
                                {
                                    ctx.close();
                                }
                            })
                    ;

                }
            });

            // Start the client.
            ChannelFuture f = b.connect(InetAddress.getLocalHost(), 10001).sync(); // (5)

            Channel cl = f.channel();

            Scanner in = new Scanner(System.in);
            while (in.hasNextLine())
            {
                String line = in.nextLine();
                if (line.equals("quit"))
                {
                    f.channel().pipeline().remove("a");
                    break;
                }
                byte[] bytes = line.getBytes();
                ChannelFuture cf = f.channel().writeAndFlush(cl.alloc().buffer(bytes.length).writeBytes(bytes));
                cf.sync();
                if (!cf.isSuccess())
                    System.out.println("Send failed: " + cf.cause());
//                byte[] bytes = line.getBytes(Charset.forName("utf-8"));
//
//                f.channel().writeAndFlush(f.channel().alloc().buffer(bytes.length).setBytes(0, bytes)).sync();

            }

            // Wait until the connection is closed.
            //f.channel().closeFuture().sync();
        } finally
        {
            workerGroup.shutdownGracefully();
        }
    }
}
