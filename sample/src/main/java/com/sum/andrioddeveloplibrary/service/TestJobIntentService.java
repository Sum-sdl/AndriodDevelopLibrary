package com.sum.andrioddeveloplibrary.service;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.JobIntentService;

import com.sum.library.utils.Logger;

/**
 * Created by sdl on 2018/4/27.
 */
public class TestJobIntentService extends JobIntentService {

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

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        print("onHandleWork");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void print(String msg) {
        Logger.e("JobIntentService:" + msg + "；" + Integer.toHexString(hashCode()));
    }
}
