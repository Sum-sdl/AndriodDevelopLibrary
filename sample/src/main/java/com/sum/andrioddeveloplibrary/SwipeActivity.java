package com.sum.andrioddeveloplibrary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sum.library.view.widget.SwipeLayout;

public class SwipeActivity extends AppCompatActivity {

    SwipeLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        swipeLayout = (SwipeLayout) findViewById(R.id.swipe);

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
    }
}
