package com.sum.andrioddeveloplibrary.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.sum.library.utils.Logger;

/**
 * Created by sdl on 2018/4/27.
 */
public class TestIntentService extends IntentService {

    public TestIntentService() {
        super("worker");
    }

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
    protected void onHandleIntent(@Nullable Intent intent) {
        print("onHandleIntent");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void print(String msg) {
        Logger.e("JobIntentService:" + msg);
    }
}
