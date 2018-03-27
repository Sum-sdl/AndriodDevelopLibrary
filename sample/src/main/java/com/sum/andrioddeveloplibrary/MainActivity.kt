package com.sum.andrioddeveloplibrary

import android.os.Bundle
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ToastUtils
import com.sum.andrioddeveloplibrary.fragment.ItemListDialogFragment
import com.sum.andrioddeveloplibrary.net.NetActivity
import com.sum.library.app.BaseActivity
import com.sum.library.framework.DownloadManager
import com.sum.library.ui.web.WebActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), ItemListDialogFragment.Listener {
    override fun onItemClicked(position: Int) {
        ToastUtils.showShort("click->" + position)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun statusBarTranslate(): Boolean {
        return true
    }

    override fun initParams() {

        b1.setOnClickListener { startActivity(StickyActivity::class.java) }
        b2.setOnClickListener { startActivity(NetActivity::class.java) }
        b3.setOnClickListener { startActivity(SwipeActivity::class.java) }
        b4.setOnClickListener { startActivity(CustomViewActivity::class.java) }
        b5.setOnClickListener {
            ItemListDialogFragment.newInstance(30).show(supportFragmentManager, "11")
        }
        b6.setOnClickListener {
            DownloadManager().setActivity(this).setLabel("更新内容")
                    .setUrl("http://app.house365.com/d/house365-rent-XiaZaiYe-v3.6.8.apk")
                    .setIsForceDownload(false).start()
        }
        b7.setOnClickListener {
            startActivity(SplashActivity::class.java)
        }
        b8.setOnClickListener {
            startActivity(WidgetUseActivity::class.java)
        }
        b10.setOnClickListener {
            startActivity(WebViewActivity::class.java)
        }
        b11.setOnClickListener {
            WebActivity.open(this, "自定义", "http://m.018929.com/index.php/phone/userReg", null, null)

        }
        b9.setOnClickListener {
            startActivity(UIActivity::class.java)
            overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PermissionUtils.permission(*PermissionConstants.getPermissions(PermissionConstants.STORAGE)).request()
    }

}
