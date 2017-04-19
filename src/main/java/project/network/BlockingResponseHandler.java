package project.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

/**
 * Created by mio on 2017/4/16.
 */
public class BlockingResponseHandler extends ChannelInboundHandlerAdapter {

    private final MessageBlockingQueue messageBlockingQueue;
    private static final AttributeKey<MessageBlockingQueue> QUEUE =
            AttributeKey.valueOf("blocking queue");
    public static final String NAME = "BlockingResponseHandler";

    public BlockingResponseHandler()
    {
        this.messageBlockingQueue = new MessageBlockingQueue();
    }
    public BlockingResponseHandler(int capacity)
    {
        this.messageBlockingQueue = new MessageBlockingQueue(capacity);
    }

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
