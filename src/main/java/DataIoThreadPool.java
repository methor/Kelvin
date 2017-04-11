import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Mio on 2017/4/10.
 */
public class DataIoThreadPool {

    private static DataIoThreadPool INSTANCE = new DataIoThreadPool();

    private ExecutorService executorService;

    private DataIoThreadPool() {
        executorService = Executors.newCachedThreadPool();
    }

    public static DataIoThreadPool getInstance() {
        return INSTANCE;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
