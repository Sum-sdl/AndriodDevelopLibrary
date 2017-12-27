package com.sum.library.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by sdl on 2017/12/27.
 */

public class AppUtils {
    /**
     * 通知到系统上下文中
     */
    public static void displayToGallery(Context context, File photoFile) {
        if (photoFile == null || !photoFile.exists()) {
            return;
        }
        String photoPath = photoFile.getAbsolutePath();
        String photoName = photoFile.getName();
        // 其次把文件插入到系统图库
        try {
            ContentResolver contentResolver = context.getContentResolver();
            MediaStore.Images.Media.insertImage(contentResolver, photoPath, photoName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + photoPath)));
    }


    // default 500
    public static byte[] compressImage(Bitmap image) {
        return compressImage(image, 500);
    }

    public static byte[] compressImage(Bitmap image, int imageSize) {
        if (image == null) {
            return null;
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            int options = 90;
            while (baos.toByteArray().length / 1024 > imageSize) {
                baos.reset();
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);
                options -= 10;
            }
            return baos.toByteArray();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return null;
    }
}
