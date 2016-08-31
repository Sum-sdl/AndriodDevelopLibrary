package com.sum.andrioddeveloplibrary;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.sum.library.utils.Logger;
import com.sum.library.view.Helper.ViewHelper;

public class ShadowActivity extends AppCompatActivity {

    /**
     * 1.PorterDuff.Mode.CLEAR 所绘制不会提交到画布上。
     * 2.PorterDuff.Mode.SRC 显示上层绘制图片
     * 3.PorterDuff.Mode.DST 显示下层绘制图片
     * 4.PorterDuff.Mode.SRC_OVER 正常绘制显示，上下层绘制叠盖。
     * <p/>
     * 5.PorterDuff.Mode.DST_OVER 上下层都显示。下层居上显示。
     * 6.PorterDuff.Mode.SRC_IN 取两层绘制交集。显示上层。
     * 7.PorterDuff.Mode.DST_IN 取两层绘制交集。显示下层。
     * 8.PorterDuff.Mode.SRC_OUT 取上层绘制非交集部分。
     * <p/>
     * 9.PorterDuff.Mode.DST_OUT 取下层绘制非交集部分。
     * 10.PorterDuff.Mode.SRC_ATOP 取下层非交集部分与上层交集部分
     * 11.PorterDuff.Mode.DST_ATOP 取上层非交集部分与下层交集部分
     * 12.PorterDuff.Mode.XOR 变暗
     * <p/>
     * 13.PorterDuff.Mode.DARKEN 调亮
     * 14.PorterDuff.Mode.LIGHTEN 用于颜色滤镜
     * 15.PorterDuff.Mode.MULTIPLY
     * 16.PorterDuff.Mode.SCREEN
     */

    private CardView mCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shadow_activity);
        FilterModel();

        mCar = (CardView) findViewById(R.id.card4);
        mCar.setMaxCardElevation(40);

        SeekBar bar = (SeekBar) findViewById(R.id.seekBar);
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float percent = ViewHelper.progressPercent(progress, 100);
                Logger.e("pro:" + progress+" percent:"+percent);
                mCar.setCardElevation(40 * percent);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    //颜色过滤测试
    private void FilterModel() {
        //        porTest(R.id.image1, PorterDuff.Mode.CLEAR);
//        porTest(R.id.image2, PorterDuff.Mode.SRC);
//        porTest(R.id.image3, PorterDuff.Mode.DST);
//        porTest(R.id.image4, PorterDuff.Mode.SRC_OUT);

//        porTest(R.id.image1, PorterDuff.Mode.DST_OVER);
//        porTest(R.id.image2, PorterDuff.Mode.SRC_IN);
//        porTest(R.id.image3, PorterDuff.Mode.DST_IN);
//        porTest(R.id.image4, PorterDuff.Mode.SRC_OUT);
//
//        porTest(R.id.image1, PorterDuff.Mode.DST_OUT);
//        porTest(R.id.image2, PorterDuff.Mode.SRC_ATOP);
//        porTest(R.id.image3, PorterDuff.Mode.DST_ATOP);
//        porTest(R.id.image4, PorterDuff.Mode.XOR);

        porTest(R.id.image1, PorterDuff.Mode.DARKEN);
        porTest(R.id.image2, PorterDuff.Mode.LIGHTEN);
        porTest(R.id.image3, PorterDuff.Mode.MULTIPLY);
        porTest(R.id.image4, PorterDuff.Mode.SCREEN);
    }

    private void porTest(int resId, PorterDuff.Mode mode) {
        ImageView imageView = (ImageView) findViewById(resId);
        Drawable background = imageView.getBackground();
        background.setColorFilter(Color.RED, mode);
    }
}
