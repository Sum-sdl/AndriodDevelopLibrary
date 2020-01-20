package com.sum.cache;

import java.util.concurrent.Executor;

/**
 * <p>加载和存储数据的任务执行
 */
public class CacheEngine {
    private Executor taskExecutor;

    public CacheEngine(CacheConfiguration configuration) {
        taskExecutor = configuration.taskExecutor;
    }

    public void submit(Runnable command) {
        taskExecutor.execute(command);
    }
}

