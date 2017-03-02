package com.sum.andrioddeveloplibrary.testActivity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.sum.andrioddeveloplibrary.R;
import com.sum.andrioddeveloplibrary.adapter.FragmentViewPagerAdapter;
import com.sum.andrioddeveloplibrary.testActivity.fragment.ComplexViewFragment;
import com.sum.andrioddeveloplibrary.testActivity.fragment.RefreshFragment;
import com.sum.andrioddeveloplibrary.testActivity.fragment.TestFragment;
import com.sum.library.app.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_top_tab)
public class TopTabActivity extends BaseActivity {


    @ViewInject(R.id.tab_layout)
    private TabLayout tabLayout;

    @ViewInject(R.id.view_pager)
    private ViewPager viewPager;

    private FragmentViewPagerAdapter mAdapter;

    @Override
    protected void initParams() {

        mAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager());

        List<String> data = new ArrayList<>();
        data.add("Tab1");
        data.add("Tab2");
        data.add("Tab3");
        mAdapter.setTitles(data);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(TestFragment.instance("Tab1"));
        fragments.add(new RefreshFragment());
        fragments.add(new ComplexViewFragment());
        mAdapter.setFragments(fragments);

        viewPager.setAdapter(mAdapter);


//        tabLayout.addTab(tabLayout.newTab().setText("Tab1"));
//        tabLayout.addTab(tabLayout.newTab().setText("Tab2"));
//        tabLayout.addTab(tabLayout.newTab().setText("Tab3"));

        tabLayout.setupWithViewPager(viewPager);
    }

}
