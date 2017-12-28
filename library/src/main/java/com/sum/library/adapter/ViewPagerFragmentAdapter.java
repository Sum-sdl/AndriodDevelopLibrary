package com.sum.library.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Sum on 15/11/28.
 */
public class ViewPagerFragmentAdapter extends FragmentPagerAdapter {

    private List<? extends Fragment> mFragments;
    private List<String> mTitles;


    public ViewPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public ViewPagerFragmentAdapter(FragmentManager fm, List<? extends Fragment> mFragments, List<String> mTitles) {
        super(fm);
        this.mFragments = mFragments;
        this.mTitles = mTitles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles != null && position < mTitles.size()) {
            return mTitles.get(position);
        }
        return super.getPageTitle(position);
    }

    @Override
    public Fragment getItem(int position) {
        if (mFragments != null) {
            return mFragments.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    public void setFragments(List<? extends Fragment> mFragments) {
        this.mFragments = mFragments;
    }

    public void setTitles(List<String> titles) {
        this.mTitles = titles;
    }
}
