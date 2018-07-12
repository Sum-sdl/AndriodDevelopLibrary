package com.sum.library.app;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.sum.library.net.ImagePipelineConfigUtils;

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
        Fresco.initialize(this, ImagePipelineConfigUtils.getDefaultImagePipelineConfig(this));
//        Fresco.initialize(this);
    }

}
