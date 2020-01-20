

package com.sum.cache.process;
/**
 * 缓存数据加工接口
 */
public interface CacheDataProcess<V> {

	/**
	 * 加工缓存的数据
	 * @param data
	 * @return 返回加工处理后的数据
	 * V
	 * @see [类、类#方法、类#成员]
	 */
	V process(V data);
}

