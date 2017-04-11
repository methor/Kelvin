import java.net.InetAddress;

/**
 * Created by mio on 2017/4/5.
 */

/**
    @field type: interpret leading 8 bits as client/server identifier, following 8 bits as major type (gossip,
 data, replica, etc.), and the last 16 bits being minor type which is handled by individual strategies
 */
public class NetworkEvent {

    private final InetAddress address;
    private final int type;
    private final byte[] payload;

    public NetworkEvent(InetAddress address, int type, byte[] payload) {
        this.address = address;
        this.type = type;
        this.payload = payload;
    }
}
