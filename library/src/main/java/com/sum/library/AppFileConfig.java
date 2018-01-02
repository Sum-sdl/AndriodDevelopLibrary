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
    public static String FOLDER_NAME = "A_Sum";

    //FileProvider 获取app文件的Uri
    public static Uri getAppSelfUri(Context context, File file, String expand) {
        if (TextUtils.isEmpty(expand)) {
            expand = ".fileProvider";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, context.getPackageName() + expand, file);
        } else {
            return (Uri.fromFile(file));
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
        return getDir("cache");
    }

    private static File getDir(String dirName) {
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
        if (existsSdcard()) {
            String cacheDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + FOLDER_NAME;

            result = new File(cacheDir);
        } else {
            result = (Utils.getApp().getFilesDir());
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
