package com.sum.library.utils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by sdl on 2018/8/28.
 */
public class FileDownloadHelper {

    public interface FileDownloadListener {
        void updateProgress(int progress, long bytesRead, long contentLength);

        void downloadSuccess(String filePath);

        void downloadError();
    }

    private OkHttpClient okHttpClient;

    private static final int TIME = 60 * 3;
    private static FileDownloadHelper mDownload;

    public static FileDownloadHelper instance() {
        if (mDownload == null) {
            synchronized (FileDownloadHelper.class) {
                mDownload = new FileDownloadHelper(TIME);
            }
        }
        return mDownload;
    }

    public static FileDownloadHelper newInstance(int seconds) {
        return new FileDownloadHelper(seconds);
    }

    private FileDownloadHelper(int time) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(time, TimeUnit.SECONDS)
                .readTimeout(time, TimeUnit.SECONDS)
                .writeTimeout(time, TimeUnit.SECONDS);
        okHttpClient = builder.build();
    }


    //对象实例操作
    private Call mFastCall;

    //只取消最近的一个下载操作
    public void downloadFastCancel() {
        if (mFastCall != null) {
            if (!mFastCall.isCanceled()) {
                mFastCall.cancel();
            }
            mFastCall = null;
        }
    }

    /**
     * 下载文件
     *
     * @param url              下载地址
     * @param targetFile       保存的本地文件地址
     * @param downloadListener 回调状态
     */
    public Call downloadFile(final String url, File targetFile, final FileDownloadListener downloadListener) {

        try {
            targetFile.deleteOnExit();
            targetFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        //Callback都在子线程,回调注意线程调整
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                if (downloadListener != null) {
                    downloadListener.downloadError();
                }
            }

            @Override
            public void onResponse(Call call, Response response) {
                InputStream is = null;
                FileOutputStream fos = null;
                boolean isDownloadOK;
                try {
                    if (response != null && response.isSuccessful()) {
                        ResponseBody body = response.body();
                        if (body == null) {
                            if (downloadListener != null) {
                                downloadListener.downloadError();
                            }
                            return;
                        }
                        //文件总长度
                        long fileSize = body.contentLength();
                        long fileSizeDownloaded = 0;
                        is = body.byteStream();
                        fos = new FileOutputStream(targetFile);
                        int count;
                        byte[] buffer = new byte[1024];
                        while ((count = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, count);
                            fileSizeDownloaded += count;
                            if (downloadListener != null) {
                                downloadListener.updateProgress((int) ((100 * fileSizeDownloaded) / fileSize), fileSizeDownloaded, fileSize);
                            }
                        }
                        fos.flush();
                        isDownloadOK = true;
                    } else {
                        isDownloadOK = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    isDownloadOK = false;
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (downloadListener != null) {
                    if (isDownloadOK) {
                        downloadListener.downloadSuccess(targetFile.getPath());
                    } else {
                        downloadListener.downloadError();
                    }
                }
            }
        });
        mFastCall = call;
        return call;
    }

}
