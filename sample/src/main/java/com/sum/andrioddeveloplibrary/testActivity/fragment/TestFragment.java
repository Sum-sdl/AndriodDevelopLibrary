package com.sum.andrioddeveloplibrary.testActivity.fragment;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sum.andrioddeveloplibrary.R;
import com.sum.library.app.BaseFragment;
import com.sum.library.domain.BaseViewModel;

/**
 * Created by 365 on 2017/3/2.
 */

public class TestFragment extends BaseFragment {


    public static TestFragment instance(String title) {
        TestFragment fragment = new TestFragment();
        Bundle bundle = new Bundle();
        bundle.putString("key", title);
        fragment.setArguments(bundle);
        return fragment;
    }

    private TextView textView;

    @Override
    public BaseViewModel getViewModel() {
        return super.getViewModel();
    }

    @Override
    protected void initParams(View view) {
        textView = (TextView) view.findViewById(R.id.tv_title);

        Bundle arguments = getArguments();
        if (arguments != null) {
            String key = getArguments().getString("key");
            textView.setText(key);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test;
    }
}
