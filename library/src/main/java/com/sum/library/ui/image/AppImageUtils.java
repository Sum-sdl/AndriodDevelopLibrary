package com.sum.library.ui.image;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.sum.library.AppFileConfig;
import com.sum.library.R;
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
     * 选择调用系统相机
     */
    public static void systemTakePhoto(Activity activity, int requestCode, Uri fileUri) {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        activity.startActivityForResult(captureIntent, requestCode);
    }


    /**
     * 拍照后刷新系统相册
     */
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
//        options.setToolbarWidgetColor(Color.BLACK);
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

}
