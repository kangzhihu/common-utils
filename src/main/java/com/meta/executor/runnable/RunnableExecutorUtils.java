package com.meta.executor.runnable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class RunnableExecutorUtils {

    private static transient Logger logger = LoggerFactory.getLogger(RunnableExecutorUtils.class);

    private int corePoolSize = 1;

    private int maximumPoolSize = 10;

    private int keepAliveTime = 60 * 60;

    private TimeUnit timeUnit = TimeUnit.SECONDS;

    private BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(100);

    private RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();

    private ThreadPoolExecutor executor = null;

    // private int produceTaskSleepTime = 60; //设置默认值

    private volatile static RunnableExecutorUtils singleton;

    public static RunnableExecutorUtils getSingleton() {
        if (singleton == null) {
            synchronized (RunnableExecutorUtils.class) {
                if (singleton == null) {
                    singleton = new RunnableExecutorUtils();
                }
            }
        }
        return singleton;
    }

    private RunnableExecutorUtils() {
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

    public boolean addTask(Runnable task) {
        if (task != null) {
            try {
                executor.execute(task);
                return true;
            } catch (Exception e1) {
                logger.error(e1.getMessage());
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 检查当前线程池是否有空闲线程用于执行任务
     *
     * @return false：没有空闲线程, true：有空闲线程
     */
    public boolean checkActive() {
        // 活跃线程数
        int activeNum = RunnableExecutorUtils.getSingleton().getExecutor().getActiveCount();
        // 当前池中的线程数
        int curNum = RunnableExecutorUtils.getSingleton().getExecutor().getPoolSize();
        // 允许最大线程数
        int maxNum = RunnableExecutorUtils.getSingleton().getExecutor().getMaximumPoolSize();
        logger.info("线程池中的活跃线程数:" + activeNum + ", 当前池中的线程数:" + curNum + ", 允许最大线程数:" + maxNum);
        // 只要最大线程数大于当前活跃线程数，即可执行任务
        return maxNum > activeNum;
    }

    public ThreadPoolExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(ThreadPoolExecutor executor) {
        this.executor = executor;
    }

}
