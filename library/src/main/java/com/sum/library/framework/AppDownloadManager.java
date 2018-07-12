package com.sum.library.framework;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sum.library.R;
import com.sum.library.utils.Logger;

/**
 * Created by Sum on 15/11/22.
 */
public class AppDownloadManager {

    private String downloadUrl;

    private Activity mActivity;

    private boolean isNeedShowView = true, isForceDownload;

    private UpdateService mDownload;

    private String mLabel;

    private AlertDialog mDialog;

    public AppDownloadManager() {
        mDownload = new UpdateService();
    }

    public void start() {
        if (isNeedShowView) {
            showChooseView();
        } else {
            downFile();
        }
    }

    private void showChooseView() {
        if (mActivity == null) {
            return;
        }
        View view = LayoutInflater.from(mActivity).inflate(R.layout.pub_download_view, null);
        final LinearLayout ll_progress = view.findViewById(R.id.ll_progress);
        ll_progress.setVisibility(View.GONE);

        final TextView download_label = view.findViewById(R.id.download_label);
        final Button btn_cancel = view.findViewById(R.id.btn_cancel);
        final Button btn_start = view.findViewById(R.id.btn_start);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setView(view);
        builder.setCancelable(false);
        if (!TextUtils.isEmpty(mLabel)) {
            download_label.setText(mLabel);
        }
        if (isForceDownload) {
            btn_cancel.setText(mActivity.getText(R.string.download_quit));
        }

        btn_cancel.setOnClickListener(v -> {
            if (isForceDownload) {
                AppUtils.exitApp();
            } else {
                mDialog.dismiss();
            }
        });

        btn_start.setOnClickListener(v -> {
            mDialog.dismiss();
            downFile();
        });
        mDialog = builder.show();
    }

    public AppDownloadManager setLabel(String label) {
        this.mLabel = label;
        return this;
    }

    public AppDownloadManager setIsForceDownload(boolean isForceDownload) {
        this.isForceDownload = isForceDownload;
        return this;
    }

    public AppDownloadManager setIsNeedShowView(boolean isNeedShowView) {
        this.isNeedShowView = isNeedShowView;
        return this;
    }

    public AppDownloadManager setActivity(Activity activity) {
        mActivity = activity;
        return this;
    }

    public AppDownloadManager setUrl(String url) {
        downloadUrl = url;
        return this;
    }

    private void downFile() {
        if (TextUtils.isEmpty(downloadUrl)) {
            Logger.e("download url is null");
            return;
        }
        ToastUtils.showLong("后台下载中...");
        mDownload.downloadStart(downloadUrl);
    }
}
