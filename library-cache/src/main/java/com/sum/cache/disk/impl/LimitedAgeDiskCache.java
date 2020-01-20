/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.sum.cache.disk.impl;


import java.io.File;
import java.io.IOException;

import com.sum.cache.LimitedAge;
import com.sum.cache.disk.DiskCache;
import com.sum.cache.disk.StoragePlainFile;
import com.sum.cache.entity.CacheEntity;

/**
 * 文件有过期时间的磁盘缓存实现
 */
public class LimitedAgeDiskCache implements DiskCache {
    private DiskCache mDiskCache;
    private long maxAge;

    /**
     * @param diskCache 磁盘缓存类
     * @param maxAge    缓存数据最大期限(单位秒)
     */
    public LimitedAgeDiskCache(DiskCache diskCache, long maxAge) {
        this.mDiskCache = diskCache;
        this.maxAge = maxAge;
    }

    @Override
    public File getDirectory() {
        if (mDiskCache == null) {
            return null;
        }
        return mDiskCache.getDirectory();
    }

    @Override
    public File getFile(String key) {
        if (mDiskCache == null) {
            return null;
        }
        return mDiskCache.getFile(key);
    }

    @Override
    public <V> V get(String key) {
        if (mDiskCache == null) {
            return null;
        }
        File file = getFile(key);
        if (file == null || !file.exists()) {
            return null;
        }
        CacheEntity<V> cacheEntity = mDiskCache.get(key);
        if (cacheEntity == null  || checkDataLimitedAndReset(key, cacheEntity.getLimitedAge())) {
            return null;
        }
        return cacheEntity.getValue();
    }


    public boolean checkDataLimitedAndReset(String key, LimitedAge limitedAge) {
        boolean result;
        LimitedAge loadingDate = limitedAge;
        if (loadingDate == null) {
            result = true;
        } else {
            result = loadingDate.checkExpire();
        }
        if (result) {
            mDiskCache.remove(key);
            File file = getFile(key);
            if (file != null && file.exists()) {
                file.delete();
            }
        }

        return result;
    }

    @Override
    public <V> boolean put(String key, V value)
            throws IOException {
        if (mDiskCache == null) {
            return false;
        }
        CacheEntity<V> cacheEntity = new CacheEntity<>(
                new LimitedAge(System.currentTimeMillis(), maxAge), value);
        return mDiskCache.put(key, cacheEntity);
    }

    @Override
    public <V> boolean put(String key, V value, long limitTime) throws IOException {
        if (mDiskCache == null) {
            return false;
        }
        CacheEntity<V> cacheEntity = new CacheEntity<>(
                new LimitedAge(System.currentTimeMillis(), limitTime), value);

        return mDiskCache.put(key, cacheEntity);
    }




    @Override
    public long size() {
        if (mDiskCache == null) {
            return 0;
        }
        return mDiskCache.size();
    }

    @Override
    public boolean remove(String key) {
        if (mDiskCache == null) {
            return false;
        }

        return mDiskCache.remove(key);
    }

    @Override
    public void clear() {
        if (mDiskCache != null) {
            mDiskCache.clear();
        }

    }

    @Override
    public void setStoragePlainFile(StoragePlainFile customSerializers) {
        mDiskCache.setStoragePlainFile(customSerializers);
    }


    @Override
    public void close() {
        if (mDiskCache != null) {
            mDiskCache.close();
        }

    }




    /**
     * 获取数据剩余有效时间(单位秒)
     *
     * @return long
     * @see [类、类#方法、类#成员]
     */
    public long getLimitedTime(String key) {
        CacheEntity cacheEntity = mDiskCache.get(key);
        if (cacheEntity != null ) {
            LimitedAge limitedAge = cacheEntity.getLimitedAge();
            if (limitedAge != null) {
                return limitedAge.limitedTime();
            }
        }
        return 0;
    }

}