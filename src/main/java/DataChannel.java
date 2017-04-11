import com.google.common.base.Optional;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Created by mio on 2017/4/5.
 */
public class DataChannel {

    private static int LEN_BYTES = 4;

    public static void send(InetAddress address, int port, byte[] data) {

        try {
            Socket socket = new Socket(InetAddress.getLocalHost(), 0);
            socket.connect(new InetSocketAddress(address, port));

            SocketChannel channel = socket.getChannel();

            ByteBuffer preamble = ByteBuffer.allocate(LEN_BYTES);
            preamble.order(ByteOrder.BIG_ENDIAN);
            preamble.putInt(data.length);

            channel.write(preamble);
            channel.write(ByteBuffer.wrap(data));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Optional<NetworkEvent> recv(Socket socket) {

        try {
            SocketChannel channel = socket.getChannel();

            ByteBuffer preamble =
                    ByteBuffer.allocate(LEN_BYTES).order(ByteOrder.BIG_ENDIAN);
            channel.read(preamble);
            int len = preamble.getInt();

            ByteBuffer payload =
                    ByteBuffer.wrap(new byte[len]).order(ByteOrder.BIG_ENDIAN);

            channel.read(payload);


            int type = payload.getInt();

            NetworkEvent e = new NetworkEvent(socket.getInetAddress(), type,
                    payload.slice().array());

            return Optional.of(e);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.absent();
    }
}
