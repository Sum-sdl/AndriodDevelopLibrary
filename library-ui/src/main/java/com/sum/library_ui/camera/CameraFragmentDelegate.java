package com.sum.library_ui.camera;

import android.view.View;

import com.sum.library.app.delegate.BaseAppUiDelegate;
import com.sum.library.utils.Logger;
import com.sum.library_ui.R;

/**
 * Created by sdl on 2019-07-11.
 */
public class CameraFragmentDelegate extends BaseAppUiDelegate {

    @Override
    protected int getLayoutId() {
        return R.layout.ui_activity_camera;
    }

    @Override
    protected void initParams(View view) {
        String targetFile = mIntentExtras.getString("targetFile");
        Logger.e("targetFile->" + targetFile);
    }
}
