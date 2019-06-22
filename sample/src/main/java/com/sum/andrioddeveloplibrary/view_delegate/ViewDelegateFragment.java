package com.sum.andrioddeveloplibrary.view_delegate;

import com.sum.andrioddeveloplibrary.view_delegate.mvp.UiCopyUseDelegate;
import com.sum.library.app.BaseMvpFragment;
import com.sum.library.domain.mvp.IViewDelegate;

/**
 * Created by sdl on 2019-06-22.
 */
public class ViewDelegateFragment extends BaseMvpFragment {
    @Override
    protected Class<? extends IViewDelegate> getViewDelegateClass() {
//        return UiKonlinDelegate.class;
        return UiCopyUseDelegate.class;
    }
}
