package com.sum.library.view.widget;

import android.content.Context;
import androidx.annotation.IdRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;

/**
 * Created by Summer on 2016/9/8.
 * 自定义的一组单选框,仅负责状态切换
 */
public class MultiRadioGroup extends LinearLayout {

    private int mCheckedId = -1;

    public MultiRadioGroup(Context context) {
        this(context, null, 0);
    }

    public MultiRadioGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiRadioGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private OnMultiCheckedChangeListener mOnCheckedChangeListener;

    public interface OnMultiCheckedChangeListener {
        void onCheckedChanged(MultiRadioGroup group, @IdRes int checkedId);
    }

    /**
     * 将上一个选择的id反选，选择当前传入的id，记录当前的id
     * <p/>
     * 对应View需要实现Checkable接口，回调到该接口状态
     */
    public void check(@IdRes int id) {
        // don't even bother
        if (id != -1 && (id == mCheckedId)) {
            return;
        }

        if (mCheckedId != -1) {
            setCheckedStateForView(mCheckedId, false);
        }

        if (id != -1) {
            setCheckedStateForView(id, true);
        }

        setCheckedId(id);
    }

    private void setCheckedId(@IdRes int id) {
        mCheckedId = id;
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
        }
    }

    /**
     * @return 返回当前选中View的Id
     */
    @IdRes
    public int getCheckedRadioButtonId() {
        return mCheckedId;
    }

    private void setCheckedStateForView(int viewId, boolean checked) {
        View checkedView = findViewById(viewId);
        if (checkedView != null && checkedView instanceof Checkable) {
            ((Checkable) checkedView).setChecked(checked);
        }
    }
}
