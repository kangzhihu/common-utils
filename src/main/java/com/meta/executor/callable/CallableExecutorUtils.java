package com.meta.executor.callable;

import com.meta.executor.runnable.RunnableTask;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

public class CallableExecutorUtils {

    private static transient Logger logger = LoggerFactory.getLogger(CallableExecutorUtils.class);

    private int corePoolSize = 1;

    private int maximumPoolSize = 10;

    private int keepAliveTime = 60 * 60;

    private TimeUnit timeUnit = TimeUnit.SECONDS;

    private BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(100);

    private RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();

    private ThreadPoolExecutor executor = null;

    private Map<String,Future> cache = new ConcurrentHashMap();

    private volatile static CallableExecutorUtils singleton;

    public static CallableExecutorUtils getSingleton(boolean isSingleton) {
        if(isSingleton){
            if (singleton == null) {
                synchronized (CallableExecutorUtils.class) {
                    if (singleton == null) {
                        singleton = new CallableExecutorUtils();
                        return singleton;
                    }
                }
            }
        }else{
            return new CallableExecutorUtils();
        }
        return singleton;
    }

    private CallableExecutorUtils() {
        init();
    }

    private void init() {
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                keepAliveTime, timeUnit, workQueue, handler);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (executor != null) {
                    executor.shutdown();
                }
            }
        });
    }

    /**
     *
     * @param task
     * @param id 若为null或者空，则表示不缓存task
     * @param <T>
     * @return
     */
    public <T> Future<T> addTask(Callable<T> task,String id) {
        if (task != null) {
            try {
                if(StringUtils.isNotBlank(id)){
                    Future<T> f = cache.get(id);
                    if(f != null){
                        return f;
                    }
                    f = executor.submit(task);
                    cache.putIfAbsent(id,f);
                    return f;
                }else{
                    return executor.submit(task);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public ThreadPoolExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(ThreadPoolExecutor executor) {
        this.executor = executor;
    }


    //region mian test
    public static void main(String[] args) throws IOException {
        int count = 300;
        final CountDownLatch latch = new CountDownLatch(count);
        for (int i=0;i<count;i++){
            int index = i;
            new Thread(new RunnableTask("task**"+i) {
                @Override
                public void excute() throws InterruptedException {
                    if(index%10 != 0){
                        CallableExecutorUtils utils = CallableExecutorUtils.getSingleton(true);
                        utils.addTask(new CallableTask<Integer>("Singleton-task") {
                            @Override
                            public Integer excute() {
                                return new Random().nextInt(20);
                            }
                        },"Singleton-"+index%10);
                    }else{
                        CallableExecutorUtils utils2 = CallableExecutorUtils.getSingleton(false);
                        utils2.addTask(new CallableTask<Integer>("Not Singleton-task") {
                            @Override
                            public Integer excute() {
                                return new Random().nextInt(20);
                            }
                        },null);
                    }
                    latch.countDown();
                }
            }).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.error("latch.getCount()---"+latch.getCount());
    }

}
