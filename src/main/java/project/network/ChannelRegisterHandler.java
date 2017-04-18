package project.network;

import io.netty.channel.*;

import java.net.SocketAddress;

/**
 * Created by mio on 2017/4/18.
 */
public class ChannelRegisterHandler extends ChannelDuplexHandler {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channels.putServerSideChannel(ctx.channel().remoteAddress(), ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channels.removeServerSideChannel(ctx.channel().remoteAddress());
        super.channelInactive(ctx);
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        promise.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess())
                    Channels.putClientSideChannel(remoteAddress, ctx.channel());
            }
        });
        super.connect(ctx, remoteAddress, localAddress, promise);
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        promise.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess())
                    Channels.removeClientSideChannel(ctx.channel().remoteAddress());
            }
        });
        super.disconnect(ctx, promise);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        promise.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess())
                    Channels.removeClientSideChannel(ctx.channel().remoteAddress());
            }
        });
        super.close(ctx, promise);
    }

}
