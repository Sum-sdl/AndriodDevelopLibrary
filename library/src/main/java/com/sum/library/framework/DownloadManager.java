package com.sum.library.framework;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

    private boolean isNeedShowView, isForceDownload;

    private File mSaveFile;
    private String mFileName;
    private String mLabel;
    private ProgressBar mProgressBar;
    private TextView tv_process;
    private Dialog mDialog;

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
        final LinearLayout ll_action = (LinearLayout) view.findViewById(R.id.ll_action);
        final LinearLayout ll_progress = (LinearLayout) view.findViewById(R.id.ll_progress);
        mProgressBar = (ProgressBar) view.findViewById(R.id.download_pb);
        tv_process = (TextView) view.findViewById(R.id.tv_process);

        TextView download_label = (TextView) view.findViewById(R.id.download_label);
        final Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        final Button btn_start = (Button) view.findViewById(R.id.btn_start);

        mDialog = new Dialog(mActivity, R.style.dialog_style);

        mDialog.setContentView(view);
        if (!TextUtils.isEmpty(mLabel)) {
            download_label.setText(mLabel);
        }
        if (isForceDownload) {
            btn_cancel.setText(mActivity.getText(R.string.download_quit));
        }

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isForceDownload) {
                    mActivity.finish();
                } else {
                    mDialog.dismiss();
                }
            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_action.setVisibility(View.GONE);
                btn_cancel.setEnabled(false);
                btn_start.setEnabled(false);

                ll_progress.setVisibility(View.VISIBLE);
                mProgressBar.setMax(100);
                downFile();
            }
        });

        mDialog.setCancelable(false);
        mDialog.show();

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.6f, 1);
        scaleX.setDuration(200);
        scaleX.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.6f, 1);
        scaleY.setDuration(200);
        scaleY.setInterpolator(new AccelerateDecelerateInterpolator());

        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY);
        set.start();
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
        LogUtil.i("start download: " + downloadUrl);
        getFileName();
        RequestParams params = new RequestParams(downloadUrl);
        params.setAutoRename(true);
        params.setAutoResume(true);

        mSaveFile = new File(AppFileConfig.getDownloadAppFile().getPath() + "/" + mFileName);
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
            Toast.makeText(x.app(), "下载完成", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            Toast.makeText(x.app(), "下载失败", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancelled(CancelledException cex) {
            LogUtil.e("onCancelled");
        }

        @Override
        public void onFinished() {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            String fileName = mSaveFile.getAbsolutePath();

            LogUtil.i("start install file: " + fileName);
            if (mSaveFile.getName().endsWith(".apk")) {
                install(x.app());
            }
        }

    }

    private void install(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(mSaveFile), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
