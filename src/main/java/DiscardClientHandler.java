import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Mio on 2017/4/11.
 */
public class DiscardClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        ByteBuf bf = ((ByteBuf)msg);
        byte[] bytes = new byte[bf.readableBytes()];
        bf.getBytes(0, bytes);
        System.out.println(new String(bytes));
        //super.channelRead(ctx, msg);
    }
}
