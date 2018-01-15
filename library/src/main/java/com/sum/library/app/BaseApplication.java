package com.sum.library.app;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.sum.library.BuildConfig;
import com.sum.library.net.ImagePipelineConfigUtils;

import org.xutils.x;

/**
 * Created by Summer on 2016/9/13.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //工具类
        Utils.init(this);
        //xUtils
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
        //图片框架
        Fresco.initialize(this, ImagePipelineConfigUtils.getDefaultImagePipelineConfig(this));
//        Fresco.initialize(this);
    }

}
