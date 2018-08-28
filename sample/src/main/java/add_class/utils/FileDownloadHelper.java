package add_class.utils;

import com.blankj.utilcode.util.FileUtils;

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
        void onUpdateProgress(int progress, long bytesRead, long contentLength);

        void onDownloadSuccess(String filePath);

        void onDownloadError();
    }

    private OkHttpClient okHttpClient;

    private static final int TIME = 60 * 2;
    private static FileDownloadHelper mDownload;

    public static FileDownloadHelper instance() {
        if (mDownload == null) {
            synchronized (FileDownloadHelper.class) {
                mDownload = new FileDownloadHelper();
            }
        }
        return mDownload;
    }

    private FileDownloadHelper() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(TIME, TimeUnit.SECONDS)
                .writeTimeout(TIME, TimeUnit.SECONDS);
        okHttpClient = builder.build();
    }


    /**
     * 下载文件
     *
     * @param url              下载地址
     * @param targetFile       保存的文件地址
     * @param downloadListener 回调状态
     */
    public void downloadFile(final String url, File targetFile, final FileDownloadListener downloadListener) {
        FileUtils.createFileByDeleteOldFile(targetFile);
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        //Callback都在子线程,回调注意线程调整
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                if (downloadListener != null) {
                    downloadListener.onDownloadError();
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
                                downloadListener.onDownloadError();
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
                                downloadListener.onUpdateProgress((int) ((100 * fileSizeDownloaded) / fileSize), fileSizeDownloaded, fileSize);
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
                        downloadListener.onDownloadSuccess(targetFile.getPath());
                    } else {
                        downloadListener.onDownloadError();
                    }
                }
            }
        });
    }

}
