package com.sum.andrioddeveloplibrary.java;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by sdl on 2018/11/8.
 */
public class Test {

    //CountDownLatch

    public static class Async extends AbstractQueuedSynchronizer {

        @Override
        protected boolean tryAcquire(int arg) {
            return super.tryAcquire(arg);
        }

        @Override
        protected boolean tryRelease(int arg) {
            return super.tryRelease(arg);
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            return super.tryReleaseShared(arg);
        }

        @Override
        protected int tryAcquireShared(int arg) {
            return super.tryAcquireShared(arg);
        }
    }

    private volatile int count;
    private volatile AtomicInteger count2;
    private AtomicInteger count3;

    public void start() {
        count3 = new AtomicInteger();
        count2 = new AtomicInteger();
        int len = 10;

        //CountDownLatch 通过await等待10个线程全部执行完成后再执行后面的代码
        CountDownLatch latch = new CountDownLatch(len);
        for (int i = 1; i <= len; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    count++;
                    count2.getAndIncrement();
                    count3.getAndIncrement();
                }
            }).start();
        }

        System.out.println("count=" + count + "," + count2 + "," + count3);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("count=" + count + "," + count2 + "," + count3);
    }

    public void start2() {
        count3 = new AtomicInteger();
        count2 = new AtomicInteger();
        int len = 10;

        //CountDownLatch 通过await等待10个线程全部执行完成后再执行后面的代码
        ReentrantLock lock = new ReentrantLock();
        for (int i = 1; i <= len; i++) {
            new Thread(() -> {
                boolean tryLock = lock.tryLock();
                System.out.println("tryLock=" + tryLock);
                if (tryLock) {
                    for (int j = 0; j < 1000; j++) {
                        count++;
                        count2.getAndIncrement();
                        count3.getAndIncrement();
                    }
                    System.out.println("success");
                    lock.unlock();
                }
            }).start();
        }
        System.out.println("count=" + count + "," + count2 + "," + count3);
    }
}

