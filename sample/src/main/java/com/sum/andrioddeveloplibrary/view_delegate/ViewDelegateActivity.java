package com.sum.andrioddeveloplibrary.view_delegate;

import com.sum.andrioddeveloplibrary.view_delegate.mvp.UiCopyUseDelegate;
import com.sum.library.app.BaseMvpActivity;
import com.sum.library.domain.mvp.IViewDelegate;

public class ViewDelegateActivity extends BaseMvpActivity {

    @Override
    protected Class<? extends IViewDelegate> getViewDelegateClass() {
        return UiCopyUseDelegate.class;
    }
}
