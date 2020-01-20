
package com.sum.cache.disk.impl.ext;


import java.io.File;
import java.io.IOException;

import com.sum.cache.disk.DiskCache;
import com.sum.cache.disk.StoragePlainFile;
import com.sum.cache.disk.impl.CacheTable;
import com.sum.cache.disk.impl.KeyLocker;
import com.sum.cache.disk.naming.FileNameGenerator;

/**
 * Lru算法的磁盘缓存实现
 */
public class LruDiskCache implements DiskCache {

    private static final String ERROR_ARG_NULL = " argument must be not null";
    private static final String ERROR_ARG_NEGATIVE = " argument must be positive number";

    protected DiskLruCache cache;
    private File reserveCacheDir;

    protected final FileNameGenerator fileNameGenerator;
    StoragePlainFile customStoragePlainFile;
    private KeyLocker keyLocker = new KeyLocker(); // To sync key-dependent operations by key

    /**
     * @param cacheDir          Directory for file caching
     * @param fileNameGenerator Name generator} for cached files. Generated names must match
     *                          the regex <strong>[a-z0-9_-]{1,64}</strong>
     * @param cacheMaxSize      Max cache size in bytes. <b>0</b> means cache size is
     *                          unlimited.
     * @throws IOException if cache can't be initialized (e.g.
     *                     "No space left on device")
     */
    public LruDiskCache(File cacheDir, FileNameGenerator fileNameGenerator,
            long cacheMaxSize) throws IOException {
        this(cacheDir, null, fileNameGenerator, cacheMaxSize, 0);
    }

    /**
     * @param cacheDir          Directory for file caching
     * @param reserveCacheDir   null-ok; Reserve directory for file caching. It's used when
     *                          the primary directory isn't available.
     * @param fileNameGenerator Name generator} for cached files. Generated names must match
     *                          the regex <strong>[a-z0-9_-]{1,64}</strong>
     * @param cacheMaxSize      Max cache size in bytes. <b>0</b> means cache size is
     *                          unlimited.
     * @param cacheMaxFileCount Max file count in cache. <b>0</b> means file count is
     *                          unlimited.
     * @throws IOException if cache can't be initialized (e.g.
     *                     "No space left on device")
     */
    public LruDiskCache(File cacheDir, File reserveCacheDir,
            FileNameGenerator fileNameGenerator, long cacheMaxSize,
            int cacheMaxFileCount) throws IOException {
        if (cacheDir == null) {
            throw new IllegalArgumentException("cacheDir" + ERROR_ARG_NULL);
        }
        if (cacheMaxSize < 0) {
            throw new IllegalArgumentException("cacheMaxSize"
                    + ERROR_ARG_NEGATIVE);
        }
        if (cacheMaxFileCount < 0) {
            throw new IllegalArgumentException("cacheMaxFileCount"
                    + ERROR_ARG_NEGATIVE);
        }
        if (fileNameGenerator == null) {
            throw new IllegalArgumentException("fileNameGenerator"
                    + ERROR_ARG_NULL);
        }

        if (cacheMaxSize == 0) {
            cacheMaxSize = Long.MAX_VALUE;
        }
        if (cacheMaxFileCount == 0) {
            cacheMaxFileCount = Integer.MAX_VALUE;
        }

        this.reserveCacheDir = reserveCacheDir;
        this.fileNameGenerator = fileNameGenerator;
        initCache(cacheDir, reserveCacheDir, cacheMaxSize, cacheMaxFileCount);
    }



    private void initCache(File cacheDir, File reserveCacheDir,
            long cacheMaxSize, int cacheMaxFileCount) throws IOException {
        try {
            cache = DiskLruCache.open(cacheDir, 1, 1, cacheMaxSize,
                    cacheMaxFileCount);
        } catch (IOException e) {
            if (reserveCacheDir != null) {
                initCache(reserveCacheDir, null, cacheMaxSize,
                        cacheMaxFileCount);
            }
            if (cache == null) {
                throw e; // new RuntimeException("Can't initialize disk cache",
                // e);
            }
        }
    }

    @Override
    public long size() {
        return cache.size();
    }

    @Override
    public File getDirectory() {
        return cache.getDirectory();
    }

    @Override
    public File getFile(String key) {
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = cache.get(getKey(key));
            return snapshot == null ? null : snapshot.getFile(0);
        } catch (IOException e) {
            return null;
        } finally {
            if (snapshot != null) {
                snapshot.close();
            }
        }
    }
    @Override
    public <V> V get(String key) {
        try {
            keyLocker.acquire(key);
            File originalFile = getFile(key);
            if (originalFile == null || !originalFile.exists()) {
                return null;
            }
            return customStoragePlainFile.readTableFile(key, originalFile);
        } finally {
            keyLocker.release(key);
        }
    }

    @Override
    public <V> boolean put(String key, V value) throws IOException {
        boolean savedSuccessfully = false;
        try {
            keyLocker.acquire(key);
            DiskLruCache.Editor editor = cache.edit(getKey(key));
            if (editor == null) {
                return false;
            }
            final CacheTable<V> paperTable = new CacheTable<>(value);
            savedSuccessfully = customStoragePlainFile.writeTableFile(key, paperTable, editor.newOutputStream(0));
            if (savedSuccessfully) {
                editor.commit();
            } else {
                editor.abort();
            }
        } finally {
            keyLocker.release(key);
        }
        return savedSuccessfully;
    }

    @Override
    public <V> boolean put(String key, V value, long maxLimitTime) throws IOException {
        return put(key, value);
    }

    @Override
    public boolean remove(String key) {
        try {
            return cache.remove(getKey(key));
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void close() {
        try {
            cache.close();
        } catch (IOException e) {
        }

    }

    @Override
    public void clear() {
        try {
            cache.delete();
        } catch (IOException e) {
        }
        try {
            initCache(cache.getDirectory(), reserveCacheDir,
                    cache.getMaxSize(), cache.getMaxFileCount());
        } catch (IOException e) {
        }
    }

    @Override
    public void setStoragePlainFile(StoragePlainFile customSerializers) {
        this.customStoragePlainFile = customSerializers;
    }


    private String getKey(String key) {
        return fileNameGenerator.generate(key);
    }

}
