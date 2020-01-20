
package com.sum.cache.memory;

import android.graphics.Bitmap;

/**
 * 内存大小计算器
 */
public class SizeOfCacheCalculator<V> {

	/**
	 * 计算一个缓存数据的大小,默认是1(就是一个数据)
	 * 
	 * @param value 缓存至
	 * @return int
	 * @see [类、类#方法、类#成员]
	 */
	public int sizeOf(String key, V value) {
		if (value instanceof Bitmap) {
			Bitmap bitmap = (Bitmap) value;
			return bitmap.getRowBytes() * bitmap.getHeight();
		} else if (value instanceof byte[]) {
			return ((byte[]) value).length;
		}
		return 1;
	}
}

