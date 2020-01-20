
package com.sum.cache.memory.impl;


import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

import com.sum.cache.memory.MemoryCache;
import com.sum.cache.util.CacheLog;

/**
 * Decorator for {@link MemoryCache}. Provides special feature for cache: some
 * different keys are considered as equals (using {@link Comparator comparator}
 * ). And when you try to put some value into cache by key so entries with
 * "equals" keys will be removed from cache before.<br />
 * <b>NOTE:</b> Used for internal needs. Normally you don't need to use this
 * class.
 */
public class FuzzyKeyMemoryCache implements MemoryCache {

	private final MemoryCache cache;
	private final Comparator<String> keyComparator;

	public FuzzyKeyMemoryCache(MemoryCache cache,
			Comparator<String> keyComparator) {
		this.cache = cache;
		this.keyComparator = keyComparator;
	}

	@Override
	public <V> boolean put(String key, V value) {
		if (cache == null) {
			CacheLog.e("MemoryCache缓存操作对象为空");
			return false;
		}
		// Search equal key and remove this entry
		synchronized (cache) {
			String keyToRemove = null;
			for (String cacheKey : cache.keys()) {
				if (keyComparator.compare(key, cacheKey) == 0) {
					keyToRemove = cacheKey;
					break;
				}
			}
			if (keyToRemove != null) {
				cache.remove(keyToRemove);
			}
		}
		return cache.put(key, value);
	}

	@Override
	public <V> boolean put(String key, V value, long maxLimitTime) {
		if (cache == null) {
			CacheLog.e("MemoryCache缓存操作对象为空");
			return false;
		}
		synchronized (cache) {
			String keyToRemove = null;
			for (String cacheKey : cache.keys()) {
				if (keyComparator.compare(key, cacheKey) == 0) {
					keyToRemove = cacheKey;
					break;
				}
			}
			if (keyToRemove != null) {
				cache.remove(keyToRemove);
			}
		}
		return cache.put(key, value, maxLimitTime);
	}

	@Override
	public <V> V get(String key) {
		if (cache == null) {
			CacheLog.e("MemoryCache缓存操作对象为空");
			return null;
		}
		return cache.get(key);
	}

	@Override
	public boolean remove(String key) {
		if (cache == null) {
			CacheLog.e( "MemoryCache缓存操作对象为空");
			return false;
		}
		return cache.remove(key);
	}

	@Override
	public Collection<String> keys() {
		if (cache == null) {
			CacheLog.e( "MemoryCache缓存操作对象为空");
			return null;
		}
		return cache.keys();
	}

	@Override
	public Map<String, ?> snapshot() {
		if (cache == null) {
			CacheLog.e( "MemoryCache缓存操作对象为空");
			return null;
		}
		return cache.snapshot();
	}

	@Override
	public void resize(int maxSize) {
		if (cache == null) {
			CacheLog.e( "MemoryCache缓存操作对象为空");
			return;
		}
		cache.resize(maxSize);
	}

	@Override
	public void clear() {
		if (cache == null) {
			CacheLog.e("MemoryCache缓存操作对象为空");
			return;
		}
		cache.clear();
	}

	@Override
	public void close() {
		if (cache == null) {
			CacheLog.e("MemoryCache缓存操作对象为空");
			return;
		}
		cache.close();
	}
}
