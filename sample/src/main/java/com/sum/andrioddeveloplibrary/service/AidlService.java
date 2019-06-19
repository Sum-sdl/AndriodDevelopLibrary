package com.sum.andrioddeveloplibrary.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.sum.andrioddeveloplibrary.service.aidl.IServiceWorker;
import com.sum.library.utils.Logger;

/**
 * Created by sdl on 2018/7/5.
 */
public class AidlService extends Service {

    private int mIndex;

    //通过aidl 跨进程互相通信
    private IBinder mIBinder = new IServiceWorker.Stub() {
        @Override
        public int getIndex() throws RemoteException {
            Logger.e("thread info service");
            return mIndex;
        }

        @Override
        public void addSize(int size) throws RemoteException {
            Logger.e("2------ thread info addSize " + size);
            mIndex = size;
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Logger.i("index=" + mIndex);
                    mIndex++;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
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
        return mIBinder;
    }

    private void print(String msg) {
        Logger.e("AidlService:" + msg + "；" + Integer.toHexString(hashCode()));
    }
}
