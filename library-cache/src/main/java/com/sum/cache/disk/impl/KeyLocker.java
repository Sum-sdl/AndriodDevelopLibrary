package com.sum.cache.disk.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;

/**
 * This class allows multiple threads to lock against a string key
 */
public class KeyLocker {
    private ConcurrentMap<String, Semaphore> semaphoreMap = new ConcurrentHashMap<>();

    public void acquire(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key couldn't be null");
        }

        if (!semaphoreMap.containsKey(key)) {
            semaphoreMap.put(key, new Semaphore(1, true));
        }
        Semaphore semaphore = semaphoreMap.get(key);
        semaphore.acquireUninterruptibly();
    }

    public void release(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Key couldn't be null");
        }

        Semaphore semaphore = semaphoreMap.get(key);
        if (semaphore == null) {
            throw new IllegalStateException("Couldn't release semaphore. The acquire() with the same key '"
                    + key + "' has to be called prior to calling release()");
        }
        semaphore.release();
    }

}
