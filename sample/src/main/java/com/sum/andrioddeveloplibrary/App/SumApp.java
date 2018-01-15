package com.sum.andrioddeveloplibrary.App;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.sum.andrioddeveloplibrary.net.TestToken;
import com.sum.library.AppFileConfig;
import com.sum.library.app.BaseApplication;
import com.sum.library.net.Retrofit2Helper;

import okhttp3.OkHttpClient;

/**
 * Created by sdl on 2017/12/27.
 */

public class SumApp extends BaseApplication {
    @Override
    public void onCreate() {
        AppFileConfig.FOLDER_NAME = "A_SumApp";
        super.onCreate();

        Retrofit2Helper instance = Retrofit2Helper.getInstance();
        OkHttpClient client = instance.buildDefaultOkHttpClient().addInterceptor(new TestToken()).build();
        instance.initRetrofit("http://newfgjapi.seconddepartment.house365.com/", client);
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
