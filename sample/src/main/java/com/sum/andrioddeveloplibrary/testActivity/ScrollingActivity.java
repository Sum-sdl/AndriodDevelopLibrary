package com.sum.andrioddeveloplibrary.testActivity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.sum.andrioddeveloplibrary.R;
import com.sum.library.utils.Logger;

public class ScrollingActivity extends AppCompatActivity {

    private NestedScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        scrollView = (NestedScrollView) findViewById(R.id.scrollView);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Logger.e(" " + scrollY + " " + oldScrollY + " " + v.getHeight() + " " + v.getTop());
                content((LinearLayout) v.getChildAt(0));
            }
        });
    }


    private void content(LinearLayout linearLayout) {
        View childAt = linearLayout.getChildAt(1);
        int top = childAt.getTop();

        //top 获取view 在ScorllView中的位置，根据scrollY可以用来判断是否移动的到界面边界

        Logger.e("t:" + top + " " + linearLayout.getChildAt(2).getTop()+" "+linearLayout.getTop());
    }
}
