package jetpack.demo.framework

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import com.blankj.utilcode.util.ToastUtils
import com.sum.andrioddeveloplibrary.R
import com.sum.library.app.BaseActivity
import com.sum.library.utils.Logger
import kotlinx.android.synthetic.main.activity_house_info_test.*
import java.util.*

class HouseInfoTestActivity : BaseActivity() {

    override fun getViewModel(): HouseViewModel {
        return ViewModelProviders.of(this).get(HouseViewModel::class.java)
    }

    override fun getLayoutId(): Int = R.layout.activity_house_info_test

    override fun initParams() {

        viewModel.mRespotryData.observe(this, Observer {
            it?.let {
                Logger.e("1->" + it.toString())
                ToastUtils.showLong(it.data)
            }
        })

        viewModel.mRespotryUpload.observe(this, Observer {
            it?.let {
                Logger.e("2->" + it.toString())
                ToastUtils.showLong(it.data)
            }
        })

        bt1.setOnClickListener {
            viewModel.sendActionState(Random().nextInt(200))
        }

        bt2.setOnClickListener {
            viewModel.click()
        }

        bt3.setOnClickListener {
            viewModel.upload()
        }

        viewModel.loadData()
    }

}
