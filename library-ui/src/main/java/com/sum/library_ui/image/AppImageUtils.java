package com.sum.library_ui.image;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;

import com.sum.library.AppFileConfig;
import com.sum.library.R;
import com.sum.library_ui.image.photoAlbum.AlbumInfo;
import com.sum.library_ui.image.photoAlbum.PhotoAlbumActivity;
import com.sum.library_ui.image.preview.ImagePreviewActivity;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by sdl on 2018/1/12.
 * 图片界面使用
 */

public class AppImageUtils {

    /**
     * 选择调用系统相册
     */
    public static void systemChooseImage(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 调用系统相册返回图片路劲
     */
    public static String systemChooseImageIntentImagePath(Context context, Intent intent) {
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
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
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

    /**
     * @return 拍照完成时，返回拍照的文件位置
     */
    public static File systemTakePhoto(Activity activity, int requestCode) {
        String target = AppFileConfig.getFileImageDirectory().getPath() + File.separator + System.currentTimeMillis() + ".jpg";
        File file = new File(target);
        systemTakePhoto(activity, requestCode, AppFileConfig.getAppSelfUri(activity, file));
        return file;
    }


    /**
     * 刷新系统相册
     */
    @SuppressLint("ObsoleteSdkInt")
    public static void appRefreshAlbum(Context context, String newFile) {
        if (TextUtils.isEmpty(newFile)) {
            return;
        }
        File target = new File(newFile);
        //刷新文件夹
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            Intent scan_dir = new Intent(Intent.ACTION_MEDIA_MOUNTED);
            scan_dir.setData(Uri.fromFile(target.getParentFile()));
            context.sendBroadcast(scan_dir);
        }
        //刷新文件
        Intent intent_scan = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent_scan.setData(Uri.fromFile(target));
        context.sendBroadcast(intent_scan);
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
        String destinationPath = AppFileConfig.getFileImageDirectory() + File.separator + System.currentTimeMillis() + ".jpg";
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
    public static void appImageAlbum(Activity activity, int maxNum) {
        AlbumInfo info = new AlbumInfo();
        info.max_count = maxNum;
        appImageAlbum(activity, info);
    }

    public static void appImageAlbum(Fragment activity, int maxNum) {
        AlbumInfo info = new AlbumInfo();
        info.max_count = maxNum;
        appImageAlbum(activity, info);
    }

    public static void appImageAlbum(Activity activity, int maxNum, boolean showTakePhoto) {
        AlbumInfo info = new AlbumInfo();
        info.max_count = maxNum;
        info.take_photo_open = showTakePhoto;
        appImageAlbum(activity, info);
    }

    public static void appImageAlbum(Fragment activity, int maxNum, boolean showTakePhoto) {
        AlbumInfo info = new AlbumInfo();
        info.max_count = maxNum;
        info.take_photo_open = showTakePhoto;
        appImageAlbum(activity, info);
    }

    public static void appImageAlbum(Activity activity, AlbumInfo info) {
        PhotoAlbumActivity.Companion.open(activity, info);
    }

    public static void appImageAlbum(Fragment activity, AlbumInfo info) {
        PhotoAlbumActivity.Companion.open(activity, info);
    }


    /**
     * 鲁班压缩图片
     *
     * @param target           目标文件
     * @param compressListener 压缩回调
     */
    public static void LuImageCompress(Context context, String target, OnCompressListener compressListener) {
        Luban.with(context)
                .load(target)
                .ignoreBy(100)
                .setTargetDir(AppFileConfig.getFileCompressDirectory().getPath())
                .setCompressListener(compressListener).launch();
    }

    public static void LuImageCompress(Context context, List<String> target, OnCompressListener compressListener) {
        Luban.with(context)
                .load(target)
                .ignoreBy(100)
                .setTargetDir(AppFileConfig.getFileCompressDirectory().getPath())
                .setCompressListener(compressListener).launch();
    }
}
