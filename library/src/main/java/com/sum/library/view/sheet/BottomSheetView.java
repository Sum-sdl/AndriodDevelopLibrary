package com.sum.library.view.sheet;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sum.library.R;

/**
 * Created by sdl on 2018/5/14.
 * 底部操作界面
 */
public class BottomSheetView extends BottomSheetDialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomDialog(getContext(), getTheme());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setCancelable(false);
        return inflater.inflate(R.layout.cus_bs_single_view, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private class BottomDialog extends BottomSheetDialog {

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
}
