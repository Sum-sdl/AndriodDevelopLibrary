package com.sum.library.view.sheet;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.sum.library.R;
import com.sum.library.app.BaseDialogFragment;

/**
 * Created by sdl on 2018/5/14.
 * 对话框操作界面
 */
public class DialogChooseView extends BaseDialogFragment {

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

    @Override
    protected int getDialogShowAnimation() {
        return 0;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.cus_dialog_choice;
    }

    @Override
    protected void initParams(View view) {
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
