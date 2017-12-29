package com.sum.library.app;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.sum.library.BuildConfig;

import org.xutils.x;

/**
 * Created by Summer on 2016/9/13.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
    }

}
