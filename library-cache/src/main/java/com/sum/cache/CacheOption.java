package com.sum.cache;

/**
 * <p>保存数据相关配置，譬如是否保存内存，磁盘以及保存时间等
 * 默认配置不保存到内存，保存到磁盘，不同步运行。
 */
public class CacheOption {
    //是否缓存到磁盘
    private boolean isCacheDisk;
    //是否缓存到内存
    private boolean isCacheMemory;

    //是否同步运行
    private boolean isSyncRun;

    //磁盘缓存时间 （单位秒）
    private long cacheDiskTime;

    //内存缓存时间 （单位秒）
    private long cacheMemoryTime;

    public CacheOption(Builder builder) {
        isCacheDisk = builder.isCacheDisk;
        isCacheMemory = builder.isCacheMemory;
        isSyncRun = builder.isSyncRun;
        cacheDiskTime = builder.cacheDiskTime;
        cacheMemoryTime = builder.cacheMemoryTime;
    }

    public boolean isCacheDisk() {
        return isCacheDisk;
    }

    public void cacheDisk(boolean cacheDisk) {
        isCacheDisk = cacheDisk;
    }

    public boolean isCacheMemory() {
        return isCacheMemory;
    }

    public void setCacheMemory(boolean cacheMemory) {
        isCacheMemory = cacheMemory;
    }

    public boolean isSyncRun() {
        return isSyncRun;
    }

    public void syncRun(boolean syncRun) {
        isSyncRun = syncRun;
    }

    public long cacheDiskTime() {
        return cacheDiskTime;
    }

    public void cacheDiskTime(long cacheDiskTime) {
        this.cacheDiskTime = cacheDiskTime;
    }

    public long cacheMemoryTime() {
        return cacheMemoryTime;
    }

    public void setCacheMemoryTime(long cacheMemoryTime) {
        this.cacheMemoryTime = cacheMemoryTime;
    }

    public static class Builder {
        //是否缓存到磁盘
        private boolean isCacheDisk =true;
        //是否缓存到内存
        private boolean isCacheMemory =false;

        //是否同步运行
        private boolean isSyncRun =false;

        //磁盘缓存时间 （单位秒）
        private long cacheDiskTime;

        //内存缓存时间 （单位秒）
        private long cacheMemoryTime;

        public Builder cacheDisk(boolean cacheDisk) {
            isCacheDisk = cacheDisk;
            return this;
        }

        public Builder cacheMemory(boolean cacheMemory) {
            isCacheMemory = cacheMemory;
            return this;
        }

        public Builder isSyncRun(boolean syncRun) {
            isSyncRun = syncRun;
            return this;
        }

        public Builder cacheDiskTime(long cacheDiskTime) {
            this.cacheDiskTime = cacheDiskTime;
            return this;
        }

        public Builder cacheMemoryTime(long cacheMemoryTime) {
            this.cacheMemoryTime = cacheMemoryTime;
            return this;
        }

        public CacheOption build() {
            return new CacheOption(this);
        }


    }
}
