package com.sum.andrioddeveloplibrary;

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
                    }
                }
        );
    }
}
