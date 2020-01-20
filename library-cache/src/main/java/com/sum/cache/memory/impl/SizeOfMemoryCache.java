
package com.sum.cache.memory.impl;


import com.sum.cache.memory.SizeOfCacheCalculator;

/**
 * 重新计算每个缓存数据占的大小的缓存类
 */
public class SizeOfMemoryCache extends LruMemoryCache {

	/** 一个缓存占用的内存计算器 */
	@SuppressWarnings("rawtypes")
	private SizeOfCacheCalculator cacheCalculator;

	public SizeOfMemoryCache(int maxSize, SizeOfCacheCalculator<?> cacheCalculator) {
		super(maxSize);
		this.cacheCalculator = cacheCalculator;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <V> int sizeOf(String key, V value) {
		if (key != null && value == null) {
			return super.sizeOf(key, value);
		} else if (key == null && value == null) {
			return 0;
		}
		if (cacheCalculator != null) {
			return cacheCalculator.sizeOf(key, value);
		}
		return super.sizeOf(key, value);
	}
}
