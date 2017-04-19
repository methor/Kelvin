package project.network;

import io.netty.channel.*;

import java.net.SocketAddress;

/**
 * Created by mio on 2017/4/18.
 */
public class ChannelRegisterHandler extends ChannelDuplexHandler {

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channels.removeChannel(ctx.channel().remoteAddress());
        ctx.pipeline().remove(BlockingResponseHandler.NAME);
        super.channelInactive(ctx);
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception
    {
        promise.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception
            {
                if (future.isSuccess())
                    Channels.putChannel(remoteAddress, ctx.channel());
            }
        });
        super.connect(ctx, remoteAddress, localAddress, promise);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        ctx.close();
    }
}
