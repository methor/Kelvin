package Network;

import com.google.common.base.Optional;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by mio on 2017/4/16.
 */
public class MessageBlockingQueue {

    static class Message {
        private int messageIndex;
        private Object payLoad;

        public Message(int messageIndex, Object payLoad) {
            this.messageIndex = messageIndex;
            this.payLoad = payLoad;
        }
    }

    private static final int CAPACITY = 100;

    /**
     * this variable is only be used to be set and decrement by ONE thread, so no
     * synchronization is applied.
     */
    private int toBeRemoved = 0;


    private BlockingQueue<Object> queue;

    public MessageBlockingQueue() {queue = new ArrayBlockingQueue<>(CAPACITY);}
    public MessageBlockingQueue(int capacity) {queue = new ArrayBlockingQueue<>(capacity);}

    public Optional peek() {

        return Optional.fromNullable(queue.peek());
    }

    public Optional poll() {

        return Optional.fromNullable(queue.poll());
    }

    public Optional poll(long timeout, TimeUnit unit) {
        // retrieve object in the head
        if (toBeRemoved == 0)
            try {
                return Optional.fromNullable(queue.poll(timeout, unit));
            } catch (InterruptedException e) {
                e.printStackTrace();
                return Optional.absent();
            }
        // discard toBeRemoved objects, and measure each time interval between successive
        // poll invocation to decide whether timed out.
        else {
            try {
                long nanoTimeout = unit.toNanos(timeout);
                long residual = nanoTimeout;
                long t0 = System.nanoTime();

                while (toBeRemoved > 0) {
                    Object object = queue.poll(residual, TimeUnit.NANOSECONDS);
                    if (object != null)
                    {
                        toBeRemoved--;
                        long t1 = System.nanoTime();
                        if (t1 - t0 >= nanoTimeout)
                            return Optional.absent();
                        residual = nanoTimeout - t1 + t0;
                    }
                    else
                        return Optional.absent();
                }
                long t1 = System.nanoTime();
                if (t1 - t0 < nanoTimeout)
                    return Optional.fromNullable(queue.poll(nanoTimeout - t1 + t0,
                        TimeUnit.NANOSECONDS));
                else
                    return Optional.absent();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return Optional.absent();
            }
        }
    }

    public void ignore(int i) {
        toBeRemoved = i;
    }

    public void put(Object e) {
        try {
            queue.put(e);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }







//    public boolean offer(Object e) {
//        return queue.offer(e);
//    }
//
//    public boolean offer(Object e, long timeout, TimeUnit unit) {
//        try {
//            return queue.offer(e, timeout, unit);
//        } catch (InterruptedException e1) {
//            e1.printStackTrace();
//        }
//        return false;
//    }
}
