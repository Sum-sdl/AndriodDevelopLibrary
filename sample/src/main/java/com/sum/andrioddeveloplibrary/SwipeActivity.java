package com.sum.andrioddeveloplibrary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sum.andrioddeveloplibrary.testview.TestView;

public class SwipeActivity extends AppCompatActivity {

    TestView swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

//        swipeLayout = (TestView) findViewById(R.id.swipe);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //会重新测量，重绘
                swipeLayout.requestLayout();
            }
        });
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //只会重绘
                swipeLayout.invalidate();
            }
        });


      /*  final View b2 = findViewById(R.id.ttv);
        //view的布局参数layoutParams在layoutInflate的时候就创建完成
        Logger.e(b2.getWidth() + " " + b2.getMeasuredWidth() + " " + b2.getLayoutParams().width);
        b2.post(new Runnable() {
            @Override
            public void run() {
                //手动设置了view 的实际大小为100 但是xml中的参数为300,只会绘制300的宽界面
                Logger.e("Runnable:" + b2.getWidth() + " " + b2.getMeasuredWidth() + " " + b2.getLayoutParams().width);
            }
        });*/


        //测试addView 无layoutParams 会抛出异常
//        swipeLayout.addView(new TestView(this),null);

    }
}
