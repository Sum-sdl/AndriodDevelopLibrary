package jetpack.demo.framework

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ToastUtils
import com.sum.andrioddeveloplibrary.R
import com.sum.library.app.BaseActivity
import com.sum.library.domain.ActionState
import com.sum.library.utils.Logger
import kotlinx.android.synthetic.main.activity_house_info_test.*
import java.util.*

class HouseInfoTestActivity : BaseActivity() {

    override fun getViewModel(): HouseViewModel {
//        return ViewModelProviders.of(this).get(HouseViewModel::class.java)
        return  ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)).get(HouseViewModel::class.java)
    }

    override fun expandActionDeal(state: ActionState?) {
        state?.let {
            Logger.e(state.toString() + "->" + state.state)
        }
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
//            viewModel.sendActionState(Random().nextInt(200))
            viewModel.sendActionState(ActionState.DIALOG_LOADING)
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
