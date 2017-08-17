package com.meta.executor.callable;

import com.google.common.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class ListenableFutureExecutorUtils {

    private static transient Logger logger = LoggerFactory.getLogger(ListenableFutureExecutorUtils.class);

    private int corePoolSize = 1;

    private int maximumPoolSize = 10;

    private int keepAliveTime = 60 * 60;

    private TimeUnit timeUnit = TimeUnit.SECONDS;

    private BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(100);

    private RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();

    private ListeningExecutorService service = null;

    private ThreadPoolExecutor executor = null;

    private volatile static ListenableFutureExecutorUtils singleton;

    public static ListenableFutureExecutorUtils getSingleton() {
            if (singleton == null) {
                synchronized (ListenableFutureExecutorUtils.class) {
                    if (singleton == null) {
                        singleton = new ListenableFutureExecutorUtils();
                    }
                }
            }
        return singleton;
    }

    private ListenableFutureExecutorUtils() {
        init();
    }

    private void init() {
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                keepAliveTime, timeUnit, workQueue, handler);
        service = MoreExecutors.listeningDecorator(executor);
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            if (executor != null) {
                executor.shutdown();
            }
        }));
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            if (service != null) {
                service.shutdown();
            }
        }));
    }

    /**
     * 提交任务,完成后将调用对应的监听器
     * @param task
     * @param callback
     * @param <T>
     * @return
     */
    public <T> void addTask(Callable<T> task,FutureCallback<T> callback) {
        if (task != null && callback != null) {
            ListenableFuture<T> listenableFuture = service.submit(task);
            Futures.addCallback(listenableFuture,callback);
        }
    }

    public ThreadPoolExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(ThreadPoolExecutor executor) {
        this.executor = executor;
    }


}
