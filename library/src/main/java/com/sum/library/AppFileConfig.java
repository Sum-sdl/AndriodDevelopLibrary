package com.sum.library;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.blankj.utilcode.util.Utils;

import java.io.File;

/**
 * Created by Sum on 15/11/22.
 * app 文件夹创建
 */
public class AppFileConfig {

    //配置文件路劲
    public static String FOLDER_NAME = "A_File";

    //FileProvider使用的Uri文件
    public static String FOLDER_PROVIDER = ".fileProvider";

    //FileProvider 获取app文件的Uri
    public static Uri getAppSelfUri(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, context.getPackageName() + FOLDER_PROVIDER, file);
        } else {
            return Uri.fromFile(file);
        }
    }

    public static File getDownloadDirectoryFile() {
        return getDir("file");
    }

    public static File getLogDirectoryFile() {
        return getDir("logs");
    }

    public static File getImageDirectoryFile() {
        return getDir("image");
    }

    public static File getCacheDirectoryFile() {
        return getDir("cache", true);
    }

    private static File getDir(String dirName) {
        return getDir(dirName, false);
    }

    private static File getDir(String dirName, boolean isCacheFile) {
        File baseDir = getBaseDir(isCacheFile);
        if (baseDir == null) {
            return null;
        }
//        Logger.e("baseFile:" + baseDir.getAbsolutePath());
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
                File file = Utils.getApp().getExternalCacheDir();
                if (file != null) {
                    cacheDir = file.getAbsolutePath();
                }
            }
            if (TextUtils.isEmpty(cacheDir)) {
                cacheDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + FOLDER_NAME;
            }
            result = new File(cacheDir);
        } else {
            if (isCacheFile) {
                result = (Utils.getApp().getCacheDir());
            } else {
                result = (Utils.getApp().getFilesDir());
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
