
package com.sum.cache.util;

import android.content.Context;

import java.io.File;
import java.io.IOException;

import com.sum.cache.disk.DiskCache;
import com.sum.cache.disk.impl.BaseDiskCache;
import com.sum.cache.disk.impl.ext.LruDiskCache;
import com.sum.cache.disk.naming.FileNameGenerator;
import com.sum.cache.disk.naming.HashCodeFileNameGenerator;

/**
 * Utility for convenient work with disk cache.<br />
 * <b>NOTE:</b> This utility works with file system so avoid using it on
 * application main thread.
 */
public final class DiskCacheUtils {

	private DiskCacheUtils() {
	}

	/**
	 * Creates {@linkplain HashCodeFileNameGenerator default implementation} of
	 * FileNameGenerator
	 */
	public static FileNameGenerator createFileNameGenerator() {
		return new HashCodeFileNameGenerator();
	}

	/**
	 * Creates default implementation of {@link DiskCache} depends on incoming
	 * parameters
	 * @param diskCacheSize 可使用磁盘最大大小(单位byte,字节)
	 */
	public static DiskCache createDiskCache(Context context,
											FileNameGenerator diskCacheFileNameGenerator, long diskCacheSize,
											int diskCacheFileCount) {
		File reserveCacheDir = createReserveDiskCacheDir(context);
		if (diskCacheSize > 0 || diskCacheFileCount > 0) {
			File individualCacheDir = StorageUtils
					.getIndividualCacheDirectory(context);
			try {
				return new LruDiskCache(individualCacheDir, reserveCacheDir,
						diskCacheFileNameGenerator, diskCacheSize,
						diskCacheFileCount);
			} catch (IOException e) {
				CacheLog.e("Fail to get lru dis cache");
				// continue and create unlimited cache
			}
		}
		File cacheDir = StorageUtils.getCacheDirectory(context);
		return new BaseDiskCache(cacheDir, reserveCacheDir,
				diskCacheFileNameGenerator);
	}

	/**
	 * Creates reserve disk cache folder which will be used if primary disk
	 * cache folder becomes unavailable
	 */
	private static File createReserveDiskCacheDir(Context context) {
		File cacheDir = StorageUtils.getCacheDirectory(context, false);
		File individualDir = new File(cacheDir, "data");
		if (individualDir.exists() || individualDir.mkdir()) {
			cacheDir = individualDir;
		}
		return cacheDir;
	}

	/**
	 * Returns {@link File} of cached key or <b>null</b> if key was not cached
	 * in disk cache
	 */
	public static File findInCache(String key, DiskCache diskCache) {
		File file = diskCache.getFile(key);
		return file != null && file.exists() ? file : null;
	}

	/**
	 * Removed cached key file from disk cache (if key was cached in disk cache
	 * before)
	 *
	 * @return <b>true</b> - if cached key file existed and was deleted;
	 *         <b>false</b> - otherwise.
	 */
	public static boolean removeFromCache(String key, DiskCache diskCache) {
		File file = diskCache.getFile(key);
		return file != null && file.exists() && file.delete();
	}
}
