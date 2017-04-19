package project.network;

import com.google.common.base.Optional;
import io.netty.channel.Channel;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mio on 2017/4/18.
 */
public class Channels {
    
    private static final Map<SocketAddress, Channel> CHANNELS = new ConcurrentHashMap<>();

    
    public static Optional<Channel> getChannel(SocketAddress address) {
        return Optional.fromNullable(CHANNELS.get(address));
    }
    
    public static Optional<Channel> putChannel(SocketAddress address, Channel channel) {
        return Optional.fromNullable(CHANNELS.put(address, channel));
    }
    
    public static Optional<Channel> putChannelIfAbsent(SocketAddress address, Channel channel) {
        return Optional.fromNullable(CHANNELS.putIfAbsent(address, channel));
    }

    public static Optional<Channel> removeChannel(SocketAddress address) {
        return Optional.fromNullable(CHANNELS.remove(address));
    }


    
    
}
