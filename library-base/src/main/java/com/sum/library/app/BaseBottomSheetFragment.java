package com.sum.library.app;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sdl on 2018/5/15.
 * 底部浮层界面，点击空白部分，自动关闭界面
 */
public abstract class BaseBottomSheetFragment extends BottomSheetDialogFragment {

    private static class BottomDialog extends BottomSheetDialog {

        BottomDialog(Context context, int theme) {
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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomDialog(getContext(), getTheme());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setCancelable(false);//让界面不跟随手势自动滚动
        return inflater.inflate(getLayoutId(), container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initParams(view);
    }

    protected abstract int getLayoutId();

    protected abstract void initParams(View view);

    public void showFast(Context context) {
        if (context instanceof FragmentActivity) {
            FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();
            show(manager, "bottom_dialog");
        }
    }
}