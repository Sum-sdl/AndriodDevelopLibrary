package com.sum.andrioddeveloplibrary

import add_class.utils.FileOpen
import android.os.Bundle
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ToastUtils
import com.sum.andrioddeveloplibrary.aa_surface_test.SurfaceActivity
import com.sum.andrioddeveloplibrary.activity.BridgeWebViewActivity
import com.sum.andrioddeveloplibrary.coroutine.CoroutineActivity
import com.sum.andrioddeveloplibrary.fragment.ItemListDialogFragment
import com.sum.andrioddeveloplibrary.net.NetActivity
import com.sum.andrioddeveloplibrary.testActivity.ServiceTestActivity
import com.sum.library.AppFileConfig
import com.sum.library.app.BaseActivity
import com.sum.library.framework.AppDownloadManager
import com.sum.library.utils.FileDownloadHelper
import com.sum.library.utils.Logger
import com.sum.library.utils.mainThread
import com.sum.library.view.widget.DialogMaker
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

    override fun statusBarTranslate(): Boolean {
        return false
    }

    override fun initParams() {

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
            AppDownloadManager().setActivity(this).setLabel("更新内容")
                    .setUrl(url)
                    .setIsForceDownload(false).start()

        }
        b7.setOnClickListener {
            startActivity(SplashActivity::class.java)
        }
        b8.setOnClickListener {
            startActivity(LibWidgetUseActivity::class.java)
        }
        b9.setOnClickListener {
            startActivity(LibUIActivity::class.java)
            overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit)
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
    }


    private fun download(url: String) {
        val mLocalFile = AppFileConfig.getFileDirectory().path + "/20180913150705.jpg"
        Logger.e("file->$mLocalFile")
        val file = File(mLocalFile)
        if (file.exists()) {
            val intent = FileOpen.getOpenFileIntent(mLocalFile)
            startActivity(intent)
            return
        }

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

        PermissionUtils.permission(*PermissionConstants.getPermissions(PermissionConstants.STORAGE)).request()
    }

}
