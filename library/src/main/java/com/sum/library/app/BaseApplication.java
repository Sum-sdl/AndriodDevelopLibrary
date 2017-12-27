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
        //blankj init
        Utils.init(this);

    }

}
