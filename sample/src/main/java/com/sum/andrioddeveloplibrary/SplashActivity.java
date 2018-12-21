package com.sum.andrioddeveloplibrary;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.AdaptScreenUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sum.library.app.BaseActivity;
import com.sum.library.utils.Logger;

import java.util.List;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.e("ScreenUtils.size->" + ScreenUtils.getScreenWidth() + "," + ScreenUtils.getScreenHeight());

        findViewById(R.id.b3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenUtils.toggleFullScreen(SplashActivity.this);
            }
        });
        findViewById(R.id.b1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findViewById(R.id.ll_content).requestLayout();
            }
        });
    }

    private Resources mRes;

    @Override
    public Resources getResources() {//设计宽度
        if (mRes == null) {
//            mRes = AdaptScreenUtils.adaptWidth(super.getResources(), 1080);
            mRes = AdaptScreenUtils.adaptHeight(super.getResources(), 1920);
//            mRes = AdaptScreenUtils.closeAdapt(super.getResources());
        }
        return mRes;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initParams() {
    }

    private void per() {
        List<String> permissions = PermissionUtils.getPermissions();


        PermissionUtils.permission(PermissionConstants.getPermissions(PermissionConstants.STORAGE)).callback(new PermissionUtils.FullCallback() {
            @Override
            public void onGranted(List<String> permissionsGranted) {//允许
                ToastUtils.showShort(permissions.toString());
            }

            @Override
            public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                show();
            }
        }).request();


    }

    private void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setTitle("提示").setMessage("权限拒绝")
                .setPositiveButton("确定", (dialog, which) -> {
                    Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.parse("package:" + SplashActivity.this.getPackageName()));
                    this.startActivity(intent);
                }).setNegativeButton("取消", (dialog, which) -> {
        }).setCancelable(false).show();
    }
}
