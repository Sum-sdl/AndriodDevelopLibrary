package com.sum.andrioddeveloplibrary.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;
import androidx.annotation.Nullable;

import com.sum.library.utils.Logger;

/**
 * Created by sdl on 2018/7/5.
 */
public class RemoteService extends Service {

    private Messenger mMsg;

    @Override
    public void onCreate() {
        super.onCreate();

        Handler mHandler = new Handler(msg -> {
            Logger.e("handleMessage:" + msg.obj + "," + msg.what);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        });

        mMsg = new Messenger(mHandler);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        print("onStartCommand");
        String data = intent.getStringExtra("data");
        if (data != null) {
        }
        return START_STICKY;
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
        return mMsg.getBinder();
    }

    private void print(String msg) {
        Logger.e("RemoteService:" + msg + "；" + Integer.toHexString(hashCode()));
    }
}
