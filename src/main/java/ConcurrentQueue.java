import com.google.common.base.Optional;
import sun.nio.ch.Net;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by mio on 2017/4/5.
 */
public class ConcurrentQueue<T> {

    private static final int CAPACITY = 100000;

    private BlockingQueue<T> queue = new LinkedBlockingQueue<>(CAPACITY);

    public Optional<T> peek() {

        return Optional.fromNullable(queue.peek());
    }

    public Optional<T> poll() {

        return Optional.fromNullable(queue.poll());
    }

    public Optional<T> take() {

        try {
            return Optional.of(queue.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return Optional.absent();
    }

    public void put(T e) {
        try {
            queue.put(e);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    public boolean offer(T e) {
        return queue.offer(e);
    }

    public boolean offer(T e, long timeout, TimeUnit unit) {
        try {
            return queue.offer(e, timeout, unit);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        return false;
    }
}
