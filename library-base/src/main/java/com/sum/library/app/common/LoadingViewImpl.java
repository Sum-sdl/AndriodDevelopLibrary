package com.sum.library.app.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AlertDialog;

import com.sum.library.utils.Logger;
import com.sum.library.view.widget.DialogMaker;

import java.lang.ref.WeakReference;


/**
 * Created by Sum on 16/6/20.
 */
class LoadingViewImpl implements LoadingView {

    private WeakReference<Activity> mReference;

    private AlertDialog mLoadDialog;

    private ProgressDialog mProgressDialog;

    LoadingViewImpl(Activity context) {
        mReference = new WeakReference<>(context);
    }

    @Override
    public void showLoading() {
        showLoading("");
    }

    @Override
    public void showLoading(String msg) {
        showLoading(msg, true);
    }

    @Override
    public void showLoading(String msg, boolean cancelable) {
        if (mLoadDialog != null && mLoadDialog.isShowing()) {
            mLoadDialog.dismiss();
        }
        Activity context = mReference.get();
        if (context == null || context.isFinishing()) {
            Logger.e("!!! context finish");
            return;
        }
        mLoadDialog = DialogMaker.showLoadingDialog(context, msg, cancelable);
        if (!mLoadDialog.isShowing()) {
            mLoadDialog.show();
        }
    }

    @Override
    public void showProgressLoading(String msg, boolean cancelable) {
        Activity context = mReference.get();
        if (context == null || context.isFinishing()) {
            Logger.e("!!! context finish");
            return;
        }
        mProgressDialog = DialogMaker.showProgress(context, "", msg, cancelable);
        if (mProgressDialog != null) {
            mProgressDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (mLoadDialog != null && mLoadDialog.isShowing()) {
            mLoadDialog.dismiss();
        }
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}

