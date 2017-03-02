package com.sum.andrioddeveloplibrary.testActivity;

import android.view.View;

import com.sum.andrioddeveloplibrary.R;
import com.sum.andrioddeveloplibrary.testActivity.fragment.ComplexViewFragment;
import com.sum.andrioddeveloplibrary.testActivity.fragment.RefreshFragment;
import com.sum.andrioddeveloplibrary.testActivity.fragment.TestFragment;
import com.sum.library.app.BaseActivity;
import com.sum.library.framework.FragmentCacheManager;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;


@ContentView(R.layout.activity_bottom_tab)
public class BottomTabActivity extends BaseActivity {


    @Event(R.id.btn_1)
    private void btn_1(View view) {
        mManager.setCurrentFragment(1);
    }

    @Event(R.id.btn_2)
    private void btn_2(View view) {
        mManager.setCurrentFragment(2);
    }

    @Event(R.id.btn_3)
    private void btn_3(View view) {
        mManager.setCurrentFragment(3);
    }

    private FragmentCacheManager mManager;

    @Override
    protected void initParams() {
        mManager = FragmentCacheManager.instance();
        mManager.setUp(this, R.id.content);

        mManager.addFragmentToCache(1, RefreshFragment.class);
        mManager.addFragmentToCache(2, TestFragment.class);
        mManager.addFragmentToCache(3, ComplexViewFragment.class);

        mManager.setCurrentFragment(1);
    }

}
