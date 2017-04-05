import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created by mio on 2017/4/5.
 */
public class Server {
    public String getName() {
        return name;
    }

    public InetAddress getAddress() {
        return address;
    }

    private final String name;
    private final InetAddress address;

    public Server(String name, InetAddress address) {
        this.name = name;
        this.address = address;
    }


}
