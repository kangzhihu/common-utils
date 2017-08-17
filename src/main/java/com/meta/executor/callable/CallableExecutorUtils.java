package com.meta.executor.callable;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class CallableExecutorUtils {

    private Logger logger = LoggerFactory.getLogger(CallableExecutorUtils.class);

    private int corePoolSize = 1;

    private int maximumPoolSize = 10;

    private int keepAliveTime = 60 * 60;

    private TimeUnit timeUnit = TimeUnit.SECONDS;

    private BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(100);

    private RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();

    private ThreadPoolExecutor executor = null;

    private CompletionService completionServcie = null;

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
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            if (executor != null) {
                executor.shutdown();
            }
        }));
        completionServcie = new ExecutorCompletionService(executor);
    }

    /**
     * 添加task
     * @param task
     * @param id 有值表示cache key
     * @param <T>
     * @return
     */
    public <T> Future<T> addTask(Callable<T> task,String... id) {
        if (task != null) {
            try {
                if(id != null && id.length>0 &&StringUtils.isNotBlank(id[0])){
                    Future<T> f = cache.get(id[0]);
                    if(f != null){
                        return f;
                    }
                    f = (Future<T>)completionServcie.submit(task);
                    cache.putIfAbsent(id[0],f);
                    return f;
                }else{
                    return (Future<T>)completionServcie.submit(task);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 添加多个任务（任务之间非阻塞式）
     * @param tasks
     * @param <T>
     * @param <R>
     * @return
     */
    public <T,R> List<T> addTasks(List<Callable<T>> tasks) {
        List<T> list = new ArrayList<>(0);
        if (tasks != null && !tasks.isEmpty()) {
            int size = tasks.size();
            tasks.forEach((task) -> {
                completionServcie.submit(task);
            });
            list = new ArrayList<>(size);
            try {
                for (int i = 0; i < size; i++) {
                    // take 方法等待下一个结果并返回 Future 对象。
                    // poll 不等待，有结果就返回一个 Future 对象，否则返回 null。
                    Future<T> future = (Future<T>)completionServcie.take();
                    list.add(future.get());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public ThreadPoolExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(ThreadPoolExecutor executor) {
        this.executor = executor;
    }

}
