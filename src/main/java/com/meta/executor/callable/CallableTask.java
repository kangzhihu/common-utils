package com.meta.executor.callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * Created by kzh on 2017/7/19.
 */
public abstract class CallableTask<V> implements Callable<V>{
    private Logger log = LoggerFactory.getLogger(CallableTask.class);

    /** 任务名称 */
    private String taskName;

    /** 执行任务花费的时间（毫秒），需要计算时使用 */
    private long taskExecuteTime = 0;

    /** 被执行任务的数据总量，需要计算时使用 */
    private int taskSize = 0;

    /** 线程任务开始执行时毫秒值 */
    private long startTime;

    /** 线程任务完成执行时毫秒值 */
    private long endTime;

    public CallableTask(String taskName) {
        this.taskName = taskName;
    }

    /**
     * 线程任务执行前
     */
    public void beforeTask(){}

    /**
     * 线程任务执行后
     */
    public void afterTask(){}

    /**
     * 线程任务执行过程
     */
    public abstract V excute();

    @Override
    public V call() throws Exception {
        beforeTask();
        V result = excute();
        afterTask();
        return result;
    }


    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public long getTaskExecuteTime() {
        return taskExecuteTime;
    }

    public void setTaskExecuteTime(long taskExecuteTime) {
        this.taskExecuteTime = taskExecuteTime;
    }

    public int getTaskSize() {
        return taskSize;
    }

    public void setTaskSize(int taskSize) {
        this.taskSize = taskSize;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
