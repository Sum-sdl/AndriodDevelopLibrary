package com.sum.library.utils;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by sdl on 2018/5/14.
 */
public class KeyboardChangeListener implements ViewTreeObserver.OnGlobalLayoutListener {

    private View mContentView;
    private int mOriginHeight;
    private int mPreHeight;
    private KeyBoardListener mKeyBoardListen;

    public interface KeyBoardListener {
        void onKeyboardChange(boolean isShow, int keyboardHeight);
    }

    public void setKeyBoardListener(KeyBoardListener keyBoardListen) {
        this.mKeyBoardListen = keyBoardListen;
    }

    public KeyboardChangeListener(Activity contextObj) {
        if (contextObj == null) {
            return;
        }
        mContentView =  contextObj.findViewById(android.R.id.content);
        addContentTreeObserver();
    }

    public KeyboardChangeListener(Fragment fragment) {
        mContentView = fragment.getView();
        addContentTreeObserver();
    }

    private void addContentTreeObserver() {
        if (mContentView != null) {
            mContentView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        }
    }

    @Override
    public void onGlobalLayout() {
        int currHeight = mContentView.getHeight();
        if (currHeight == 0) {
            return;
        }
        boolean hasChange = false;
        if (mPreHeight == 0) {
            mPreHeight = currHeight;
            mOriginHeight = currHeight;
        } else {
            if (mPreHeight != currHeight) {
                hasChange = true;
                mPreHeight = currHeight;
            } else {
                hasChange = false;
            }
        }
        if (hasChange) {
            boolean isShow;
            int keyboardHeight = 0;
            if (mOriginHeight == currHeight) {
                //hidden
                isShow = false;
            } else {
                //show
                keyboardHeight = mOriginHeight - currHeight;
                isShow = true;
            }

            if (mKeyBoardListen != null) {
                mKeyBoardListen.onKeyboardChange(isShow, keyboardHeight);
            }
        }
    }

    public void destroy() {
        if (mContentView != null) {
            mContentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }
}
