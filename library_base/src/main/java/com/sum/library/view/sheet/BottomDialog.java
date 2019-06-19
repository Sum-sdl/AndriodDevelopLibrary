package com.sum.library.view.sheet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sdl on 2018/5/15.
 */
class BottomDialog extends BottomSheetDialog {

    BottomDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        addTouchListener();
    }

    @Override
    public void setContentView(int layoutResId) {
        super.setContentView(layoutResId);
        addTouchListener();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        addTouchListener();
    }

    private void addTouchListener() {
        View touch = findViewById(android.support.design.R.id.touch_outside);
        if (touch != null) {
            touch.setOnClickListener(v -> {
                if (isShowing()) {
                    cancel();
                }
            });
        }
    }
}
