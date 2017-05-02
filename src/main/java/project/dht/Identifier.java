package project.dht;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.hash.*;
import io.netty.buffer.ByteBuf;
import project.GlobalConfiguration;

import javax.security.auth.login.Configuration;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by Mio on 2017/4/20.
 */
public class Identifier implements Comparable<Identifier> {
    public final int len = GlobalConfiguration.getInstance().getHashSize() / 8;
    public final byte[] id = new byte[len];

    @JsonIgnore
    static Funnel<InetAddress> inetAddressFunnel = (Funnel<InetAddress>)
            (from, into) -> into.putBytes(from.getAddress());
    Identifier() {
    }

    public Identifier(InetAddress address) {
        HashFunction hf = Hashing.md5();
        HashCode hc = hf.newHasher().putObject(address, inetAddressFunnel).hash();
        hc.writeBytesTo(id, 0, len);
    }


    Identifier(Identifier id) {
        System.arraycopy(id.id, 0, id, 0, len);
    }

    @Override
    public int compareTo(Identifier o)
    {
        ByteBuffer b1 = ByteBuffer.wrap(this.id);
        ByteBuffer b2 = ByteBuffer.wrap(o.id);
        return b1.compareTo(b2);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Identifier))
            return false;
        return Arrays.equals(id, ((Identifier)obj).id);
    }

    @Override
    public String toString() {
        return "Identifier{" +
                "id=" + Arrays.toString(id) +
                ", len=" + len +
                '}';
    }
}
