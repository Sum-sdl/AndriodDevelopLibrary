

package com.sum.cache;

import android.content.Context;

import java.util.concurrent.Executor;

import com.sum.cache.disk.DiskCache;
import com.sum.cache.disk.StoragePlainFile;
import com.sum.cache.disk.impl.KryoStoragePlainFile;
import com.sum.cache.disk.impl.LimitedAgeDiskCache;
import com.sum.cache.disk.naming.FileNameGenerator;
import com.sum.cache.disk.naming.HashCodeFileNameGenerator;
import com.sum.cache.memory.MemoryCache;
import com.sum.cache.memory.impl.LimitedAgeMemoryCache;
import com.sum.cache.util.DiskCacheUtils;
import com.sum.cache.util.MemoryCacheUtils;


/**
 * 缓存配置，涉及到默认的时间，存储大小等
 */
public class CacheConfiguration {

    /**
     * 默认的内存数（单位:个）
     */
    private final static int MAX_MEMORY_SIZE_DEFAULT = 30;

    /**
     * 默认的磁盘缓存大小(单位字节)
     */
    private final static long MAX_DISK_SIZE_DEFAULT = 8 * 1024 * 1024;

    /**
     * 默认文件个数
     */
    private final static int MAX_FILE_COUNT_DEFAULT = 50;

    /**
     * 默认的内存过期时间(单位秒)
     */
    private final static long MAX_LIMIT_MEMORY_TIME_DEFAULT = 5*1000;
    /**
     * 默认的磁盘过期时间(单位秒)
     */
    private final static long MAX_LIMIT_DISK_TIME_DEFAULT = 365 * 24 * 60*1000;

    /**
     * 磁盘缓存类
     */
    private DiskCache diskCache;

    /***
     * 内存缓存类
     */
    private MemoryCache memoryCache;

    /**
     * 缓存内存默认有效期(单位秒)
     */
    private long maxMemoryTime;

    /**
     * 缓存磁盘默认有效期(单位秒)
     */
    private long maxDiskTime;

    //任务执行
    Executor taskExecutor;
    /**
     * 默认缓存存储和读取配置
     */
    public CacheOption cacheOptionDefault;


    /**
     * {@link Builder} 通过builder构建缓存配置
     *
     * @param builder 具体的builder
     */
    public CacheConfiguration(CacheConfiguration.Builder builder) {
        taskExecutor = builder.taskExecutor;
        diskCache = builder.diskCache;
        memoryCache = builder.memoryCache;
        maxMemoryTime = builder.maxMemoryTime;
        maxDiskTime = builder.maxDiskTime;
        cacheOptionDefault = builder.cacheOptionDefault;
    }

    /**
     * 清除缓存框架的基础配置
     */
    public void clear() {
        taskExecutor=null;
        diskCache = null;
        memoryCache = null;
        maxMemoryTime = 0;
        maxDiskTime = 0;
    }

    /***
     * 获取基本类型的磁盘缓存
     */
    public DiskCache getDiskCache() {
        return diskCache;
    }

    /**
     * 获取有缓存有有限期的磁盘缓存
     */
    public DiskCache getLimitAgeDiskCache() {
        return new LimitedAgeDiskCache(diskCache, maxDiskTime);
    }

    /***
     * 设置磁盘缓存
     */
    public void setDiskCache(DiskCache diskCache) {
        this.diskCache = diskCache;
    }

    /***
     * 获取基本类型内存缓存
     */
    public MemoryCache getMemoryCache() {
        return memoryCache;
    }

    /***
     * 获取缓存有过期时间的缓存加载工具
     */
    public MemoryCache getLimitedAgeMemoryCache() {
        return new LimitedAgeMemoryCache(memoryCache, maxMemoryTime);
    }
    public StoragePlainFile getStoragePlainFile() {
        return new KryoStoragePlainFile();
    }

    /***
     * 设置内存缓存
     */
    public void setMemoryCache(MemoryCache memoryCache) {
        this.memoryCache = memoryCache;
    }

    /**
     * 清理 缓存
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



    public static class Builder {
        Context context;
        Executor taskExecutor;
        DiskCache diskCache;
        MemoryCache memoryCache;
        FileNameGenerator diskCacheFileNameGenerator;
        long diskCacheSize;
        //如果是0的话，默认是无穷大
        int diskCacheFileCount;
        int maxMemoryCount;

        private long maxMemoryTime;

        private long maxDiskTime;


        public CacheOption cacheOptionDefault;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置任务执行器
         */
        public Builder taskExecutor(Executor taskExecutor) {
            this.taskExecutor = taskExecutor;
            return this;
        }

        /**
         * 设置磁盘缓存
         */
        public Builder diskCache(DiskCache diskCache) {
            this.diskCache = diskCache;
            return this;
        }

        /**
         * 设置内存缓存
         */
        public Builder memoryCache(MemoryCache memoryCache) {
            this.memoryCache = memoryCache;
            return this;
        }

        /**
         * 设置默认的文件命名方式
         */
        public Builder diskCacheFileNameGenerator(FileNameGenerator diskCacheFileNameGenerator) {
            this.diskCacheFileNameGenerator = diskCacheFileNameGenerator;
            return this;
        }

        /**
         * 设置磁盘缓存大小
         */
        public Builder diskCacheSize(long diskCacheSize) {
            this.diskCacheSize = diskCacheSize;
            return this;
        }

        /**
         * 设置缓存文件数目
         */
        public Builder diskCacheFileCount(int diskCacheFileCount) {
            this.diskCacheFileCount = diskCacheFileCount;
            return this;
        }

        /**
         * 设置内存的最大缓存数据个数
         */
        public Builder maxMemoryCount(int maxMemorySize) {
            this.maxMemoryCount = maxMemorySize;
            return this;
        }

        /**
         * 这是磁盘缓存的最大时间
         */
        public Builder maxDiskTime(long maxDiskTime) {
            this.maxDiskTime = maxDiskTime;
            return this;
        }

        /**
         * 设置内存中缓存的最大时间
         */
        public Builder maxMemoryTime(long maxMemoryTime) {
            this.maxMemoryTime = maxMemoryTime;
            return this;
        }

        public CacheConfiguration build() {
            this.initEmptyFieldsWithDefaultValues();
            return new CacheConfiguration(this);
        }


        private void initEmptyFieldsWithDefaultValues() {
            if (maxDiskTime == 0) {
                maxDiskTime = MAX_LIMIT_DISK_TIME_DEFAULT;
            }
            if (maxMemoryTime == 0) {
                maxMemoryTime = MAX_LIMIT_MEMORY_TIME_DEFAULT;
            }
            if (diskCacheFileNameGenerator == null) {
                diskCacheFileNameGenerator = new HashCodeFileNameGenerator();
            }
            if (diskCacheSize == 0) {
                diskCacheSize = MAX_DISK_SIZE_DEFAULT;
            }
            if (diskCacheFileCount == 0) {
                diskCacheFileCount = MAX_FILE_COUNT_DEFAULT;
            }
            if (maxMemoryCount == 0) {
                maxMemoryCount = MAX_MEMORY_SIZE_DEFAULT;
            }
            if (memoryCache == null) {
                memoryCache = MemoryCacheUtils.createLruMemoryCache(maxMemoryCount);
            }
            if (diskCache == null) {
                diskCache = DiskCacheUtils.createDiskCache(context, diskCacheFileNameGenerator, diskCacheSize, diskCacheFileCount);
            }
            if (taskExecutor == null) {
                taskExecutor = DefaultConfigurationFactory.createTaskDistributor();
            }
            if (cacheOptionDefault == null) {
                cacheOptionDefault = new CacheOption.Builder().build();
            }
        }
    }

}
