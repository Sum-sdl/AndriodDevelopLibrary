package com.sum.andrioddeveloplibrary.testview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.sum.andrioddeveloplibrary.R;
import com.sum.library.app.BaseAppBar;

/**
 * Created by sdl on 2018/5/11.
 */
public class CustomBar extends BaseAppBar {

    public CustomBar(Context context) {
        super(context);
    }

    public CustomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initParams() {
        TextView tn = _findViewById(R.id.tv_1);
        tn.setText("@@@@@@@@@@@@@@@@@@@");
        tn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("Hello Base Bar");
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.bar_custom;
    }
}
