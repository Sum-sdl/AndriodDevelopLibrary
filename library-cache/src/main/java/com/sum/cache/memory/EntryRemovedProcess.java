

package com.sum.cache.memory;
/**
 * 数据被删除后进行垃圾回收的一些处理
 */
public class EntryRemovedProcess<V> {

	/***
	 * 当item被回收或者删掉时调用。改方法当value被回收释放存储空间时被remove调用， 
	 * 或者替换item值时put调用，默认实现什么都没做。
	 * 
	 * @param evicted 是否释放被删除的空间
	 * @param key
	 * @param oldValue 老的数据
	 * @param newValue 新数据 void
	 * @throws
	 * @see [类、类#方法、类#成员]
	 */
	public void entryRemoved(boolean evicted, String key, V oldValue,
			V newValue) {
		oldValue = null;
	};
}

