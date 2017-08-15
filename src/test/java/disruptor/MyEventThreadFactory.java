package disruptor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by user on 2017/8/15.
 */
public class MyEventThreadFactory implements ThreadFactory {
    private AtomicInteger threadCounter = new AtomicInteger(0);

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setName("event-thread-"+threadCounter.getAndIncrement());
        return t;
    }
}
