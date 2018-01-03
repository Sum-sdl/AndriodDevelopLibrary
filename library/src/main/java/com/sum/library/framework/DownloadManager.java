package com.sum.library.framework;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sum.library.AppFileConfig;
import com.sum.library.R;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * Created by Sum on 15/11/22.
 */
public class DownloadManager {

    private String downloadUrl;

    private Activity mActivity;

    private boolean isNeedShowView = true, isForceDownload;

    private File mSaveFile;
    private String mFileName;
    private String mLabel;
    private ProgressBar mProgressBar;
    private TextView tv_process;
    private AlertDialog mDialog;

    public DownloadManager() {
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
        View view = LayoutInflater.from(mActivity).inflate(R.layout.download_view, null);
        final LinearLayout ll_progress = view.findViewById(R.id.ll_progress);
        ll_progress.setVisibility(View.GONE);

        mProgressBar = view.findViewById(R.id.download_pb);
        tv_process = view.findViewById(R.id.tv_process);

        final TextView download_label = view.findViewById(R.id.download_label);
        final Button btn_cancel = view.findViewById(R.id.btn_cancel);
        final Button btn_start = view.findViewById(R.id.btn_start);
        final Button btn_to_back = view.findViewById(R.id.btn_to_back);

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.dialog_alert);
        builder.setView(view);
        builder.setCancelable(false);
        if (!TextUtils.isEmpty(mLabel)) {
            download_label.setText(mLabel);
        }
        if (isForceDownload) {
            btn_cancel.setText(mActivity.getText(R.string.download_quit));
        }

        btn_to_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isForceDownload) {
                    AppUtils.exitApp();
                } else {
                    mDialog.dismiss();
                }
            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_start.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.GONE);
                download_label.setVisibility(View.GONE);
                btn_to_back.setVisibility(View.VISIBLE);
                ll_progress.setVisibility(View.VISIBLE);
                mProgressBar.setMax(100);
                downFile();
            }
        });
        mDialog = builder.show();
    }

    public DownloadManager setLabel(String label) {
        this.mLabel = label;
        return this;
    }

    public DownloadManager setIsForceDownload(boolean isForceDownload) {
        this.isForceDownload = isForceDownload;
        return this;
    }

    public DownloadManager setIsNeedShowView(boolean isNeedShowView) {
        this.isNeedShowView = isNeedShowView;
        return this;
    }

    public DownloadManager setActivity(Activity activity) {
        mActivity = activity;
        return this;
    }

    public DownloadManager setUrl(String url) {
        downloadUrl = url;
        return this;
    }

    private void downFile() {
        if (TextUtils.isEmpty(downloadUrl)) {
            LogUtil.e("download url is null");
            return;
        }
        LogUtil.e("start download: " + downloadUrl);
        getFileName();
        RequestParams params = new RequestParams(downloadUrl);
        params.setAutoRename(true);
        params.setAutoResume(true);
        params.setMaxRetryCount(1);
        params.setConnectTimeout(30 * 1000);
        params.setReadTimeout(30 * 1000);
        mSaveFile = new File(AppFileConfig.getDownloadDirectoryFile().getPath()
                + File.separator + mFileName);
        params.setSaveFilePath(mSaveFile.getPath());
        params.setCancelFast(false);

        x.http().get(params, new downloadCallback());
    }

    private String getFileName() {
        int index = downloadUrl.lastIndexOf("/");
        mFileName = downloadUrl.substring(index + 1, downloadUrl.length());
        return mFileName;
    }


    class downloadCallback implements Callback.CommonCallback<File>,
            Callback.ProgressCallback<File> {


        @Override
        public void onWaiting() {
        }

        @Override
        public void onStarted() {
        }

        @Override
        public void onLoading(long total, long current, boolean isDownloading) {
            int percent = (int) (current * 100 / total);
            if (mProgressBar != null) {
                mProgressBar.setProgress(percent);
                tv_process.setText(percent + "%");
            }
        }

        @Override
        public void onSuccess(File result) {
            ToastUtils.showShort("下载完成");
            LogUtil.e("start install file: " + result.getAbsolutePath());
            if (result.getName().endsWith(".apk")) {
                install(mActivity, result);
            }
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            ToastUtils.showShort("下载失败");
        }

        @Override
        public void onCancelled(CancelledException cex) {
            ToastUtils.showShort("下载取消");
        }

        @Override
        public void onFinished() {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }

    }

    private void install(Context context, File result) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        Uri uri = AppFileConfig.getAppSelfUri(context, result, null);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
