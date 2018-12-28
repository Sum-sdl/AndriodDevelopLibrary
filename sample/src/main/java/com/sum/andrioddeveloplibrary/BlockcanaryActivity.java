package com.sum.andrioddeveloplibrary;

import android.os.HandlerThread;

import com.sum.library.app.BaseActivity;

/**
 * Created by sdl on 2018/12/28.
 */
public class BlockcanaryActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_blockcanary;
    }

    @Override
    protected void initParams() {
        //线程优先级 -20 到 19；由大到小
//        HandlerThread
    }
}
