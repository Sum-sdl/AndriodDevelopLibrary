

package com.sum.cache;
/**
 * 缓存接口
 */
public interface Cache {
	
	/**
	 * Puts value into cache by key
	 * @param <V>
	 *
	 * @return <b>true</b> - if value was put into cache successfully, <b>false</b> - if value was <b>not</b> put into
	 * cache
	 */
	<V> boolean put(String key, V value);
	
	/**
	 * Puts value into cache by key
	 * @param <V>
	 *@param maxLimitTime 内存缓存数据的有效期(单位秒)
	 * @return <b>true</b> - if value was put into cache successfully, <b>false</b> - if value was <b>not</b> put into
	 * cache
	 */
	<V> boolean put(String key, V value, long maxLimitTime);

	/** 
	 * Returns value by key. If there is no value for key then null will be returned. 
	 * @param <V>*/
	<V> V get(String keyk);

	/** Removes item by key */
	boolean remove(String key);
	
	/***
	 * 重置内存缓存的最大限度大小
	 * @param maxSize
	 * void
	 * @see [类、类#方法、类#成员]
	 */
	void resize(int maxSize);
	
	/**
	 * 关闭缓存，关闭后不能再使用此缓存
	 * void
	 * @see [类、类#方法、类#成员]
	 */
	void close();

	/***
	 * 清理掉当前缓存,对象完全被清理
	 * void
	 * @see [类、类#方法、类#成员]
	 */
	void clear();
}
