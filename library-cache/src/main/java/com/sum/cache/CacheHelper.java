

package com.sum.cache;

import android.text.TextUtils;

import com.esotericsoftware.kryo.Serializer;

import java.io.File;
import java.io.IOException;

import com.sum.cache.disk.DiskCache;
import com.sum.cache.disk.StoragePlainFile;
import com.sum.cache.disk.impl.LimitedAgeDiskCache;
import com.sum.cache.memory.MemoryCache;
import com.sum.cache.process.CacheDataProcess;
import com.sum.cache.util.CacheLog;

/**
 * 基本的加载存储功能
 */
public class CacheHelper {
    /**
     * 磁盘缓存类
     */
    private DiskCache diskCache;

    /***
     * 内存缓存类
     */
    private MemoryCache memoryCache;

    StoragePlainFile storagePlainFile;

    public CacheHelper(DiskCache diskCache, MemoryCache memoryCache,
            StoragePlainFile storagePlainFile) {
        this.diskCache = diskCache;
        this.memoryCache = memoryCache;
        this.storagePlainFile = storagePlainFile;
        this.diskCache.setStoragePlainFile(storagePlainFile);
    }


    /***
     * 插入一条缓存数据
     *
     * @param key                数据标识
     * @param value              要存入缓存的数据
     * @param insertCacheProcess 对插入的数据进行处理的类(处理后在插入缓存)
     */
    public <V> void insert(String key, V value, CacheDataProcess<V> insertCacheProcess,
            CacheOption option) {
        insert(key, insertCacheProcess.process(value), option);
    }


    /***
     * 插入一条缓存数据
     *
     * @param <V>            泛型
     * @param key            数据标识
     * @param value          要存入缓存的数据
     * @param option         存储配置信息
     */
    public <V> void insert(String key, V value, CacheOption option) {
        if (diskCache == null || memoryCache == null) {
            CacheLog.e("diskCache or memoryCache or cachePutEntity is null");
            return;
        }
        try {
            if (option.isCacheMemory()) {
                if (option.cacheMemoryTime() > 0) {
                    memoryCache.put(key, value, option.cacheMemoryTime());
                } else {
                    memoryCache.put(key, value);
                }
            }

            //以下是磁盘缓存
            if (option.isCacheDisk()) {
                File file = diskCache.getFile(key);
                if (file != null && file.exists()) {
                    diskCache.remove(key);
                }
                if (option.cacheDiskTime() > 0) {
                    diskCache.put(key, value, option.cacheDiskTime());
                } else {
                    diskCache.put(key, value);
                }
            }
        } catch (IOException e) {
            CacheLog.e("Fail to write in disc");
        } catch (Exception e) {
            CacheLog.e("Fail to write in disc");
        }

    }

    /***
     * 查询一条缓存数据
     *
     * @param queryDataProcess 对于查询到的数据进行处理的方法(从缓存中取道数据后再进行加工处理后给使用者)
     * @see [类、类#方法、类#成员]
     */
    public <V> V query(String key, CacheDataProcess<V> queryDataProcess) {
        return queryDataProcess.process((V) query(key));
    }

    /***
     * 查询一条缓存数据
     */
    public <V> V query(String key) {
        return query(key, false);
    }

    /***
     * 查询一条缓存数据
     */
    public <V> V query(String key, boolean isCachedToMemory) {
        if (diskCache == null || memoryCache == null) {
            CacheLog.e("diskCache or memoryCache or cacheGetEntity is null");
            return null;
        }
        V value = null;
        try {
            value = memoryCache.get(key);
            if (value != null) {
                return value;
            }
            value = diskCache.get(key);
            if (value != null && isCachedToMemory) {
                if (diskCache instanceof LimitedAgeDiskCache) {
                    LimitedAgeDiskCache limitedAgeDiskCache = (LimitedAgeDiskCache) diskCache;
                    long limitedTime = limitedAgeDiskCache.getLimitedTime(key);
                    if (limitedTime > 0) {
                        memoryCache.put(key, value, limitedTime);
                    }
                } else {
                    memoryCache.put(key, value);
                }
            }
        } catch (Exception e) {
            CacheLog.e("Fail to get cache data");
        }
        return value;
    }

    /***
     * 删除一条缓存数据
     *
     * @param key 数据标识
     * @return 是否删除成功 boolean
     */
    public boolean delete(String key) {
        if (diskCache == null || memoryCache == null) {
            CacheLog.e("diskCache or memoryCache or cachePutEntity is null");
            return false;
        }
        boolean isSuccess = false;
        if (memoryCache.keys().contains(key)) {
            isSuccess = memoryCache.remove(key);
        }
        File file = diskCache.getFile(key);
        if (file != null && file.exists()) {
            isSuccess = diskCache.remove(key);
        }

        return isSuccess;
    }

    /***
     * 计算缓存大小
     */
    public long size() {
        if (diskCache == null || memoryCache == null) {
            CacheLog.e("diskCache or memoryCache or cachePutEntity is null");
            return 0;
        }
        String mapString = memoryCache.snapshot().toString();
        long memoryCacheSize = TextUtils.isEmpty(mapString) ? 0 : mapString.getBytes().length;
        return diskCache.size() + memoryCacheSize;
    }
    /**
     * Adds a custom serializer for a specific class
     * When used, must be called right after Paper.init()
     *
     * @param clazz      type of the custom serializer
     * @param serializer the serializer instance
     * @param <T>        type of the serializer
     */
    public  <T> void addSerializer(Class<T> clazz, Serializer<T> serializer) {
        if (storagePlainFile !=null){
            storagePlainFile.addSerializer(clazz,serializer);
        }

    }
    public DiskCache getDiskCache() {
        return diskCache;
    }

    public void setDiskCache(DiskCache diskCache) {
        this.diskCache = diskCache;
    }

    public MemoryCache getMemoryCache() {
        return memoryCache;
    }

    public void setMemoryCache(MemoryCache memoryCache) {
        this.memoryCache = memoryCache;
    }

    /**
     * 关闭缓存，对象完全被清理，关闭后不能再使用此缓存 void
     */
    public void close() {
        if (diskCache != null) {
            diskCache.close();
            diskCache = null;
        }
        if (memoryCache != null) {
            memoryCache.close();
            memoryCache = null;
        }
    }

    /***
     * 清理掉当前缓存
     */
    public void clear() {
        if (diskCache != null) {
            diskCache.clear();
        }
        if (memoryCache != null) {
            memoryCache.clear();
        }
    }

}
