package jetpack.demo.base

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.Utils


/**
 * Created by sdl on 2018/7/12.
 */
class DownloadUtils {

    private val mContext: Context = Utils.getApp()

    private var title = ""
    private var desc = "下载完成后，点击安装"

    private var mDwUrl = ""

    private var mLastId = 0L

    init {
        title = AppUtils.getAppName() + ".apk"
    }

    fun downloadStart(url: String) {
        mDwUrl = url
        if (mLastId != 0L) {
            clearCurrentTask(mLastId)
        }

        val downloadManager = mContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        mLastId = downloadManager.enqueue(getDownloadRequest())
    }

    private fun getDownloadRequest(): DownloadManager.Request {
        val uri = Uri.parse(mDwUrl)
        val req = DownloadManager.Request(uri)
        //设置WIFI下进行更新
        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
        //下载中和下载完后都显示通知栏
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        //使用系统默认的下载路径 此处为应用内 /android/data/packages ,所以兼容7.0
        req.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, title)
        //通知栏标题
        req.setTitle(title)
        //通知栏描述信息
        req.setDescription(desc)
        //设置类型为.apk
        req.setMimeType("application/vnd.android.package-archive")
        return req
    }

    private fun clearCurrentTask(downloadId: Long) {
        val dm = mContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        try {
            dm.remove(downloadId)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }
    }

}