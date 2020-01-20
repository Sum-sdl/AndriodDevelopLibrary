package com.sum.cache.memory.impl;


import com.sum.cache.memory.EntryRemovedProcess;

/**
 *可以自定义数据被删除后的垃圾回收处理的缓存
 */
public class EntryRemovedProcessMemoryCache extends LruMemoryCache {

	/**
	 * 垃圾回收处理者
	 */
	@SuppressWarnings("rawtypes")
	private EntryRemovedProcess mEntryRemovedProcess;
	
	public EntryRemovedProcessMemoryCache(int maxSize,
			EntryRemovedProcess<?> entryRemovedProcess) {
		super(maxSize);
		this.mEntryRemovedProcess = entryRemovedProcess;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <V> void entryRemoved(boolean evicted, String key, V oldValue,
			V newValue) {
		if (mEntryRemovedProcess != null) {
			mEntryRemovedProcess.entryRemoved(evicted, key, oldValue, newValue);
		} else {
			super.entryRemoved(evicted, key, oldValue, newValue);
		}
	}
}
