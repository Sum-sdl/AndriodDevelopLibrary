package com.sum.library.utils.simple;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Sum on 15/11/28.
 */
public class SimpleViewPagerFragmentAdapter extends FragmentPagerAdapter {

    private List<? extends Fragment> mFragments;
    private List<String> mTitles;


    public SimpleViewPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public SimpleViewPagerFragmentAdapter(FragmentManager fm, List<? extends Fragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    public SimpleViewPagerFragmentAdapter(FragmentManager fm, List<? extends Fragment> mFragments, List<String> mTitles) {
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
