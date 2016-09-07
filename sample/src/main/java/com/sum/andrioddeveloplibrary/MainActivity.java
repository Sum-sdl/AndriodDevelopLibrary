package com.sum.andrioddeveloplibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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

        String string = getString(R.string.app_add_res);
        Logger.e("编译时添加的资源属性:" + string);

        boolean logShow = BuildConfig.LOG_SHOW;

        Logger.e("编译时添加的构建配置:" + logShow+" BuildType:"+BuildConfig.API_HOST);
    }

    private void startTargetActivity(Class a) {
        Intent intent = new Intent(this, a);
        startActivity(intent);
    }

}
