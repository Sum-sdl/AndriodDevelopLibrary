package com.sum.andrioddeveloplibrary.view_delegate;

import com.sum.andrioddeveloplibrary.view_delegate.delegate.UiCopyUseDelegate;
import com.sum.library.app.delegate.BaseDelegateFragment;
import com.sum.library.app.delegate.IViewDelegate;

/**
 * Created by sdl on 2019-06-22.
 */
public class ViewDelegateFragment extends BaseDelegateFragment {
    @Override
    protected Class<? extends IViewDelegate> getViewDelegateClass() {
//        return UiKonlinDelegate.class;
        return UiCopyUseDelegate.class;
    }
}
