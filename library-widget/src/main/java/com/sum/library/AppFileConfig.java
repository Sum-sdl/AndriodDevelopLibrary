package com.sum.library;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import androidx.core.content.FileProvider;

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

    //根目录是否在系统应用缓存目录创建文件夹
    private static boolean mIsAppDataDir = false;

    /**
     * 不存在sd卡时,全部存放在应用内部file文件夹
     * 应用内部文件夹只能app写入,外部应该不能写入,只能读取
     *
     * @param context                  applicationContext
     * @param appExternalDirectoryName 目录根路径的包名
     * @param isAppDataDir             true:全部创建在Android/Data/files目录 false:全部创建sd卡根目录
     */
    public static void init(Context context, String appExternalDirectoryName, boolean isAppDataDir) {
        if (context == null) {
            throw new RuntimeException("context is null");
        }
        mIsAppDataDir = isAppDataDir;
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
        return getBaseDir();
    }


    /**
     * App_External_Directory_Name
     *
     * @return 根目录->图片目录
     */
    public static File getFileImageDirectory() {
        return getDir("images");
    }

    /**
     * @return 根目录->日志目录
     */
    public static File getFileLogDirectory() {
        return getDir("logs");
    }

    /**
     * App_External_Directory_Name
     *
     * @return 根目录->文件目录
     */
    public static File getFileDirectory() {
        return getDir("file");
    }

    /**
     * App_External_Directory_Name
     *
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
     * @param dirName 自定义目录名
     * @return 创建并返回外部存储App_External_Directory_Name目录下的文件夹
     */
    public static File getDir(String dirName) {
        File baseDir = getBaseDir();
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


    private static File getBaseDir() {
        File result;
        String rootDir = null;
        if (existsSdcard()) {
            if (mIsAppDataDir) {
                File externalFilesDir = mContext.getExternalFilesDir(App_External_Directory_Name);
                if (externalFilesDir != null) {
                    rootDir = externalFilesDir.getAbsolutePath();//系统缓存数据文件夹路劲
                }
            } else {
                //sd 跟目录创建文件夹
                rootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + App_External_Directory_Name;
            }
        } else {
            rootDir = (mContext.getFilesDir()) + File.separator + App_External_Directory_Name;
        }
        try {
            if (TextUtils.isEmpty(rootDir)) {
                return null;
            }
            result = new File(rootDir);
            if (result.exists() || result.mkdirs()) {
                return result;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Boolean existsSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}
