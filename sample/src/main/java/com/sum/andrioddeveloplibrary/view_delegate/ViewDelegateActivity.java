package com.sum.andrioddeveloplibrary.view_delegate;

import com.sum.andrioddeveloplibrary.view_delegate.delegate.UiCopyUseDelegate;
import com.sum.andrioddeveloplibrary.view_delegate.delegate.UiKonlinDelegate;
import com.sum.library.app.delegate.BaseDelegateActivity;
import com.sum.library.app.delegate.IViewDelegate;

public class ViewDelegateActivity extends BaseDelegateActivity {

    @Override
    protected Class<? extends IViewDelegate> getViewDelegateClass() {
        return UiCopyUseDelegate.class;
//        return UiKonlinDelegate.class;
    }
}
