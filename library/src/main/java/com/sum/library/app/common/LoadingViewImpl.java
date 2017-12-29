package com.sum.library.app.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.sum.library.view.widget.DialogMaker;


/**
 * Created by Sum on 16/6/20.
 */
public class LoadingViewImpl implements LoadingView {

    private Context mContext;

    private AlertDialog mLoadDialog;

    private ProgressDialog mProgressDialog;

    public LoadingViewImpl(Context context) {
        mContext = context;
    }

    @Override
    public void showLoading() {
        if (mLoadDialog == null) {
            mLoadDialog = DialogMaker.showLoadingDialog(mContext, null);
        }
        if (!mLoadDialog.isShowing()) {
            mLoadDialog.show();
        }
    }

    @Override
    public void showLoading(String msg) {
        showLoading(msg, true);
    }

    @Override
    public void showLoading(String msg, boolean cancelable) {
        mProgressDialog = DialogMaker.showProgress(mContext, "", msg, cancelable);
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

