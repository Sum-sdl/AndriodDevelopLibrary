package com.sum.andrioddeveloplibrary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sum.library.view.widget.ChooseView;

public class CustomViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);

        ChooseView chooseView = (ChooseView) findViewById(R.id.choose_view);
        chooseView.setBitmapResId(R.mipmap.ic_work_type_bike_big);
    }
}
