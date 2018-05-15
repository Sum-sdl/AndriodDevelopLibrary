package com.sum.library.view.sheet;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sum.library.R;

/**
 * Created by sdl on 2018/5/14.
 * 对话框操作界面
 */
public class DialogChooseView extends AppCompatDialogFragment {

    private String mTitle, mContent;
    private String mPos, mNeg;
    private boolean mCancel;

    private View.OnClickListener mPosLis, mNegLis;

    public DialogChooseView setCancel(boolean cancel) {
        mCancel = cancel;
        return this;
    }

    public DialogChooseView setTitle(String title) {
        mTitle = title;
        return this;
    }

    public DialogChooseView setMessage(String message) {
        mContent = message;
        return this;
    }

    public DialogChooseView setPos(String pos) {
        mPos = pos;
        return this;
    }

    public DialogChooseView setNeg(String neg) {
        mNeg = neg;
        return this;
    }

    public DialogChooseView setPosListener(View.OnClickListener pos) {
        mPosLis = pos;
        return this;
    }

    public DialogChooseView setNegListener(View.OnClickListener neg) {
        mNegLis = neg;
        return this;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window window = getDialog().getWindow();
        if (window != null) {
            if (Build.VERSION.SDK_INT >= 21) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        return inflater.inflate(R.layout.cus_dialog_choice, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(@NonNull View view) {
        TextView title = view.findViewById(R.id.choice_container_title);
        TextView content = view.findViewById(R.id.choice_container_content);

        TextView neg = view.findViewById(R.id.choice_container_negative);
        TextView pos = view.findViewById(R.id.choice_container_positive);

        if (!TextUtils.isEmpty(mTitle)) {
            title.setVisibility(View.VISIBLE);
            title.setText(mTitle);
        } else {
            title.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mContent)) {
            content.setText(mContent);
        }
        if (!TextUtils.isEmpty(mPos)) {
            pos.setText(mPos);
        }
        if (!TextUtils.isEmpty(mNeg)) {
            neg.setText(mNeg);
        }

        neg.setOnClickListener(v -> {
            dismiss();
            if (mNegLis != null) {
                mNegLis.onClick(v);
            }
        });

        pos.setOnClickListener(v -> {
            dismiss();
            if (mPosLis != null) {
                mPosLis.onClick(v);
            }
        });
        setCancelable(mCancel);
    }
}
