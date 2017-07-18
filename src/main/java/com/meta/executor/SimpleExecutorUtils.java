package com.meta.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class SimpleExecutorUtils {
	
	private Logger log = LoggerFactory.getLogger(SimpleExecutorUtils.class);

	private int corePoolSize = 1;

	private int maximumPoolSize = 10;

	private int keepAliveTime = 60 * 60;

	private TimeUnit timeUnit = TimeUnit.SECONDS;

	private BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(100);

	private RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();

	private ThreadPoolExecutor executor = null;

	// private int produceTaskSleepTime = 60; //设置默认值
	
	private volatile static SimpleExecutorUtils singleton;

	public static SimpleExecutorUtils getSingleton() {
		if (singleton == null) {
			synchronized (SimpleExecutorUtils.class) {
				if (singleton == null) {
					singleton = new SimpleExecutorUtils();
				}
			}
		}
		return singleton;
	}

	private SimpleExecutorUtils() {
		init();
	}

	private void init() {
		executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
				keepAliveTime, timeUnit, workQueue, handler);
		Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                if (executor != null) {
                    executor.shutdown();
                }
            }
		});
	}
	
	public String addTask(Runnable task) {
		if(task != null) {
			try {
				executor.execute(task);
				return "success";
			}
			catch (Exception e1) {
				log.error(e1.getMessage());
				return "error";
			}
		} else {
			return "error";
		}
	}

	/**
	 * 检查当前线程池是否有空闲线程用于执行任务
	 * 
	 * @return false：没有空闲线程, true：有空闲线程
	 */
	public boolean checkActive() {
		// 活跃线程数
		int activeNum = SimpleExecutorUtils.getSingleton().getExecutor().getActiveCount();
		// 当前池中的线程数
		int curNum = SimpleExecutorUtils.getSingleton().getExecutor().getPoolSize();
		// 允许最大线程数
		int maxNum = SimpleExecutorUtils.getSingleton().getExecutor().getMaximumPoolSize();
		log.info("线程池中的活跃线程数:" + activeNum + ", 当前池中的线程数:" + curNum + ", 允许最大线程数:" + maxNum);
		// 只要最大线程数大于当前活跃线程数，即可执行任务
		if (maxNum <= activeNum) {
			return false;
		} else {
			return true;
		}
	}

	public ThreadPoolExecutor getExecutor() {
		return executor;
	}

	public void setExecutor(ThreadPoolExecutor executor) {
		this.executor = executor;
	}

}
