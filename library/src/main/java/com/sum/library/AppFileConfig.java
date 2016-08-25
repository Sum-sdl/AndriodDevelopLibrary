package com.sum.library;

import android.os.Environment;

import org.xutils.x;

import java.io.File;

/**
 * Created by Sum on 15/11/22.
 */
public class AppFileConfig {

    public static File getDownloadAppFile() {

        return getDir("app");
    }

    public static File getLogFile() {

        return getDir("log");
    }

    public static File getTakePhoneFile(){
        return getDir("pic");
    }

    public static File getCacheFile(){
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

    public static File getBaseDir() {
        File result;
        if (existsSdcard()) {
            String cacheDir = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/sum";

            result = new File(cacheDir);
        } else {
            result = (x.app().getFilesDir());
        }
        if (result.exists() || result.mkdirs()) {
            return result;
        } else {
            return null;
        }
    }

    public static Boolean existsSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}
