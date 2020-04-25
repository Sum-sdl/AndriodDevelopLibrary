package com.sum.library.storage;

import android.net.Uri;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by sdl on 2020/4/13
 * App 文件存储入口
 */
public class AppFileStorage {

    private AppFileStorage() {
    }

    //主要的文件操作路径
    private static IStorageFile mStorageFile;

    /**
     * 必须要先初始化存储配置
     *
     * @param config 文件初始化配置
     */
    public static void init(StorageConfig config) {
        if (config == null) {
            throw new RuntimeException("file config is null");
        }
        if (TextUtils.isEmpty(config.getBaseDirName())) {
            throw new RuntimeException("base dir name is null");
        }
        mStorageFile = new StorageFileImpl(config);
    }


    /**
     * @return 根目录下的文件路径
     */
    public static File getStorageRootDir() {
        return mStorageFile.getStorageRootDir();
    }

    /**
     * @param dirName 根目录下的文件夹
     * @return 根目录下的文件路径
     */
    public static File getStorageDir(String dirName) {
        return mStorageFile.getStorageDir(dirName);
    }

    /**
     * @param file 存储在storageRootDir目录下的文件
     * @return 返回存储路径对应的Uri地址, 用于第三方应用访问
     */
    public static Uri getStorageFileUri(File file) {
        return mStorageFile.getStorageFileUri(file);
    }

    /**
     * @return 默认根目录下file文件夹
     */
    public static File getStorageFileDir() {
        return getStorageDir("file");
    }

    /**
     * 默认根目录下file文件夹的子路径
     *
     * @param dirName file文件夹下的子目录名称
     * @return 根目录/file/dirName
     */
    public static File getStorageFileChildDir(String dirName) {
        File result = new File(getStorageFileDir(), dirName);
        if (result.exists() || result.mkdirs()) {
            return result;
        } else {
            return result;
        }
    }

    /**
     * @return 默认根目录下image文件夹
     */
    public static File getStorageImagesDir() {
        return getStorageDir("images");
    }

}
