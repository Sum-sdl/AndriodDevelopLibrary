package com.sum.andrioddeveloplibrary.testActivity;

import android.os.Bundle;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
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
        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Logger.e(" " + scrollY + " " + oldScrollY + " " + v.getHeight() + " " + v.getTop());

                content((LinearLayout) v.getChildAt(0), scrollY);
            }
        });
    }


    private void content(LinearLayout linearLayout, int deY) {
        View childAt = linearLayout.getChildAt(1);
        childAt.setTranslationY(deY);
        int top = childAt.getTop();//固定的值,自己距离Parent的距离
        //top 获取view 在ScorllView中的位置，根据scrollY可以用来判断是否移动的到界面边界
        // childAt.getScrollY() =0 view在Y方向的位置没有移动，或者跟Canvas 移动有关
        Logger.e("T:" + top + " sY:" + childAt.getScrollY() + " " + linearLayout.getTop());
    }
}
