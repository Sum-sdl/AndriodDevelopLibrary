package com.sum.library.storage;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.core.content.FileProvider;

import java.io.File;

/**
 * Created by sdl on 2020/4/13
 * App文件路径存储实现
 */
class StorageFileImpl implements IStorageFile {

    //基础配置
    private StorageConfig config;
    private Context context;

    StorageFileImpl(StorageConfig config) {
        this.config = config;
        this.context = config.getApplication();
    }

    @Override
    public File getStorageRootDir() {
        return getBaseDir();
    }

    @Override
    public File getStorageDir(String dirName) {
        return createDir(dirName);
    }

    @Override
    public Uri getStorageFileUri(File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, config.getCustomFileProvider(), file);
        } else {
            return Uri.fromFile(file);
        }
    }

    //在根目录下生成一个文件夹
    private File createDir(String dirName) {
        File baseDir = getBaseDir();
        if (baseDir == null) {
            return null;
        }
        File result = new File(baseDir.getPath(), dirName);
        if (result.exists() || result.mkdirs()) {
            return result;
        } else {
            return null;
        }
    }

    //文件跟目录
    private File getBaseDir() {
        File baseFile;
        if (existsSdcard() && config.isAppDataDir()) {
            baseFile = context.getExternalFilesDir(config.getBaseDirName());
        } else {
            baseFile = new File(context.getFilesDir(), config.getBaseDirName());
        }
        if (baseFile == null) {
            return null;
        }
        if (baseFile.exists() || baseFile.mkdirs()) {
            return baseFile;
        } else {
            return null;
        }
    }

    private Boolean existsSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}
