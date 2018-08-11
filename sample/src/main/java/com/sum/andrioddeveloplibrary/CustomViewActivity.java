package com.sum.andrioddeveloplibrary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.sum.library.view.widget.CircleView;

import java.util.ArrayList;
import java.util.List;

import add_class.other_view.ChooseView;

public class CustomViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);

        final ChooseView chooseView = (ChooseView) findViewById(R.id.choose_view);
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


        CircleView pcv_splash_jump = findViewById(R.id.pcv_splash_jump);
        CircleView pcv_splash_jump2 = findViewById(R.id.pcv_splash_jump2);
        CircleView pcv_splash_jump3 = findViewById(R.id.pcv_splash_jump3);
        CircleView pcv_splash_jump4 = findViewById(R.id.pcv_splash_jump4);
        pcv_splash_jump3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("Jump");
            }
        });
        findViewById(R.id.bt2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pcv_splash_jump2.fastStart();
                pcv_splash_jump4.fastStart();
                pcv_splash_jump3.fastStart();
                pcv_splash_jump.fastStart(3000, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ToastUtils.showShort("Anim Finish");
                    }
                });
            }
        });
    }
}
