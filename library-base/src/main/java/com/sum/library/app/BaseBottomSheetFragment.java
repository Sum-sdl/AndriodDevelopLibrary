package com.sum.library.app;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * Created by sdl on 2018/5/15.
 * 底部浮层界面，点击空白部分，自动关闭界面
 */
public abstract class BaseBottomSheetFragment extends BottomSheetDialogFragment {

    protected static class BottomDialog extends BottomSheetDialog {

        //背景色透明
        boolean bgIsTransparent;

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
            View touch = findViewById(R.id.touch_outside);
            if (touch != null) {
                touch.setOnClickListener(v -> {
                    if (isShowing()) {
                        cancel();
                    }
                });
            }
            //跟布局颜色透明
            View bgView = findViewById(R.id.design_bottom_sheet);
            if (bgView != null && bgIsTransparent) {
                bgView.setBackgroundResource(android.R.color.transparent);
            }
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //处理window的样式
            Window window = getWindow();
            if (window != null && bgIsTransparent) {
                //默认背景色透明
                window.setBackgroundDrawableResource(android.R.color.transparent);
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomDialog dialog = new BottomDialog(getContext(), getTheme());
        dialog.bgIsTransparent = getDialogBgIsTransparent();
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getDialogNoBg()) {
            setStyle(DialogFragment.STYLE_NO_TITLE, com.sum.library.R.style.dialog_no_bg);
        }
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

    //初始化设置Dialog背景色是否是透明的
    protected boolean getDialogBgIsTransparent() {
        return false;
    }

    //弹框没有背景
    protected boolean getDialogNoBg() {
        return false;
    }

    protected abstract int getLayoutId();

    protected abstract void initParams(View view);

    public void showFast(Context context) {
        if (context instanceof FragmentActivity) {
            if (!((FragmentActivity) context).isFinishing()) {
                FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();
                show(manager, "bottom_dialog");
            }
        }
    }

    @Override
    public void show(@NonNull FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch (Exception ignored) {
        }
    }

}
