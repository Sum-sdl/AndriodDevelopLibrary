package com.sum.andrioddeveloplibrary

import add_class.utils.FileOpen
import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.ImageView
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.sum.andrioddeveloplibrary.aa_surface_test.SurfaceActivity
import com.sum.andrioddeveloplibrary.activity.BridgeWebViewActivity
import com.sum.andrioddeveloplibrary.api.IApiFun
import com.sum.andrioddeveloplibrary.autosize.AutoSizeTestActivity
import com.sum.andrioddeveloplibrary.coroutine.CoroutineActivity
import com.sum.andrioddeveloplibrary.fragment.ItemListDialogFragment
import com.sum.andrioddeveloplibrary.net.NetActivity
import com.sum.andrioddeveloplibrary.testActivity.ServiceTestActivity
import com.sum.andrioddeveloplibrary.view_delegate.ViewDelegateActivity
import com.sum.cache.CacheConfiguration
import com.sum.cache.CacheManager
import com.sum.cache.CacheOption
import com.sum.cache.util.CacheLog
import com.sum.library.app.BaseActivity
import com.sum.library.storage.AppFileStorage
import com.sum.library.utils.ACache
import com.sum.library.utils.Logger
import com.sum.library.utils.TaskExecutor.mainThread
import com.sum.library.view.widget.DialogMaker
import com.sum.library_network.download.FileDownloadHelper
import com.zp.apt.api.ApiFinder
import jetpack.demo.NewStartActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : BaseActivity(), ItemListDialogFragment.Listener {
    override fun onItemClicked(position: Int) {
        ToastUtils.showShort("click->" + position)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initParams() {

        b21.setOnClickListener { startActivity(ViewDelegateActivity::class.java) }
        b19.setOnClickListener { startActivity(BlockcanaryActivity::class.java) }
        b18.setOnClickListener { startActivity(CoroutineActivity::class.java) }
        b17.setOnClickListener { startActivity(BridgeWebViewActivity::class.java) }
        b16.setOnClickListener { startActivity(SurfaceActivity::class.java) }
        b1.setOnClickListener { startActivity(StickyActivity::class.java) }
        b2.setOnClickListener { startActivity(NetActivity::class.java) }
        b3.setOnClickListener { startActivity(SwipeActivity::class.java) }
        b4.setOnClickListener { startActivity(CustomViewActivity::class.java) }
        b5.setOnClickListener {
            startActivity(DialogTestActivity::class.java)
        }
        b6.setOnClickListener {
            val url = "http://app.house365.com/d/house365-rent-XiaZaiYe-v3.6.8.apk"
//            AppDownloadManager().setActivity(this).setLabel("更新内容")
//                    .setUrl(url)
//                    .setIsForceDownload(false).start()

        }
        b7.setOnClickListener {
            startActivity(AutoSizeTestActivity::class.java)
        }
        b8.setOnClickListener {
            startActivity(LibWidgetUseActivity::class.java)
        }
        b9.setOnClickListener {
            startActivity(LibUIActivity::class.java)
        }

        b12.setOnClickListener {
            startActivity(ServiceTestActivity::class.java)
        }
        b13.setOnClickListener {
            startActivity(EncryptionActivity::class.java)
        }
        b14.setOnClickListener {
            startActivity(NewStartActivity::class.java)
        }

        b15.setOnClickListener {
            val url = "http://oa.house365.com/attachment_new/2018/09/13/347511066/%CE%A2%D0%C5%CD%BC%C6%AC_20180913150705.jpg"
            download(url)
        }


        //缓存初始化
        CacheManager.getInstance().init(CacheConfiguration.Builder(this).build())

        b20.setOnClickListener {
//            wms()
            CacheLog.setTAG("cache", true)
            CacheManager.getInstance().save("k","Hello",
                    CacheOption.Builder().isSyncRun(true).build())

            ToastUtils.showLong(CacheManager.getInstance().loadString("k"))
        }

        b22.setOnClickListener {
            protoTest()
        }

        b23.setOnClickListener {
            ApiFinder.get(IApiFun::class.java).toast()
        }
    }

    private fun protoTest(){

    }

    /**
     * WindowManager->WindowManagerImpl
     *
     * Window -> PhoneWindow ->DecorView根布局
     * */
    private fun wms() {

        val wm: WindowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val view = ImageView(this)
        view.setBackgroundColor(Color.RED)
        view.setImageResource(R.mipmap.ic_launcher)

        val layout = WindowManager.LayoutParams()

        //view size
        layout.width = 200
        layout.height = 700

        //收到Gravity影响，注意这个x、y点的意义
        //x,y是相对应gravity的一个偏移量，如果超出后界面，最少取界面位置
        layout.x = ScreenUtils.getScreenWidth()
        layout.y = ScreenUtils.getScreenHeight() - 1000
        layout.gravity = Gravity.TOP or Gravity.START
        layout.format = PixelFormat.RGB_565

        layout.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE

//        layout.type = WindowManager.LayoutParams.TYPE_APPLICATION //应用内部

        layout.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY//需要权限

        wm.addView(view, layout)

    }


    private fun download(url: String) {
        val mLocalFile = AppFileStorage.getStorageRootDir().path + "/20180913150705.jpg"
        Logger.e("file->$mLocalFile")
        val file = File(mLocalFile)
//        if (file.exists()) {
//            val intent = FileOpen.getOpenFileIntent(mLocalFile)
//            startActivity(intent)
//            return
//        }

        val dialog = DialogMaker.showProgress(this, "", "文件下载中...", false)
        FileDownloadHelper.instance().downloadFile(url, file, object : FileDownloadHelper.FileDownloadListener {
            private var mCurPos = 0
            override fun updateProgress(pos: Int, p1: Long, p2: Long) {
                if (mCurPos != pos) {
                    mCurPos = pos
                    mainThread {
                        dialog.setMessage("文件下载中...$pos%")
                    }
                }
            }

            override fun downloadError() {
                mainThread {
                    dialog.dismiss()
                    ToastUtils.showShort("文件下载异常,请重试")
                }
            }

            override fun downloadSuccess(target: String) {
                mainThread {
                    dialog.dismiss()
                    ToastUtils.showShort("文件下载成功")
                    val intent = FileOpen.getOpenFileIntent(target)
                    startActivity(intent)
                }
            }
        })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PermissionUtils.permission(*PermissionConstants.getPermissions(PermissionConstants.STORAGE), *PermissionConstants.getPermissions(PermissionConstants.PHONE)).request()

        var time = System.currentTimeMillis() - SumApp.mOpenStartTime
        LogUtils.e("open time->$time")
        time = System.currentTimeMillis() - (ACache.get(this).getAsObject("time") as Long)
        LogUtils.e("open time22->$time")

    }

}
