package com.sum.library.ui.image;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.sum.library.ui.image.preview.ImagePreviewActivity;

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
     * 图片预览界面
     */
    public static void appImagePreview(Context context, ArrayList<String> urls) {
        ImagePreviewActivity.Companion.open(context, urls);
    }

}
