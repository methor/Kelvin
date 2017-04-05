import com.google.common.base.Optional;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by mio on 2017/4/5.
 */
public class DataChannel {

    private static int LEN_BYTES = 4;

    public static void send(InetAddress address, int port, byte[] data) {

        try {
            Socket socket = new Socket(InetAddress.getLocalHost(), 0);
            socket.connect(new InetSocketAddress(address, port));
            OutputStream outputStream = socket.getOutputStream();
            ByteBuffer b = ByteBuffer.allocate(LEN_BYTES);
            b.order(ByteOrder.BIG_ENDIAN);
            b.putInt(data.length);
            outputStream.write(b.array());
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Optional<byte[]> recv(Socket socket) {

        try {
            InputStream inputStream = socket.getInputStream();
            ByteBuffer preamble = ByteBuffer.allocate(LEN_BYTES);
            inputStream.read(preamble.array());
            preamble.order(ByteOrder.BIG_ENDIAN);
            int len = preamble.getInt();

            byte[] data = new byte[len];
            inputStream.read(data);

            return Optional.of(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.absent();
    }
}
