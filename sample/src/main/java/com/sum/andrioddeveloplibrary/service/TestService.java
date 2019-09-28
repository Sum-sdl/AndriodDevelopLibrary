package com.sum.andrioddeveloplibrary.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

import com.sum.library.utils.Logger;

/**
 * Created by sdl on 2018/4/27.
 */
public class TestService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        print("onCreate");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        print("onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        print("onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        print("onBind");
        return null;
    }


    private void print(String msg) {
        Logger.e("JobIntentService:" + msg + "；" + Integer.toHexString(hashCode()));
    }
}
