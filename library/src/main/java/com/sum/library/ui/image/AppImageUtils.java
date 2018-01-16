package com.sum.library.ui.image;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.blankj.utilcode.util.Utils;
import com.sum.library.AppFileConfig;
import com.sum.library.R;
import com.sum.library.ui.image.photoAlbum.AlbumInfo;
import com.sum.library.ui.image.photoAlbum.PhotoAlbumActivity;
import com.sum.library.ui.image.preview.ImagePreviewActivity;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by sdl on 2018/1/12.
 * 图片界面使用
 */

public class AppImageUtils {

    /**
     * 选择调用系统相册
     */
    public static void systemChooseImage(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 调用系统相册返回图片路劲
     */
    public static String systemChooseImageIntentImagePath(Intent intent) {
        if (intent == null) {
            return null;
        }
        Uri uri = intent.getData();
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String img_path = null;
        if (null == scheme) {
            img_path = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            img_path = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = Utils.getApp().getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        img_path = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return img_path;
    }


    /**
     * 选择调用系统相机
     */
    public static void systemTakePhoto(Activity activity, int requestCode, Uri targetUri) {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, targetUri);
        activity.startActivityForResult(captureIntent, requestCode);
    }

    public static File systemTakePhoto(Activity activity, int requestCode) {
        String target = AppFileConfig.getImageDirectoryFile().getPath() + File.separator + System.currentTimeMillis() + ".jpg";
        File file = new File(target);
        systemTakePhoto(activity, requestCode, AppFileConfig.getAppSelfUri(activity, file));
        return file;
    }


    /**
     * 刷新系统相册
     */
    public static void appRefreshAlbum(String newFile) {
        if (TextUtils.isEmpty(newFile)) {
            return;
        }
        File target = new File(newFile);
        //刷新文件夹
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            Intent scan_dir = new Intent(Intent.ACTION_MEDIA_MOUNTED);
            scan_dir.setData(Uri.fromFile(target.getParentFile()));
            Utils.getApp().sendBroadcast(scan_dir);
        }
        //刷新文件
        Intent intent_scan = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent_scan.setData(Uri.fromFile(target));
        Utils.getApp().sendBroadcast(intent_scan);
    }


    /**
     * 图片预览界面
     */
    public static void appImagePreview(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        ArrayList<String> list = new ArrayList<>();
        list.add(url);
        appImagePreview(context, list);
    }

    public static void appImagePreview(Context context, ArrayList<String> urls) {
        ImagePreviewActivity.Companion.open(context, urls);
    }


    /**
     * 图片剪裁
     */
    public static void appImageCrop(Activity activity, String sourcePath, int requestCode, float ratio, UCrop.Options option) {
        String destinationPath = AppFileConfig.getImageDirectoryFile() + File.separator + System.currentTimeMillis() + ".jpg";
        UCrop uCrop = UCrop.of(Uri.fromFile(new File(sourcePath)), Uri.fromFile(new File(destinationPath)));
        UCrop.Options options = new UCrop.Options();
        if (ratio != 0) {
            options.withAspectRatio(ratio, 1);
        } else {
            options.setFreeStyleCropEnabled(true);
        }
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
        options.setToolbarColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        options.setCompressionQuality(80);
        options.setHideBottomControls(true);
        uCrop.withOptions(options);
        if (option != null) {
            uCrop.withOptions(option);
        }
        uCrop.start(activity, requestCode);
    }

    public static void appImageCrop(Activity activity, String sourcePath, int requestCode) {
        appImageCrop(activity, sourcePath, requestCode, 0, null);
    }

    public static void appImageCrop(Activity activity, String sourcePath, int requestCode, UCrop.Options option) {
        appImageCrop(activity, sourcePath, requestCode, 0, option);
    }

    /**
     * 图片剪裁返回路劲
     */
    public static String appImageCropIntentPath(Intent intent) {
        if (intent == null) {
            return null;
        }
        Uri uri = UCrop.getOutput(intent);
        if (uri != null) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * 自定义多选相册
     */
    public static void appImageAlbum(Activity activity, int requestCode, int maxNum) {
        AlbumInfo info = new AlbumInfo();
        info.max_count = maxNum;
        info.request_code = requestCode;
        PhotoAlbumActivity.Companion.open(activity, info);
    }

    public static void appImageAlbum(Activity activity, AlbumInfo info) {
        PhotoAlbumActivity.Companion.open(activity, info);
    }
}
