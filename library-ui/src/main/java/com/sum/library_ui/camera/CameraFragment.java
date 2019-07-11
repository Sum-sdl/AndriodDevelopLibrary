package com.sum.library_ui.camera;

import com.sum.library.app.delegate.BaseDelegateFragment;

/**
 * Created by sdl on 2019-07-11.
 */
public class CameraFragment extends BaseDelegateFragment<CameraFragmentDelegate> {
    @Override
    protected Class<CameraFragmentDelegate> getViewDelegateClass() {
        return CameraFragmentDelegate.class;
    }
}
