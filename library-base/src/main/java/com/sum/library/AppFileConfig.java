package com.sum.library;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import androidx.core.content.FileProvider;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by Sum on 15/11/22.
 * app 文件夹创建
 */
public class AppFileConfig {

    //外部根文件夹名称
    private static String App_External_Directory_Name = "A_SumApp_File";

    //FileProvider使用的Uri文件
    public static String FOLDER_PROVIDER = ".fileProvider";

    private static Context mContext;

    public static void init(Context context, String appExternalDirectoryName) {
        if (context == null) {
            throw new RuntimeException("context is null");
        }
        if (!TextUtils.isEmpty(appExternalDirectoryName)) {
            App_External_Directory_Name = appExternalDirectoryName;
        }
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

    /**
     * @return 项目存储的根目录
     */
    public static File getAppStoreDirectory() {
        return getBaseDir(false);
    }

    /**
     * @param dirName 自定义目录名
     * @return 创建并返回外部存储App_External_Directory_Name目录下的文件夹
     */
    public static File getDir(String dirName) {
        return getDir(dirName, false);
    }

    /**
     * @return 根目录->图片目录
     */
    public static File getFileImageDirectory() {
        return getDir("images");
    }

    /**
     * @return 根目录->文件目录
     */
    public static File getFileDirectory() {
        return getDir("file");
    }

    /**
     * @return 根目录->文件目录->压缩目录
     */
    public static File getFileCompressDirectory() {
        File result = new File(getFileDirectory() + File.separator + "compress");
        if (result.exists() || result.mkdirs()) {
            return result;
        } else {
            return result;
        }
    }

    /**
     * @return 系统统一缓存目录cache/image
     */
    public static File getAppCacheImageDirectory() {
        return getDir("image", true);
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
