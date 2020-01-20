
package com.sum.cache.disk.impl;


import java.io.File;
import java.io.IOException;

import com.sum.cache.disk.DiskCache;
import com.sum.cache.disk.StoragePlainFile;
import com.sum.cache.disk.naming.FileNameGenerator;
import com.sum.cache.util.CacheLog;

/**
 * Base disk cache.
 *
 * @see FileNameGenerator
 * @since 1.0.0
 */
public class BaseDiskCache implements DiskCache {
    private static final String ERROR_ARG_NULL = " argument must be not null";
    private static final String TEMP_key_POSTFIX = ".tmp";

    protected final File cacheDir;
    protected final File reserveCacheDir;

    private KeyLocker keyLocker = new KeyLocker(); // To sync key-dependent operations by key
    protected final FileNameGenerator fileNameGenerator;
    StoragePlainFile customStoragePlainFile;

    /**
     * @param cacheDir          Directory for file caching
     * @param reserveCacheDir   null-ok; Reserve directory for file caching. It's used when
     *                          the primary directory isn't available.
     * @param fileNameGenerator Name generator} for cached files
     */
    public BaseDiskCache(File cacheDir, File reserveCacheDir,
            FileNameGenerator fileNameGenerator) {
        if (cacheDir == null) {
            throw new IllegalArgumentException("cacheDir" + ERROR_ARG_NULL);
        }
        if (fileNameGenerator == null) {
            throw new IllegalArgumentException("fileNameGenerator"
                    + ERROR_ARG_NULL);
        }
        this.cacheDir = cacheDir;
        this.reserveCacheDir = reserveCacheDir;
        this.fileNameGenerator = fileNameGenerator;
    }

    @Override
    public long size() {
        return getDirSize(getDirectory());
    }

    /**
     * 获取Dir大小
     *
     * @return long
     * @see [类、类#方法、类#成员]
     */
    private long getDirSize(File file) {
        //判断文件是否存在     
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小    
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                long size = 0;
                for (File f : children) {
                    size += getDirSize(f);
                }
                return size;
            } else {//如果是文件则直接返回其大小,以byte为单位   
                long size = file.length();
                return size;
            }
        } else {
            CacheLog.e("The file is not exist,please check the file path");
            return 0;
        }
    }

    @Override
    public void setStoragePlainFile(StoragePlainFile customSerializers) {
        this.customStoragePlainFile = customSerializers;
    }

    @Override
    public File getDirectory() {
        return cacheDir;
    }

    @Override
    public File getFile(String key) {
        String fileName = fileNameGenerator.generate(key);
        File dir = cacheDir;
        if (!cacheDir.exists() && !cacheDir.mkdirs()) {
            if (reserveCacheDir != null
                    && (reserveCacheDir.exists() || reserveCacheDir.mkdirs())) {
                dir = reserveCacheDir;
            }
        }
        return new File(dir, fileName);
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
    public <V> boolean put(String key, V value)
            throws IOException {
        boolean savedSuccessfully = false;
        try {
            keyLocker.acquire(key);
            final CacheTable<V> paperTable = new CacheTable<>(value);
            final File originalFile = getFile(key);
            final File backupFile = new File(originalFile.getAbsolutePath() + TEMP_key_POSTFIX);
            // Rename the current file so it may be used as a backup during the next read
            if (originalFile.exists()) {
                //Rename original to backup
                if (!backupFile.exists()) {
                    if (!originalFile.renameTo(backupFile)) {
                        CacheLog.e("Couldn't rename file " + originalFile
                                + " to backup file " + backupFile);
                        return false;
                    }
                } else {
                    //Backup exist -> original file is broken and must be deleted
                    //noinspection ResultOfMethodCallIgnored
                    originalFile.delete();
                }
            }
            savedSuccessfully = customStoragePlainFile.writeTableFile(key, paperTable, originalFile,
                    backupFile);

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
        return getFile(key).delete();
    }

    @Override
    public void close() {
        // Nothing to do
    }

    @Override
    public void clear() {
        File[] files = cacheDir.listFiles();
        if (files != null) {
            for (File f : files) {
                f.delete();
            }
        }
    }

}