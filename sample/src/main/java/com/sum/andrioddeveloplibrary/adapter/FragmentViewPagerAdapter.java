package com.sum.andrioddeveloplibrary.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Sum on 15/11/28.
 */
public class FragmentViewPagerAdapter extends FragmentPagerAdapter {

    private List<? extends Fragment> mFragments;


    public FragmentViewPagerAdapter(FragmentManager fm) {
        super(fm);
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
}
