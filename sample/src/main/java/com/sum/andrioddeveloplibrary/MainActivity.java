package com.sum.andrioddeveloplibrary;

import android.content.Intent;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sum.andrioddeveloplibrary.testActivity.ScrollingActivity;
import com.sum.library.utils.Logger;
import com.sum.library.utils.ToastUtil;
import com.sum.library.view.widget.MagicCircle;

public class MainActivity extends AppCompatActivity {

    private MagicCircle magic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        magic = (MagicCircle) findViewById(R.id.magic);

        ToastUtil.init(this);

        findViewById(R.id.b1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTargetActivity(StickyActivity.class);
            }
        });
        findViewById(R.id.b2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTargetActivity(SwipeActivity.class);
            }
        });
        findViewById(R.id.b3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTargetActivity(ShadowActivity.class);
            }
        });
        findViewById(R.id.b4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTargetActivity(CustomViewActivity.class);
            }
        });
        findViewById(R.id.b6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTargetActivity(ScrollingActivity.class);
            }
        });

        findViewById(R.id.b5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                magic.startAnimation();
            }
        });


        final View b2 = findViewById(R.id.b2);
        //view的布局参数在layoutInflate的时候就创建完成
        Logger.e(b2.getWidth() + " " + b2.getMeasuredWidth() + " " + b2.getLayoutParams().width);
        b2.post(new Runnable() {
            @Override
            public void run() {
                Logger.e("Runnable:" + b2.getWidth() + " " + b2.getMeasuredWidth() + " " + b2.getLayoutParams().width);
                //layoutparmas 规定了view的绘制区域
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) b2.getLayoutParams();
                layoutParams.topMargin = 100;
                layoutParams.width += 300;
                b2.setLayoutParams(layoutParams);
                Logger.e("Runnable2:" + b2.getWidth() + " " + b2.getMeasuredWidth() + " " + b2.getLayoutParams().width);
            }
        });

//        String string = getString(R.string.app_add_res);
//        Logger.e("编译时添加的资源属性:" + string);
//
//        boolean logShow = BuildConfig.LOG_SHOW;
//
//        Logger.e("编译时添加的构建配置:" + logShow+" BuildType:"+BuildConfig.API_HOST);
//        xml();

        doMainTest();
    }

    private void xml() {

        View view = LayoutInflater.from(this).inflate(R.layout.activity_swipe, null);

        View.inflate(this, R.layout.activity_swipe, null);

        //xml解析
        XmlResourceParser parser = getResources().getLayout(R.layout.activity_swipe);

        //布局属性
        AttributeSet attributeSet = Xml.asAttributeSet(parser);

        Logger.e("" + attributeSet.getAttributeCount());

        TypedArray typedArray = obtainStyledAttributes(attributeSet, R.styleable.SwipeLayout);

        Logger.e("typedArray swipelayout " + typedArray.getInt(R.styleable.SwipeLayout_swipe_direction, 1000));

        typedArray.recycle();
    }

    private void startTargetActivity(Class a) {
        Intent intent = new Intent(this, a);
        startActivity(intent);
    }


    private void doMainTest() {

    }

}
