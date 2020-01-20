

package com.sum.cache.process;
/**
 * 默认的缓存数据加工处理器
 */
public class DefaultCacheDataProcess<V> implements CacheDataProcess<V>{

	@Override
	public V process(V data) {
		return data;
	}

}

