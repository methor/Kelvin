import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.AttributeKey;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by mio on 2017/4/16.
 */
public class BlockingResponseHandler extends ChannelInboundHandlerAdapter {

    private final MessageBlockingQueue messageBlockingQueue = new MessageBlockingQueue();
    private static final AttributeKey<MessageBlockingQueue> QUEUE =
            AttributeKey.valueOf("blocking queue");


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().attr(QUEUE).set(messageBlockingQueue);
        super.channelRegistered(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        messageBlockingQueue.put(msg);
        //super.channelRead(ctx, msg);
    }

}
