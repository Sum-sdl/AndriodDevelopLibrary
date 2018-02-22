package com.sum.andrioddeveloplibrary.testview;

import android.os.Handler;
import android.os.Message;

import com.sum.library.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sdl on 2018/1/23.
 */

public class BasePushMessage {

    private List<Object> mMessages;

    private final int MAX_TIME = 6 * 1000 * 2;
    private final int ONE_TIME = 3 * 1000;

    private int mHandleCount;
    private Handler mHandler;

    public BasePushMessage() {
        mMessages = new ArrayList<>();
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (mMessages.size() > 0) {
                    mHandleCount++;
                    sendOneNotification();
                    if (mHandleCount * ONE_TIME < MAX_TIME) {//时间不足继续
                        checkOneTime();
                    }
                } else {
                    checkOneTime();
                }
                return true;
            }
        });
        checkOneTime();
    }

    public void addOneMessage(Object o) {
        mMessages.add(o);
    }

    private void sendOneNotification() {//避免处理同一个消息
        Logger.e("sendOneNotification:" + mMessages.size());
    }

    private void checkOneTime() {
        Logger.e("checkOneTime,mHandleCount:" + mHandleCount);
        mHandler.sendEmptyMessageDelayed(0, ONE_TIME);
    }
}
