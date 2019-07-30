package com.sum.library_ui.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.sum.library.app.BaseActivity;
import com.sum.library_ui.R;
import com.sum.library_ui.utils.LibUtils;

/**
 * Created by sdl on 2019-07-11.
 */
public class CameraActivity extends BaseActivity implements CameraFragment.TakeCompleteListener {

    public static void open(Activity activity, int requestCode) {
        open(activity, requestCode, null);
    }

    /**
     * @param requestCode 请求码
     * @param targetFile  不传,会有默认值路径返回
     */
    public static void open(Activity activity, int requestCode, String targetFile) {
        Intent intent = new Intent(activity, CameraActivity.class);
        intent.putExtra("targetFile", targetFile);
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(R.anim.activity_bottom_in, R.anim.activity_anim_no);
    }

    private View mPreviewView;

    private PhotoView mImageView;

    private CameraFragment mFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.ui_activity_camera_temp;
    }

    @Override
    protected void initParams() {
        LibUtils.setFullScreen(this);
        mPreviewView = findViewById(R.id.fl_take_result);
        mImageView = findViewById(R.id.photo_view);
        findViewById(R.id.tv_take_cancel).setOnClickListener(v -> cancel());

        findViewById(R.id.tv_take_conform).setOnClickListener(v -> conform());

        findViewById(R.id.tv_take).setOnClickListener(v -> reTake());

        if (hasPermission()) {
            startCamera();
        } else {
            if (hasDelayAllPermissions(this, needPermission)) {
                showTipDialog();
            } else {
                ActivityCompat.requestPermissions(this, needPermission, 1000);
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_anim_no, R.anim.activity_bottom_out);
    }

    private void showTipDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("为了您可以正常使用照相机，\n请点击\"设置\"-\"权限\"-打开 \"存储空间\"与\"相机\" 权限。")
                .setPositiveButton("设置", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                }).setNegativeButton("取消", (dialog, which) -> finish());
        builder.setCancelable(false);
        builder.show();
    }

    private String[] needPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};


    private boolean hasPermission() {
        for (String permission : needPermission) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private boolean hasDelayAllPermissions(Activity activity, String... permissions) {
        int count = 0;
        for (String permission : permissions) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                count++;
            }
        }
        return count != 0;
    }


    private void startCamera() {
        if (getSupportFragmentManager().getFragments().size() == 0) {
            CameraFragment fragment = new CameraFragment();
            Bundle bundle = new Bundle();
            bundle.putString("targetFile", getIntent().getStringExtra("targetFile"));
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(
                    R.id.fl_content, fragment, "camera").commitAllowingStateLoss();
            mFragment = fragment;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            boolean isGrant = true;
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    isGrant = false;
                    break;
                }
            }
            if (isGrant) {
                startCamera();
            } else {
                showTipDialog();
            }
        }
    }

    private String mFilePath;

    @Override
    public void onTakeFinish(String filePath) {
        mFilePath = filePath;
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        Uri parse = Uri.parse("file://" + filePath);
        RequestOptions options = RequestOptions.fitCenterTransform()
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(this).asDrawable().load(parse).apply(options)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(mImageView);

        mPreviewView.setVisibility(View.VISIBLE);
        //关闭相机
        mFragment.mPreviewView.setVisibility(View.GONE);
    }


    private void reTake() {
        mFragment.mPreviewView.setVisibility(View.VISIBLE);
        mPreviewView.setVisibility(View.GONE);
    }

    private void cancel() {
        mFragment.deleteFile();
        finish();
    }

    private void conform() {
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putString("path", mFilePath);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
