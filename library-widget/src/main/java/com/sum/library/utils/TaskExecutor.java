package com.sum.library.utils;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

/**
 * Created by sdl on 2019/2/14.
 * 全局多子线程任务执行
 */
public class TaskExecutor {

    private static final MainThreadExecutor MAIN_THREAD = new MainThreadExecutor();
    private static final Executor IO_THREAD = AsyncTask.SERIAL_EXECUTOR;
    private static final Executor IO_M_THREAD = AsyncTask.THREAD_POOL_EXECUTOR;

    private static class MainThreadExecutor implements Executor {

        private Handler mMainHandler;
        private int mDelayTime = 0;

        @Override
        public void execute(Runnable command) {
            if (mMainHandler == null) {
                mMainHandler = new Handler(Looper.getMainLooper());
            }
            int delayTime = mDelayTime;
            mMainHandler.postDelayed(command, delayTime);
            mDelayTime = 0;
        }
    }

    //串行子线程任务
    public static void ioThread(Runnable task) {
        IO_THREAD.execute(task);
    }

    //多线程执行任务
    public static void ioMThread(Runnable task) {
        IO_M_THREAD.execute(task);
    }

    //主线程任务
    public static void mainThread(Runnable task) {
        MAIN_THREAD.execute(task);
    }

    //主线程任务
    public static void mainThread(Runnable task, int delayTime) {
        MAIN_THREAD.mDelayTime = delayTime;
        MAIN_THREAD.execute(task);
    }
}
