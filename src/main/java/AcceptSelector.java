import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Mio on 2017/4/10.
 */
public class AcceptSelector {

    private Selector selector;

    private AcceptSelector() {
        try
        {
            selector = Selector.open();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    private static class SingletonHolder {
        private static final AcceptSelector INSTANCE = new AcceptSelector();
    }

    public static AcceptSelector getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public Selector getSelector() {
        return selector;
    }

    public void startup() {
        while (true) {
            try
            {
                int num = selector.select();
                if (num == 0) continue;
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if ((selectionKey.readyOps() & SelectionKey.OP_ACCEPT) ==
                            SelectionKey.OP_ACCEPT) {
                        ServerSocket serverSocket = (ServerSocket)selectionKey.attachment();
                        Socket socket = serverSocket.accept();

                        /** submit a callable, which starts a "session" to repeatedly
                            reads messages and determines by the client/server identifier
                            and what major and then transfer the data as well as the
                            minor type to the corresponding handler strategy.
                        */
                        DataIoThreadPool.getInstance().getExecutorService()
                                .submit();
                    }
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
