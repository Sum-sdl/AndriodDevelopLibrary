package jetpack.demo.databing

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import com.sum.andrioddeveloplibrary.R
import com.sum.andrioddeveloplibrary.databinding.ActivityDataBindTestBinding
import kotlinx.android.synthetic.main.activity_data_bind_test.*

class DataBindTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        val binding = DataBindingUtil.setContentView<ActivityDataBindTestBinding>(this, R.layout.activity_data_bind_test)

        binding.viewModel = viewModel

        var index = 100

        btn_1.setOnClickListener {
            index += 100
            viewModel.mInfoLiveData.value = index.toString()
        }

        viewModel.mRespotryData.observe(this, Observer {
            it?.let {
                ToastUtils.showLong("remoteData->" + it.data)
            }
        })
    }
}
