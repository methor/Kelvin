import java.net.InetAddress;

/**
 * Created by mio on 2017/4/5.
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
