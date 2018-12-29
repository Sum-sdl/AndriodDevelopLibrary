package com.sum.andrioddeveloplibrary;

import android.os.HandlerThread;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
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
        findViewById(R.id.b1).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    Thread.sleep(2000);
                    ToastUtils.showShort("finish sleep");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
