package com.sum.library.view.Helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.sum.library.AppFileConfig;
import com.sum.library.utils.Logger;
import com.sum.library.utils.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sum on 16/5/27.
 * 拍照统一管理
 */
public class PhotoHelper {

    //拍照
    public final static int CAPTURE_PHOTO_DEFAULT_CODE = 0x1111;
    //相册
    public final static int DICM_PHOTO_DEFAULT_CODE = 0x1112;
    //剪裁照片
    public final static int CROP_PHOTO_DEFAULT_CODE = 0x1113;
    //请求拍照权限6.0
    private final static int CAMERA_PER_REQ = 0x11;

    private final static String TIMESTAMP_FORMAT = "yyyy_MM_dd_HH_mm_ss";
    private Activity mActivity;
    private Fragment mFragment;
    private Context mContext;

    private int mCurRequestCode;//请求码

    private static SimpleDateFormat mFormat = new SimpleDateFormat(TIMESTAMP_FORMAT, Locale.CHINA);
    /**
     * 存放图片的目录
     */
    private File mPhotoFolder;
    /**
     * 拍照生成的图片文件
     */
    private File mPhotoFile;

    private Uri mCropUri;


    public PhotoHelper(Activity activity) {
        mContext = activity;
        this.mActivity = activity;
        this.mPhotoFolder = AppFileConfig.getCacheFile();
    }

    public PhotoHelper(Fragment fragment) {
        mContext = fragment.getContext();
        this.mFragment = fragment;
        this.mPhotoFolder = AppFileConfig.getCacheFile();
    }

    //显示相册
    public void showDICM() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        //界面权限判断
        if (mActivity != null && mFragment == null) {
            mActivity.startActivityForResult(intent, DICM_PHOTO_DEFAULT_CODE);
        } else if (mFragment != null) {
            mFragment.startActivityForResult(intent, DICM_PHOTO_DEFAULT_CODE);
        }
    }


    /**
     * 拍照,默认的请求码
     */
    public void capture() {
        capture(CAPTURE_PHOTO_DEFAULT_CODE);
    }

    /**
     * 拍照
     *
     * @param requestCode 指定的请求码
     */
    public void capture(int requestCode) {
        mCurRequestCode = requestCode;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //fragment里启动检测
            if (mActivity == null && mFragment != null) {
                mActivity = mFragment.getActivity();
            }
            if (!PermissionHelper.hasPermissionCamera(mActivity)) {
                PermissionHelper.reqPermissionCamera(mActivity, CAMERA_PER_REQ);
            } else {
                takePhone();
            }
        } else {
            takePhone();
        }
    }

    //权限返回处理
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission Granted
            takePhone();
        } else {
            // Permission Denied
            ToastUtil.showToastLong("拒绝打开相机");
        }
    }

    //创建文件，启动相机
    private void takePhone() {
        createPhotoFile();
        if (mPhotoFile == null) {
            Logger.e("take phone file is null");
            return;
        }
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri fileUri = Uri.fromFile(mPhotoFile);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        //界面权限判断
        if (mActivity != null && mFragment == null) {
            mActivity.startActivityForResult(captureIntent, mCurRequestCode);
        } else if (mFragment != null) {
            mFragment.startActivityForResult(captureIntent, mCurRequestCode);
        }

    }

    //创建当前拍照的文件
    private void createPhotoFile() {
        if (mPhotoFolder != null) {
            if (!mPhotoFolder.exists()) {//检查保存图片的目录存不存在
                mPhotoFolder.mkdirs();
            }

            String fileName = mFormat.format(new Date());
            mPhotoFile = new File(mPhotoFolder, fileName + ".jpg");
            if (mPhotoFile.exists()) {
                mPhotoFile.delete();
            }
            try {
                mPhotoFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                mPhotoFile = null;
            }
        } else {
            mPhotoFile = null;
        }
    }


    /**
     * 判断系统中是否存在可以启动的相机应用
     *
     * @return 存在返回true，不存在返回false
     */
    public boolean hasCamera() {
        PackageManager packageManager = mActivity.getPackageManager();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 获取当前拍到的图片文件
     *
     * @return
     */
    public File getPhoto() {
        return mPhotoFile;
    }

    /**
     * 设置照片文件
     *
     * @param photoFile
     */
    public void setPhoto(File photoFile) {
        this.mPhotoFile = photoFile;
    }

    public void startPhotoZoom(Uri uri) {
        mCropUri = uri;
        int dp = 500;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);//输出是X方向的比例
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高，切忌不要再改动下列数字，会卡死
        intent.putExtra("outputX", dp);//输出X方向的像素
        intent.putExtra("outputY", dp);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);//设置为不返回数据
        //界面权限判断
        if (mActivity != null && mFragment == null) {
            mActivity.startActivityForResult(intent, CROP_PHOTO_DEFAULT_CODE);
        } else if (mFragment != null) {
            mFragment.startActivityForResult(intent, CROP_PHOTO_DEFAULT_CODE);
        }
    }

    public Bitmap getCropBitmap() {
        return getBitmapFromUri(mCropUri);
    }


    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
