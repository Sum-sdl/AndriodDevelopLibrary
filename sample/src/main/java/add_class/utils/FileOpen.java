package add_class.utils;

import android.content.Intent;
import android.net.Uri;

import com.blankj.utilcode.util.Utils;
import com.sum.library.AppFileConfig;

import java.io.File;
import java.util.Locale;

/**
 * Created by sdl on 2018/8/29.
 */
public class FileOpen {

    public static Intent getOpenFileIntent(String filePath) {

        File file = new File(filePath);
        if (!file.exists())
            return null;
        /* 取得扩展名 */
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase(Locale.getDefault());
        /* 依扩展名的类型决定MimeType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            return getAudioFileIntent(filePath);
        } else if (end.equals("3gp") || end.equals("mp4")) {
            return getVideoFileIntent(filePath);
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
            return getImageFileIntent(filePath);
        } else if (end.equals("apk")) {
            return getApkFileIntent(filePath);
        } else if (end.contains("ppt")) {
            return getPptFileIntent(filePath);
        } else if (end.contains("xls")) {
            return getExcelFileIntent(filePath);
        } else if (end.contains("doc")) {
            return getWordFileIntent(filePath);
        } else if (end.contains("pdf")) {
            return getPdfFileIntent(filePath);
        } else if (end.contains("chm")) {
            return getChmFileIntent(filePath);
        } else if (end.equals("txt")) {
            return getTextFileIntent(filePath);
        } else {
            return getAllIntent(filePath);
        }
    }

    // Android获取一个用于打开APK文件的intent
    private static Intent getAllIntent(String param) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        dealIntent(intent);
        Uri uri = getUri(param);
        intent.setDataAndType(uri, "*/*");
        return intent;
    }

    // Android获取一个用于打开APK文件的intent
    private static Intent getApkFileIntent(String param) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        dealIntent(intent);
        Uri uri = getUri(param);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    // Android获取一个用于打开VIDEO文件的intent
    private static Intent getVideoFileIntent(String param) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Uri uri = getUri(param);
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    // Android获取一个用于打开AUDIO文件的intent
    private static Intent getAudioFileIntent(String param) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Uri uri = getUri(param);
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    // Android获取一个用于打开图片文件的intent
    private static Intent getImageFileIntent(String param) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        dealIntent(intent);
        Uri uri = getUri(param);
        intent.setDataAndType(uri, "image/*");
        return intent;
    }
    // Android获取一个用于打开PPT文件的intent
    private static Intent getPptFileIntent(String param) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        dealIntent(intent);
        Uri uri = getUri(param);
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    // Android获取一个用于打开Excel文件的intent
    private static Intent getExcelFileIntent(String param) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        dealIntent(intent);
        Uri uri = getUri(param);
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    // Android获取一个用于打开Word文件的intent
    private static Intent getWordFileIntent(String param) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        dealIntent(intent);
        Uri uri = getUri(param);
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    // Android获取一个用于打开CHM文件的intent
    private static Intent getChmFileIntent(String param) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        dealIntent(intent);
        Uri uri = getUri(param);
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    // Android获取一个用于打开文本文件的intent
    private static Intent getTextFileIntent(String param) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        dealIntent(intent);
        Uri uri = getUri(param);
        intent.setDataAndType(uri, "text/plain");
        return intent;
    }

    // Android获取一个用于打开PDF文件的intent
    private static Intent getPdfFileIntent(String param) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        dealIntent(intent);
        Uri uri = getUri(param);
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }


    private static void dealIntent(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addCategory("android.intent.category.DEFAULT");
    }



    private static Uri getUri(String param) {
        return AppFileConfig.getAppSelfUri(Utils.getApp(), new File(param));
    }

}
