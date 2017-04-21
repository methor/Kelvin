package project.dht;

import project.GlobalConfiguration;

import javax.security.auth.login.Configuration;
import java.nio.ByteBuffer;

/**
 * Created by Mio on 2017/4/20.
 */
public class Identifier implements Comparable<Identifier> {
    private final int len;
    private final byte[] id;
    public Identifier() {
        len = GlobalConfiguration.getInstance().getHashSize() / 8;
        id = new byte[len];
    }

    @Override
    public int compareTo(Identifier o)
    {
        ByteBuffer b1 = ByteBuffer.wrap(this.id);
        ByteBuffer b2 = ByteBuffer.wrap(o.id);
        return b1.compareTo(b2);
    }
}
