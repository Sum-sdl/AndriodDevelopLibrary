package com.sum.library.view.refresh;

import android.content.Context;

import com.scwang.smart.refresh.layout.api.RefreshFooter;

/**
 * Created by sdl on 2020/8/28
 */
public class RefreshEmptyFooter extends EmptyComponent implements RefreshFooter {


    public RefreshEmptyFooter(Context context) {
        super(context);
    }

    public RefreshEmptyFooter(Context context, boolean refreshCallback, String text) {
        super(context, refreshCallback, text);
    }

    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        //忽略更多底部内容显示
        return true;
    }
}
