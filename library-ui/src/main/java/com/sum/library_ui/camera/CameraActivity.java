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
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.sum.library.app.BaseActivity;
import com.sum.library_ui.R;
import com.sum.library_ui.utils.LibUtils;

/**
 * Created by sdl on 2019-07-11.
 */
public class CameraActivity extends BaseActivity implements CameraFragment.TakeCompleteListener {

    public static void open(Activity context, String targetFile) {
        Intent intent = new Intent(context, CameraActivity.class);
        intent.putExtra("targetFile", targetFile);
        context.startActivityForResult(intent, 1001);
        context.overridePendingTransition(R.anim.activity_bottom_in, R.anim.activity_anim_no);
    }

    public static void open(Fragment context, String targetFile) {
        Intent intent = new Intent(context.getContext(), CameraActivity.class);
        intent.putExtra("targetFile", targetFile);
        context.startActivityForResult(intent, 1001);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ui_activity_camera_temp;
    }

    @Override
    protected void initParams() {
        LibUtils.setFullScreen(this);
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

    @Override
    public void onTakeFinish(String filePath) {
        //返回上一级目录
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putString("path", filePath);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
