package com.sum.andrioddeveloplibrary.view_delegate

import com.sum.andrioddeveloplibrary.view_delegate.delegate.UiKonlinDelegate
import com.sum.library.app.delegate.BaseDelegateFragment

/**
 * Created by sdl on 2019-06-24.
 */

class ViewDkotlinFragment : BaseDelegateFragment() {

    override fun getViewDelegateClass(): Class<UiKonlinDelegate> {
        return UiKonlinDelegate::class.java
    }
}