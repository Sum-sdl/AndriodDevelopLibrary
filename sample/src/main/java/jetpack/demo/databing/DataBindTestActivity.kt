package jetpack.demo.databing

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import com.sum.library.utils.Logger
import com.sum.library.domain.ActionState
import kotlinx.android.synthetic.main.activity_data_bind_test.*

class DataBindTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

//        val binding = DataBindingUtil.setContentView<ActivityDataBindTestBinding>(this, R.layout.activity_data_bind_test)
//
//        binding.viewModel = viewModel

//        setContentView(R.id.activity_data_bind_test)

        var index = 100

        btn_1.setOnClickListener {
            index += 100
            viewModel.mInfoLiveData.value = index.toString()
        }

        btn_2.setOnClickListener {
            viewModel.sendActionState(ActionState.TOAST)
        }

        viewModel.mRespotryData.observe(this, Observer {
            it?.let {
                ToastUtils.showLong("remoteData->" + it.data)
            }
        })

        //base
        viewModel.registerActionState(this, Observer {
            it?.let {
                Logger.e(it.toString())
                ToastUtils.showLong("state->" + it.state)
            }
        })
    }
}
