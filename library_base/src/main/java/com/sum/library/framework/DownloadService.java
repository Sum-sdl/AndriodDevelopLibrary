package com.sum.library.framework;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

/**
 * Created by sdl on 2019-06-19.
 */
class DownloadService {

    private Context mContext;

    private String title = "";
    private String desc = "下载完成后，点击安装";

    private String mDwUrl = "";

    private Long mLastId = 0L;

    DownloadService(Context context) {
        mContext = context;
    }


    void downloadStart(String url) {
        mDwUrl = url;
        title = url.substring(url.lastIndexOf("/") + 1);
        if (mLastId != 0L) {
            clearCurrentTask(mLastId);
        }

        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);

        mLastId = downloadManager.enqueue(getDownloadRequest());
    }

    private DownloadManager.Request getDownloadRequest() {
        Uri uri = Uri.parse(mDwUrl);
        DownloadManager.Request req = new DownloadManager.Request(uri);
        //设置WIFI下进行更新
        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //下载中和下载完后都显示通知栏
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //使用系统默认的下载路径
        req.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, title);
        //通知栏标题
        req.setTitle(title);
        //通知栏描述信息
        req.setDescription(desc);
        //设置类型为.apk
        req.setMimeType("application/vnd.android.package-archive");
        return req;
    }

    private void clearCurrentTask(Long downloadId) {
        DownloadManager dm = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        try {
            dm.remove(downloadId);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
