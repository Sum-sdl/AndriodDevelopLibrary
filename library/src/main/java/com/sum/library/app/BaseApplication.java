package com.sum.library.app;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

/**
 * Created by Summer on 2016/9/13.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //工具类
        Utils.init(this);
        //图片框架
//        GlideBuilder
    }

}
