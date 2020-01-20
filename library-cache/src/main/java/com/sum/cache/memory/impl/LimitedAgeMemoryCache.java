
package com.sum.cache.memory.impl;


import java.util.Collection;
import java.util.Map;

import com.sum.cache.LimitedAge;
import com.sum.cache.entity.CacheEntity;
import com.sum.cache.memory.MemoryCache;
import com.sum.cache.util.CacheLog;

/**
 * Decorator for {@link MemoryCache}. Provides special feature for cache: if
 * some cached object age exceeds defined value then this object will be removed
 * from cache.
 */
public class LimitedAgeMemoryCache implements MemoryCache {

    private final MemoryCache cache;

    private final long maxAge;
    /**
     * @param cache  Wrapped memory cache
     * @param maxAge Max object age <b>(in seconds)</b>. If object age will exceed
     *               this value then it'll be removed from cache on next treatment
     *               (and therefore be reloaded).
     */
    public LimitedAgeMemoryCache(MemoryCache cache, long maxAge) {
        this.cache = cache;
        this.maxAge = maxAge;
    }

    @Override
    public <V> boolean put(String key, V value) {
        if (cache == null) {
            CacheLog.e("Memory Cache is null");
            return false;
        }
        CacheEntity<V> cacheEntity = new CacheEntity<>(new LimitedAge(System.currentTimeMillis(),
                maxAge), value);
        return cache.put(key, cacheEntity);
    }

    @Override
    public <V> boolean put(String key, V value, long maxLimitTime) {
        if (cache == null) {
            CacheLog.e("Memory Cache is null");
            return false;
        }
        CacheEntity<V> cacheEntity = new CacheEntity<V>(new LimitedAge(System.currentTimeMillis(),
                maxLimitTime), value);
        return cache.put(key, cacheEntity);
    }

    @Override
    public <V> V get(String key) {
        if (cache == null) {
            CacheLog.e("Memory Cache is null");
            return null;
        }
        CacheEntity<V> cacheEntity = cache.get(key);
        V value = null;
        if (cacheEntity != null) {
            LimitedAge loadingDate = cacheEntity.getLimitedAge();
            if (loadingDate != null && loadingDate.checkExpire()) {
                cache.remove(key);
            } else {
                value = cacheEntity.getValue();
            }
        }
        return value;
    }

    @Override
    public boolean remove(String key) {
        if (cache == null) {
            CacheLog.e("Memory Cache is null");
            return false;
        }
        return cache.remove(key);
    }

    @Override
    public Collection<String> keys() {
        if (cache == null) {
            CacheLog.e("Memory Cache is null");
            return null;
        }
        return cache.keys();
    }

    @Override
    public Map<String, ?> snapshot() {
        if (cache == null) {
            CacheLog.e("Memory Cache is null");
            return null;
        }
        return cache.snapshot();
    }

    @Override
    public void resize(int maxSize) {
        if (cache == null) {
            CacheLog.e("Memory Cache is null");
            return;
        }
        cache.resize(maxSize);
    }

    @Override
    public void clear() {
        if (cache == null) {
            CacheLog.e("Memory Cache is null");
            return;
        }
        cache.clear();
    }

    @Override
    public void close() {
        if (cache == null) {
            CacheLog.e("Memory Cache is null");
            return;
        }
        cache.close();
    }

}
