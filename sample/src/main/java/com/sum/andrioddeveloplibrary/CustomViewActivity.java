package com.sum.andrioddeveloplibrary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import add_class.other_view.ChooseView;

public class CustomViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);

        final ChooseView chooseView = (ChooseView) findViewById(R.id.choose_view);
//        chooseView.setBitmapResId(R.mipmap.ic_work_type_bike_big);
        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ChooseView.ShowData> tipText = new ArrayList<>();
                tipText.add(new ChooseView.ShowData("AAA", 15));
                tipText.add(new ChooseView.ShowData("A", 25));
                tipText.add(new ChooseView.ShowData("99999", 35));
                tipText.add(new ChooseView.ShowData("B", 45));
                tipText.add(new ChooseView.ShowData("CC", 55));
                chooseView.setShowData(tipText);
            }
        });

        chooseView.setChooseChangeListener(new ChooseView.ChooseChangeListener() {
            @Override
            public void onChooseChange(ChooseView.ShowData time) {
                ToastUtils.showShort(time.tip + "," + time.value);
            }
        });

    }
}
