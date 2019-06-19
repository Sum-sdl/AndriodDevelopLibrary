package com.sum.library.framework;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by sdl on 2018/7/12.
 */
public class DownloadReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (id != -1) {
                Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show();
                // 兼容Android 8.0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //先获取是否有安装未知来源应用的权限
                    boolean haveInstallPermission = context.getPackageManager().canRequestPackageInstalls();
                    if (!haveInstallPermission) {//没有权限
                        //注意这个是8.0新API
                        Intent setting = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                                Uri.parse("package:" + context.getPackageName()));
                        context.startActivity(setting);
                    } else {
                        installApk(context, id);
                    }
                } else {
                    installApk(context, id);
                }
            }
        } else if (intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
            //处理 如果还未完成下载，用户点击Notification ，跳转到下载中心
            Intent viewDownloadIntent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
            viewDownloadIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(viewDownloadIntent);
        }
    }

    private void installApk(Context context, long downloadApkId) {
        DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (dManager == null) {
            return;
        }
        Intent install = new Intent(Intent.ACTION_VIEW);
        Uri downloadFileUri = dManager.getUriForDownloadedFile(downloadApkId);
        if (downloadFileUri != null) {
            Log.d("DownloadReceiver", "install-> " + downloadFileUri.toString());
            install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            context.startActivity(install);
        } else {
            Log.e("DownloadReceiver", "download error");
        }
    }
}
