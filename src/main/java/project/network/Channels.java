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
    
    private static final Map<SocketAddress, Channel> SERVERSIDECHANNELS = new ConcurrentHashMap<>();
    private static final Map<SocketAddress, Channel> CLIENTSIDECHANNELS = new ConcurrentHashMap<>();
    
    public static Optional<Channel> getServerSideChannel(SocketAddress address) {
        return Optional.fromNullable(SERVERSIDECHANNELS.get(address));
    }
    
    public static Optional<Channel> putServerSideChannel(SocketAddress address, Channel channel) {
        return Optional.fromNullable(SERVERSIDECHANNELS.put(address, channel));
    }
    
    public static Optional<Channel> putServerSideChannelIfAbsent(SocketAddress address, Channel channel) {
        return Optional.fromNullable(SERVERSIDECHANNELS.putIfAbsent(address, channel));
    }

    public static Optional<Channel> removeServerSideChannel(SocketAddress address) {
        return Optional.fromNullable(SERVERSIDECHANNELS.remove(address));
    }

    public static Optional<Channel> getClientSideChannel(SocketAddress address) {
        return Optional.fromNullable(CLIENTSIDECHANNELS.get(address));
    }

    public static Optional<Channel> putClientSideChannel(SocketAddress address, Channel channel) {
        return Optional.fromNullable(CLIENTSIDECHANNELS.put(address, channel));
    }

    public static Optional<Channel> putClientSideChannelIfAbsent(SocketAddress address, Channel channel) {
        return Optional.fromNullable(CLIENTSIDECHANNELS.putIfAbsent(address, channel));
    }

    public static Optional<Channel> removeClientSideChannel(SocketAddress address) {
        return Optional.fromNullable(CLIENTSIDECHANNELS.remove(address));
    }
    
    
}
