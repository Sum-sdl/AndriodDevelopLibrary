package com.sum.library.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import org.xutils.x;

/**
 * Created by Summer on 2016/9/9.
 */
public abstract class BaseActivity extends FragmentActivity {

    protected abstract void initParams();

    public boolean isNeedInject() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isNeedInject()) {
            x.view().inject(this);
            initParams();
        }
    }
}
