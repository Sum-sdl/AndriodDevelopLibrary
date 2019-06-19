package com.sum.library.domain;

import android.support.v4.util.Pools;

/**
 * Created by sdl on 2019/2/18.
 * ui界面常用交互操作
 */
public class ActionState implements Cloneable {
    //REGISTER:首次注册界面观察者发送
    static final int REGISTER = 0;

    //Toast提示
    public static final int TOAST = 1;
    //show 加载中
    public static final int DIALOG_LOADING = 2;
    //close 加载中
    public static final int DIALOG_HIDE = 3;
    //进度条提示
    public static final int DIALOG_PROGRESS_SHOW = 4;

    //网络请求开始
    public static final int NET_START = 5;
    //网络请求结束
    public static final int NET_FINISH = 6;
    //网络请求异常
    public static final int NET_ERROR = 7;


    private int state;
    private String msg;

    private ActionState() {
    }

    private ActionState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    private static Pools.SynchronizedPool<ActionState> pools = new Pools.SynchronizedPool<>(10);

    public static ActionState obtain(int state) {
        ActionState acquire = pools.acquire();
        if (acquire == null) {
            acquire = new ActionState(0);
        }
        acquire.state = state;
        acquire.msg = null;
        return acquire;
    }

    public static void release(ActionState state) {
        pools.release(state);
    }

}
