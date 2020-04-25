package com.sum.library.storage;

import android.net.Uri;

import java.io.File;

/**
 * Created by sdl on 2020/4/13
 * 文件访问接口
 */
interface IStorageFile {

    /**
     * @return 返回App文件存储跟路径
     */
    File getStorageRootDir();

    /**
     * @param dirName 根目录下的文件夹
     * @return 根目录下的文件路径
     */
    File getStorageDir(String dirName);


    /**
     * @param file App存储的文件路径
     * @return 返回存储路径对应的Uri地址, 用于第三方应用访问
     */
    Uri getStorageFileUri(File file);
}
