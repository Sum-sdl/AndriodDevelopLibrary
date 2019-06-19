package com.sum.library;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by Sum on 15/11/22.
 * app 文件夹创建
 */
public class AppFileConfig {

    //外部根文件夹名称
    public static String App_External_Directory_Name = "A_SumApp_File";

    //FileProvider使用的Uri文件
    public static String FOLDER_PROVIDER = ".fileProvider";

    private static Context mContext;

    public static void init(Context context) {
        mContext = context.getApplicationContext();
    }

    //FileProvider 获取app文件的Uri
    public static Uri getAppSelfUri(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, context.getPackageName() + FOLDER_PROVIDER, file);
        } else {
            return Uri.fromFile(file);
        }
    }

    public static File getAppStoreDirectory() {
        return getBaseDir(false);
    }

    public static File getFileDirectory() {
        return getDir("file");
    }

    public static File getFileImageDirectory() {
        return getDir("images");
    }

    public static File getFileDirectoryCompress() {
        File result = new File(getFileDirectory() + File.separator + "compress");
        if (result.exists() || result.mkdirs()) {
            return result;
        } else {
            return result;
        }
    }

    //任意临时缓存文件夹
    public static File getCacheDirectoryFile() {
        return getDir("cache", true);
    }

    public static File getDir(String dirName) {
        return getDir(dirName, false);
    }

    //创建项目目录下的文件夹
    public static File getDir(String dirName, boolean isAppCacheFile) {
        File baseDir = getBaseDir(isAppCacheFile);
        if (baseDir == null) {
            return null;
        }
        File result = new File(baseDir.getPath() + File.separator + dirName);
        if (result.exists() || result.mkdirs()) {
            return result;
        } else {
            return null;
        }
    }

    private static File getBaseDir(boolean isCacheFile) {
        File result;
        if (existsSdcard()) {
            String cacheDir = null;
            if (isCacheFile) {
                File file = mContext.getExternalCacheDir();//系统缓存数据文件夹路劲
                if (file != null) {
                    cacheDir = file.getAbsolutePath();
                }
            }
            if (TextUtils.isEmpty(cacheDir)) {
                cacheDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + App_External_Directory_Name;
            }
            result = new File(cacheDir);
        } else {
            if (isCacheFile) {
                result = (mContext.getCacheDir());
            } else {
                result = (mContext.getFilesDir());
            }
        }
        if (result.exists() || result.mkdirs()) {
            return result;
        } else {
            return null;
        }
    }

    private static Boolean existsSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}
