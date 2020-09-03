package com.sum.library.view.refresh;

import android.content.Context;

import com.scwang.smart.refresh.layout.api.RefreshHeader;

/**
 * Created by sdl on 2020/8/28
 */
public class RefreshEmptyHeader extends EmptyComponent implements RefreshHeader {

    public RefreshEmptyHeader(Context context) {
        super(context);
    }

    public RefreshEmptyHeader(Context context, boolean refreshCallback, String text) {
        super(context, refreshCallback, text);
    }

}
