package com.sum.library.view.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sum.library.R;

public class ClearEditText extends AppCompatEditText implements View.OnTouchListener,
        View.OnFocusChangeListener, TextWatcher {

    Drawable clearDrawable;
    OnFocusChangeListener focusChangeListener;

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        clearDrawable = context.getResources().getDrawable(R.mipmap.lib_ic_edit_delete);
        clearDrawable.setBounds(0, 0, clearDrawable.getIntrinsicWidth(), clearDrawable.getIntrinsicHeight());
        setClearIconVisible(false);
        setOnTouchListener(this);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
        if (focusChangeListener != null) {
            focusChangeListener.onFocusChange(v, hasFocus);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            if (x > getWidth() - getPaddingRight() - clearDrawable.getIntrinsicWidth() && clearDrawable.isVisible()) {
                setText("");
            }
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (isFocused()) {
            setClearIconVisible(getText().length() > 0);
        }
    }

    private void setClearIconVisible(boolean visible) {
        clearDrawable.setVisible(visible, false);
        Drawable[] drawables = getCompoundDrawables();
        setCompoundDrawables(drawables[0], drawables[1], visible ? clearDrawable : null, drawables[3]);
    }

    public interface OnFocusChangeListener {
        void onFocusChange(View v, boolean hasFocus);
    }

    public void setFocusChangeListener(OnFocusChangeListener focusChangeListener) {
        this.focusChangeListener = focusChangeListener;
    }
}
