package com.sum.andrioddeveloplibrary;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sum.library.app.BaseActivity;
import com.sum.library.view.Helper.PermissionHelper;

/**
 * Created by sdl on 2017/12/29.
 * 启动页
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initParams() {

        per();

        findViewById(R.id.b7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                per();
            }
        });
    }

    private void per() {
        PermissionUtils.requestPermissions(
                this, 10, PermissionHelper.FilePermission(),
                new PermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        ToastUtils.showShort("onPermissionGranted");
                    }

                    @Override
                    public void onPermissionDenied(String[] deniedPermissions) {
                        ToastUtils.showShort("onPermissionDenied" + deniedPermissions[0]);

                        show(deniedPermissions[0]);

                    }
                }
        );
    }

    private void show(String deniedPermission) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setTitle("提示").setMessage(deniedPermission)
                .setPositiveButton("确定", (dialog, which) -> {
                    Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.parse("package:" + SplashActivity.this.getPackageName()));
                    this.startActivity(intent);
                }).setNegativeButton("取消", (dialog, which) -> {
        }).setCancelable(false).show();
    }
}
