package com.sum.andrioddeveloplibrary.testview;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ycc on 2018/1/23.
 */

public class ProduceTwoMinPushMessage {
    private List<BasePushMessage> mMessages;

    private final int MAX_TIME = 60 * 1000 * 2;
    private final int ONE_TIME = 30 * 1000;

    private int mHandleCount;
    private Handler mHandler;

    private boolean mNeedFilter = false;//控制是否需要做数据过滤

    private static ProduceTwoMinPushMessage mMessageDeal;

    public static ProduceTwoMinPushMessage getInstance() {
        if (mMessageDeal == null) {
            synchronized (ProduceTwoMinPushMessage.class) {
                if (mMessageDeal == null) {
                    mMessageDeal = new ProduceTwoMinPushMessage();
                }
            }
        }
        return mMessageDeal;
    }

    //设置登录开始
    public void resetLoginTime() {
        mNeedFilter = true;
        mHandleCount = 0;
        checkOneTime();
    }

    private ProduceTwoMinPushMessage() {
        mMessages = new ArrayList<>();
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Log.e("ycc", "handleMessage");
                mHandleCount++;//单位计时个数
                if (isTimeInMaxTime()) {//时间不足继续
                    if (mMessages.size() > 0) {
                        sendOneNotification();
                    }
                    checkOneTime();
                } else {
                    mNeedFilter = false;
                    mMessages.clear();
                }
                return true;
            }
        });
    }

    private boolean isTimeInMaxTime() {
        return mHandleCount * ONE_TIME < MAX_TIME;
    }


    public boolean addOneMessage(BasePushMessage basePushMessage) {
        if (!mNeedFilter) {
            return false;
        }
        //2分钟之内异步处理
        if (isTimeInMaxTime()) {
            mMessages.add(basePushMessage);
            return true;
        }
        return false;
    }

    private void sendOneNotification() {//避免处理同一个消息
        int index = mMessages.size() - 1;
        BasePushMessage pushMessage = mMessages.get(index);
        sendMessage(pushMessage);
        mMessages.remove(pushMessage);
    }

    private void sendMessage(BasePushMessage message) {
        Log.e("ycc", "sendMessage:" + message);
    }

    private void checkOneTime() {
        mHandler.sendEmptyMessageDelayed(0, ONE_TIME);
    }

}
