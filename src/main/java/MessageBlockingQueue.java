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

    private static final int CAPACITY = 100000;




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
        if (toBeRemoved == 0)
            try {
                return Optional.fromNullable(queue.poll(timeout, unit));
            } catch (InterruptedException e) {
                e.printStackTrace();
                return Optional.absent();
            }
        else {
            try {
                Thread.sleep(1,2);
                while (toBeRemoved > 0) {
                    Object object = queue.poll();
                    if (object != null)
                        toBeRemoved--;
                    else
                        return Optional.absent();
                }
                return Optional.fromNullable(queue.poll());
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
